package com.micro.camera

import android.graphics.Bitmap
import android.graphics.Rect
import android.util.Size
import com.alibaba.fastjson.JSON
import com.apkfuns.logutils.LogUtils
import com.joye.jiang.common.sdk.*
import com.micro.camera.warp.CameraOffsetBean
import com.micro.camera.warp.CameraWarpBean
import com.micro.opencv.utils.distance
import com.micro.opencv.utils.toBitmap
import com.micro.opencv.utils.toMat
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import io.reactivex.rxjava3.core.Observable
import org.opencv.android.OpenCVLoader
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.imgproc.Imgproc
import org.opencv.utils.Converters

/**
 * @see com.micro.camera.warp.CameraWarpManager
 */
@Deprecated("use CameraWarpManager")
class CameraOffsetManager {

    /**
     * 摄像头相对于屏幕的投影映射矩阵
     */
    private var cameraToScreen: CameraWarpBean? = null

    /**
     * 摄像头投影映射后相对于屏幕的偏移
     */
    private var cameraToScreenOffset: CameraOffsetBean? = null

    /**
     * 红外摄像头对于屏幕的投影映射矩阵
     */
    private var irCameraToScreen: CameraWarpBean? = null


    /**
     * 屏幕在摄像头的里面的矩形
     */
    private var screenRect: Rect? = null

    private var isOpenInit = false

    init {
        isOpenInit = OpenCVLoader.initDebug()
    }

    companion object {
        @JvmStatic
        val instance = SingletonHolder.holder

        private const val SP_CAMERA_TO_SCREEN = "sp_camera_to_screen"

        private const val SP_IR_CAMERA_TO_SCREEN = "sp_ir_camera_to_screen"

        private const val SP_CAMERA_TO_SCREEN_OFFSET = "sp_camera_to_screen_offset"
    }

    private object SingletonHolder {
        val holder = CameraOffsetManager()
    }

    fun initLoad() {
        Observable.just(SharedPreferenceUtils.instance.getString(SP_CAMERA_TO_SCREEN))
            .filter {
                it.trim().isNotBlank()
            }
            .map {
                LogUtils.d("cameraOffset init load ${it}")
                JSON.parseObject(it, CameraWarpBean::class.java)
            }
            .subscribe({
                cameraToScreen = it
                initWarpTrans()
            }, {
                it.printStackTrace()
            })
        Observable.just(SharedPreferenceUtils.instance.getString(SP_CAMERA_TO_SCREEN_OFFSET))
            .filter {
                it.trim().isNotBlank()
            }
            .map {
                JSON.parseObject(it, CameraOffsetBean::class.java)
            }
            .subscribe({
                cameraToScreenOffset = it
            }, {
                it.printStackTrace()
            })
    }

    fun updateIRCameraToScreen(
        srcPointList: MutableList<Point>,
        dstPointList: MutableList<Point>,
        screenSize: Size
    ) {
        irCameraToScreen = CameraWarpBean()
        srcPointList.forEach {
            irCameraToScreen?.srcPointList?.add(mutableListOf(it.x, it.y))
        }
        dstPointList.forEach {
            irCameraToScreen?.dstPointList?.add(mutableListOf(it.x, it.y))
        }
        irCameraToScreen?.screenSize?.add(screenSize.width)
        irCameraToScreen?.screenSize?.add(screenSize.height)

        irPerspectiveMat = null
        LogUtils.d("updateCameraToScreen ${JSON.toJSONString(irCameraToScreen)}")
        SharedPreferenceUtils.instance.put(
            SP_IR_CAMERA_TO_SCREEN,
            JSON.toJSONString(irCameraToScreen)
        )
        initWarpTrans()
    }

    /**
     * 更新摄像头相对于屏幕的投影映射矩阵
     */
    fun updateCameraToScreen(
        srcPointList: MutableList<Point>,
        dstPointList: MutableList<Point>,
        screenSize: Size,
        cameraSize: Size
    ) {
        cameraToScreen = CameraWarpBean()
        srcPointList.forEach {
            cameraToScreen?.srcPointList?.add(mutableListOf(it.x, it.y))
        }
        dstPointList.forEach {
            cameraToScreen?.dstPointList?.add(mutableListOf(it.x, it.y))
        }
        cameraToScreen?.screenSize?.add(screenSize.width)
        cameraToScreen?.screenSize?.add(screenSize.height)

        cameraToScreen?.cameraSize?.add(cameraSize.width)
        cameraToScreen?.cameraSize?.add(cameraSize.height)
        perspectiveMat = null
        LogUtils.d("updateCameraToScreen ${JSON.toJSONString(cameraToScreen)}")
        SharedPreferenceUtils.instance.put(SP_CAMERA_TO_SCREEN, JSON.toJSONString(cameraToScreen))
        initWarpTrans()
    }

    /**
     * 获取摄像头相对于屏幕的投影映射矩阵
     */
    fun loadCameraToScreen(): CameraWarpBean? {
        return cameraToScreen
    }

    fun updateCameraToScreenOffset(x: Double, y: Double, scal: Double) {
        cameraToScreenOffset = CameraOffsetBean()
        cameraToScreenOffset?.x = x
        cameraToScreenOffset?.y = y
        cameraToScreenOffset?.scal = scal
        LogUtils.d("updateCameraToScreenOffset ${JSON.toJSONString(cameraToScreenOffset)}")
        SharedPreferenceUtils.instance.put(
            SP_CAMERA_TO_SCREEN_OFFSET,
            JSON.toJSONString(cameraToScreenOffset)
        )
    }

    fun loadCameraToScreenOffset(): CameraOffsetBean? {
        return cameraToScreenOffset
    }

    fun loadCameraPerspectiveMat(): Mat? {
        return perspectiveMat
    }

    /**
     * 摄像头到屏幕的映射
     */
    private var perspectiveMat: Mat? = null

    /**
     * 红外摄像头到屏幕的映射
     */
    private var irPerspectiveMat: Mat? = null

    /**
     * 屏幕到摄像头的映射
     */
    private var screenToCameraMat: Mat? = null

    /**
     * 点位是否在屏幕内
     */
    fun isPointInScreen(x: Double, y: Double): MutableList<Double>? {
        initWarpTrans()
        var screenPoint = cameraPointToScreen(x, y)
        if (screenPoint.isNullOrEmpty() || screenRect == null) {
            return null
        } else {
            screenRect?.let {
                if (it.contains(screenPoint[0].toInt(), screenPoint[1].toInt())) {
                    return screenPoint
                }
            }
        }
        return null
    }

    /**
     * 摄像头点位转成屏幕点位
     */
    fun cameraPointToScreen(x: Double, y: Double): MutableList<Double>? {
        try {
            cameraPointWarp(x, y)?.let {
                return warpToScreen(it[0], it[1])
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 屏幕点位转成摄像头点位
     */
    fun screenPointToCamera(x: Double, y: Double): MutableList<Double>? {
        try {
            screenPointWarp(x, y)?.let {
                return screenToWarp(it[0], it[1])
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 点进行投影仿射变换
     */
    fun cameraPointWarp(x: Double, y: Double): MutableList<Double>? {
        try {
            initWarpTrans()
            perspectiveMat?.let {
                var dstX = (it[0, 0][0].mul(x) + it[0, 1][0].mul(y) + it[0, 2][0])
                    .divide(it[2, 0][0].mul(x) + it[2, 1][0].mul(y) + it[2, 2][0])
                var dstY = (it[1, 0][0].mul(x) + it[1, 1][0].mul(y) + it[1, 2][0])
                    .divide(it[2, 0][0].mul(x) + it[2, 1][0].mul(y) + it[2, 2][0])
                return mutableListOf(dstX, dstY)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun screenPointWarp(x: Double, y: Double): MutableList<Double>? {
        try {
            initWarpTrans()
            screenToCameraMat?.let {
                var dstX = (it[0, 0][0].mul(x) + it[0, 1][0].mul(y) + it[0, 2][0])
                    .divide(it[2, 0][0].mul(x) + it[2, 1][0].mul(y) + it[2, 2][0])
                var dstY = (it[1, 0][0].mul(x) + it[1, 1][0].mul(y) + it[1, 2][0])
                    .divide(it[2, 0][0].mul(x) + it[2, 1][0].mul(y) + it[2, 2][0])
                return mutableListOf(dstX, dstY)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun irToScreenWarp(x: Double, y: Double): MutableList<Double>? {
        try {
            initWarpTrans()
            irPerspectiveMat?.let {
                var dstX = (it[0, 0][0].mul(x) + it[0, 1][0].mul(y) + it[0, 2][0])
                    .divide(it[2, 0][0].mul(x) + it[2, 1][0].mul(y) + it[2, 2][0])
                var dstY = (it[1, 0][0].mul(x) + it[1, 1][0].mul(y) + it[1, 2][0])
                    .divide(it[2, 0][0].mul(x) + it[2, 1][0].mul(y) + it[2, 2][0])
                return mutableListOf(dstX, dstY)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return null
    }


    /**
     * 投影仿射后的点映射到屏幕上面的点位
     */
    fun warpToScreen(x: Double, y: Double): MutableList<Double>? {
        try {
            cameraToScreenOffset?.let {
                var dstX = x.mul(it.scal).sub(it.x)
                var dstY = y.mul(it.scal).sub(it.y)
                return mutableListOf(dstX, dstY)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 屏幕上面的点位映射到摄像头投影仿射后的点位
     */
    fun screenToWarp(x: Double, y: Double): MutableList<Double>? {
        try {
            cameraToScreenOffset?.let {
                var dstX = (x + it.x).divide(it.scal)
                var dstY = (y + it.y).divide(it.scal)
                return mutableListOf(dstX, dstY)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


    fun screenWarp(mat: Mat): Mat {
        LogUtils.d("screenWarp ${mat.rows()} ${mat.cols()}")
        initWarpTrans()
        perspectiveMat?.let {
//            var testPoint = Point(300.0, 300.0)
//            var startTime = System.currentTimeMillis()
//            var testMat = Converters.vector_Point_to_Mat(mutableListOf(testPoint), CvType.CV_32F)
//            var testDet = Mat()
//            Imgproc.warpPerspective(
//                testMat,
//                testDet,
//                perspectiveMmat,
//                testMat.size()
//            )
//            var testPoints = mutableListOf<Point>()
//            Converters.Mat_to_vector_Point(testDet, testPoints)
//            var endTime = System.currentTimeMillis()
//            var perPoints = mutableListOf<Point>()
//            Converters.Mat_to_vector_Point(it, perPoints)
//            LogUtils.d("放射矩阵 ${JSON.toJSONString(perPoints)}")
//            LogUtils.d("测试耗时 ${endTime - startTime} ${JSON.toJSONString(testPoints)}")
            val dst = Mat()
            Imgproc.warpPerspective(
                mat,
                dst,
                perspectiveMat,
                mat.size()
            )
            return dst
        }
        return mat
    }

    private fun initWarpTrans() {
        if (!isOpenInit) {
            return
        }
        if (perspectiveMat == null) {
            cameraToScreen?.let {
                var srcPoint = mutableListOf<Point>()
                var dstPoint = mutableListOf<Point>()
                it.srcPointList.filter {
                    it.size == 2
                }.forEach {
                    srcPoint.add(Point(it[0], it[1]))
                }
                it.dstPointList.filter {
                    it.size == 2
                }.forEach {
                    dstPoint.add(Point(it[0], it[1]))
                }
                perspectiveMat = Imgproc.getPerspectiveTransform(
                    Converters.vector_Point_to_Mat(srcPoint, CvType.CV_32F),
                    Converters.vector_Point_to_Mat(dstPoint, CvType.CV_32F)
                )
                screenToCameraMat = Imgproc.getPerspectiveTransform(
                    Converters.vector_Point_to_Mat(dstPoint, CvType.CV_32F),
                    Converters.vector_Point_to_Mat(srcPoint, CvType.CV_32F)
                )
            }
        }
        //初始化红外摄像头
        if (irPerspectiveMat == null) {
            irCameraToScreen?.let {
                var srcPoint = mutableListOf<Point>()
                var dstPoint = mutableListOf<Point>()
                it.srcPointList.filter {
                    it.size == 2
                }.forEach {
                    srcPoint.add(Point(it[0], it[1]))
                }
                it.dstPointList.filter {
                    it.size == 2
                }.forEach {
                    dstPoint.add(Point(it[0], it[1]))
                }
                irPerspectiveMat = Imgproc.getPerspectiveTransform(
                    Converters.vector_Point_to_Mat(srcPoint, CvType.CV_32F),
                    Converters.vector_Point_to_Mat(dstPoint, CvType.CV_32F)
                )
            }
        }
        if (screenRect == null) {
            var screenWidth =
                QMUIDisplayHelper.getScreenWidth(ApplicationUtils.instance.getApplication())
            var screenHeight = screenWidth.divide(16.0).mul(9.0).toInt()
            screenRect = Rect(0, 0, screenWidth, screenHeight)
        }
    }

    /**
     * 屏幕进行投影映射
     */
    fun screenWarp(bitmap: Bitmap): Bitmap {
        return screenWarp(bitmap.toMat()).toBitmap()
    }

    /**
     * 经过投影映射后的画面，剪切屏幕范围
     */
    fun screenWarpCut(bitmap: Bitmap): Bitmap {
        try {
            var screenPoint = mutableListOf<Point>()
            var screenWidth =
                QMUIDisplayHelper.getRealScreenSize(ApplicationUtils.instance.getApplication())[0]
            var screenHeight =
                QMUIDisplayHelper.getRealScreenSize(ApplicationUtils.instance.getApplication())[1]
            screenToWarp(0.0, 0.0)?.let {
                screenPoint.add(Point(it[0], it[1]))
            }
            screenToWarp(screenWidth.toDouble(), 0.0)?.let {
                screenPoint.add(Point(it[0], it[1]))
            }
            screenToWarp(
                screenWidth.toDouble(),
                screenHeight.toDouble()
            )?.let {
                screenPoint.add(Point(it[0], it[1]))
            }
            screenToWarp(0.0, screenHeight.toDouble())?.let {
                screenPoint.add(Point(it[0], it[1]))
            }
            var width = screenPoint[0].distance(screenPoint[1])
            var height = screenPoint[3].distance(screenPoint[0])
            return Bitmap.createBitmap(
                bitmap,
                screenPoint[0].x.toInt(),
                screenPoint[1].y.toInt(),
                width.toInt(),
                height.toInt()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return bitmap
        }
    }
}
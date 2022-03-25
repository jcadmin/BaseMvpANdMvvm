package com.micro.camera.warp

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Rect
import android.util.Size
import androidx.annotation.Keep
import com.alibaba.fastjson.JSON
import com.apkfuns.logutils.LogUtils
import com.joye.jiang.common.sdk.*
import com.micro.opencv.utils.distance
import com.micro.opencv.utils.toBitmap
import com.micro.opencv.utils.toMat
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import io.reactivex.Observable
import org.opencv.android.OpenCVLoader
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.imgproc.Imgproc
import org.opencv.utils.Converters

@Keep
class CameraWarpManager private constructor() {

    /**
     * opencv是否初始化成功
     */
    private var isOpencvInitSuccess = false

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
     * 屏幕在摄像头的里面的矩形
     */
    private var screenRect: Rect? = null

    @Keep
    companion object {
        @JvmStatic
        val instance = SingletonHolder.holder

        private const val SP_CAMERA_TO_SCREEN = "sp_camera_to_screen"

        private const val SP_IR_CAMERA_TO_SCREEN = "sp_ir_camera_to_screen"

        private const val SP_CAMERA_TO_SCREEN_OFFSET = "sp_camera_to_screen_offset"
    }

    init {
        isOpencvInitSuccess = OpenCVLoader.initDebug()
    }

    private object SingletonHolder {
        val holder = CameraWarpManager()
    }

    @SuppressLint("CheckResult")
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
                LogUtils.d("initWarpTrans start")
                initWarpTrans()
                LogUtils.d("initWarpTrans end")
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
        initLoad()
        initWarpTrans()
    }

    /**
     * 更新摄像头与光机的偏移量
     */
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
        initLoad()
        initWarpTrans()
    }

    /**
     * 摄像头点位是否在屏幕内
     */
    fun isPointInScreen(warpBuilder: WarpBuilder): MutableList<Double>? {
        var screenPoint = cameraPointToScreen(warpBuilder)
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
    fun cameraPointToScreen(warpBuilder: WarpBuilder): MutableList<Double>? {
        try {
            cameraPointWarp(warpBuilder)?.let {
                return cameraWarpToScreen(it[0], it[1])
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 摄像头点位进行投影仿射
     */
    fun cameraPointWarp(warpBuilder: WarpBuilder): MutableList<Double>? {
        try {
            initWarpTrans()
            var warpBean = warpBuilder.build()
            var scale = getScale(warpBean)
            var x = warpBean.x.mul(scale)
            var y = warpBean.y.mul(scale)
            perspectiveMat?.let {
                var dstX = (it[0, 0][0].mul(x) + it[0, 1][0].mul(y) + it[0, 2][0])
                    .divide(it[2, 0][0].mul(x) + it[2, 1][0].mul(y) + it[2, 2][0])
                var dstY = (it[1, 0][0].mul(x) + it[1, 1][0].mul(y) + it[1, 2][0])
                    .divide(it[2, 0][0].mul(x) + it[2, 1][0].mul(y) + it[2, 2][0])
                return mutableListOf(dstX, dstY)
            }
            if (warpBean.compatible) {
                return mutableListOf(warpBean.x, warpBean.y)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 投影仿射后的点映射到屏幕上面的点位
     */
    fun cameraWarpToScreen(x: Double, y: Double): MutableList<Double>? {
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
     *摄像头转换成矫正后的图片
     */
    fun cameraToWarp(bitmap: Bitmap): Bitmap {
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


    private fun screenWarp(mat: Mat): Mat {
        initWarpTrans()
        perspectiveMat?.let {
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

    /**
     * 获取当前图像和初始化图像的缩放比
     */
    private fun getScale(warpBean: WarpBean): Double {
        var scale = 1.0
        cameraToScreen?.cameraSize?.let {
            scale = it[0].divide(warpBean.imageWidth.toDouble())
        }
        return scale
    }

    private fun initWarpTrans() {
        if (!isOpencvInitSuccess) {
            LogUtils.e("opencv初始化失败")
            return
        }
        if (cameraToScreen == null) {
            initLoad()
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

}
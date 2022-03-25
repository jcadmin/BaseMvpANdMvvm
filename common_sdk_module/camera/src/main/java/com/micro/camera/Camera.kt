package com.micro.camera

import android.app.Service
import android.content.Context
import android.graphics.Bitmap
import android.graphics.SurfaceTexture
import android.util.AttributeSet
import android.util.Log
import android.util.Size
import android.view.*
import android.widget.LinearLayout
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.Executor

@Deprecated("")
class Camera : LinearLayout {

    private lateinit var mContext: Context

//    private var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>? = null

    private var mRootView: View? = null

    private var viewFinder: PreviewView? = null

    private var mImageCapture: ImageCapture? = null

    var cameraListener: CameraAnalyzer.CameraListener? = null

    private var executor: Executor? = null

    private var mCameraControl: CameraControl? = null

    private var mCameraInfo: CameraInfo? = null

    private var cameraSelector: CameraSelector? = null

    private var cameraProvider: ProcessCameraProvider? = null

    private var previewSize = Size(3264, 2448)
//    private var previewSize = Size(1280, 960)

    private var isBack = true

    private var cameraId = "0"

    private var preview: Preview? = null

    /**
     * 是否正在预览
     */
    private var isPreview = false

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView()
    }

    fun startPreview(context: Context) {
        this.mContext = context
        executor = ContextCompat.getMainExecutor(mContext);
        var cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            try {
                //相机的生命周期和activity的绑定
                cameraProvider = cameraProviderFuture.get()
                initImageCapture()
                bindPreview(cameraProvider!!)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, executor!!)
    }

    fun changeCamera(back: Boolean = !isBack) {
        if (isBack != back) {
            cameraId =
                if (back) CameraSelector.LENS_FACING_BACK.toString() else CameraSelector.LENS_FACING_FRONT.toString()
            isBack = back
            startPreview(mContext!!)
        }
    }

    fun setCameraSize(width: Int, height: Int) {
        previewSize = Size(width, height)
        if (isPreview) {
            startPreview(mContext!!)
        }
    }

    fun changeNext() {
        for (index in 0 until CameraManager.instance.cameraIdList().size) {
            if (cameraId == CameraManager.instance.cameraIdList()[index]) {
                if (index == CameraManager.instance.cameraIdList().size - 1) {
                    cameraId = CameraManager.instance.cameraIdList().first()
                } else {
                    cameraId = CameraManager.instance.cameraIdList()[index + 1]
                }
            }
        }
        startPreview(mContext!!)
    }


    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        //预览的capture
        preview = Preview.Builder().apply {
            viewFinder?.display?.let {
                setTargetRotation(Surface.ROTATION_0)

            }
            setTargetResolution(previewSize)
        }.build()

        //解绑
        cameraProvider.unbindAll()

        var imageAnalyzer = ImageAnalysis.Builder()
            // 仅将最新图像传送到分析仪，并在到达图像时将其丢弃。
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setTargetResolution(previewSize)
            .setTargetRotation(Surface.ROTATION_0)
            .build()
            .also {
                it.setAnalyzer(
                    executor!!,
                    CameraAnalyzer(context, object : CameraAnalyzer.CameraListener {
                        override fun preview(bitmap: Bitmap?) {
                            cameraListener?.preview(bitmap)
                        }
                    })
                )
            }
//        if (cameraSelector == null) {
//            cameraId = CameraManager.instance.cameraIdList().firstOrNull()
//                ?: CameraSelector.LENS_FACING_BACK.toString()
//        }
        cameraSelector = CameraSelector.Builder()
            .addCameraFilter(MicroCameraFilter(cameraId)).build()
        cameraProvider.unbindAll()
        if (mContext is LifecycleOwner) {
            var camera = cameraProvider.bindToLifecycle(
                mContext as LifecycleOwner,
                cameraSelector!!, preview, mImageCapture, imageAnalyzer
            )
            viewFinder?.let {
                preview?.setSurfaceProvider(it.surfaceProvider)
            }
            mCameraInfo = camera.cameraInfo
            mCameraControl = camera.cameraControl
        }
        isPreview = true
    }

    private fun initView() {
        CameraManager.instance.init(context)
        if (context is Service) {
            return
        }
        mRootView = LayoutInflater.from(context).inflate(R.layout.view_camera, this)
        viewFinder = mRootView!!.findViewById(R.id.view_finder)
    }

    private fun initImageCapture() {
        //创建图片的capture
        mImageCapture = ImageCapture.Builder()
            .setFlashMode(ImageCapture.FLASH_MODE_AUTO)
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
        var orientationEventListener = object : OrientationEventListener(mContext) {
            override fun onOrientationChanged(orientation: Int) {
                var rotaion = Surface.ROTATION_0
//                if (orientation in 45..134) {
//                    rotaion = Surface.ROTATION_270
//                } else if (orientation in 135..224) {
//                    rotaion = Surface.ROTATION_180
//                } else if (orientation in 225..315) {
//                    rotaion = Surface.ROTATION_90
//                } else {
//                    rotaion = Surface.ROTATION_0
//                }
                Log.d("result", "orientation is :${orientation}")
                mImageCapture?.targetRotation = rotaion
            }
        }
        orientationEventListener.enable()
    }


}
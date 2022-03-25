package com.micro.camera

import android.content.Intent
import android.hardware.camera2.params.StreamConfigurationMap
import android.util.Log
import android.util.Size
import android.view.Surface
import androidx.camera.core.CameraInfo
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleService
import com.apkfuns.logutils.LogUtils
import com.hwangjr.rxbus.RxBus
import java.util.concurrent.Executor

open abstract class BaseCameraService : LifecycleService() {

    companion object {
        const val CAMERA_ID = "camera_id"
    }

    protected val TAG = this::class.java.simpleName

    protected var cameraSelector: CameraSelector? = null

    protected var executor: Executor? = null

    protected var cameraProvider: ProcessCameraProvider? = null

    protected var cameraId: String? = null

    protected var imageAnalyzer: ImageAnalysis? = null

    protected var mCameraInfo: CameraInfo? = null

    open protected fun initSize(): Size {
        return Size(1080, 1440)
    }

    /**
     * 初始化相机id
     */
    protected abstract fun initCameraId(): Int

    /**
     * 初始化图像分析
     */
    protected abstract fun initCameraAnalyzer(): ImageAnalysis.Analyzer


    override fun onCreate() {
        super.onCreate()
        LogUtils.d("${TAG} onCreate")
        CameraManager.instance.init(this)
        RxBus.get().register(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val cameraId = intent?.getStringExtra(CAMERA_ID)?.toString()
        if (!cameraId.isNullOrEmpty()) {
            this.cameraId = cameraId
        }
        initCamera()
        startCamera()
        return super.onStartCommand(intent, flags, startId)
    }

    protected fun changeCameraId(cameraId: String) {
        Log.d(TAG, "changeCameraId: ${cameraId}")
        if (!CameraManager.instance.cameraIdList().contains(cameraId)) {
            return
        }
//        ForegroundCameraManager.instance.currentCameraId = cameraId.toIntOrNull() ?: 0
        this.cameraId = cameraId
        cameraSelector = CameraSelector.Builder()
            .addCameraFilter(MicroCameraFilter(cameraId)).build()
//        imageAnalyzer?.let {
//            cameraProvider?.unbind(it)
//        }
        startCamera()
    }

    fun changePreviewSize(size: Size) {
        initAnalyzer(size)
        startCamera()
    }

    private fun initCamera() {
        executor = ContextCompat.getMainExecutor(this);
        initAnalyzer()
        changeCameraId(cameraId ?: initCameraId().toString())
    }

    /**
     * 初始化图像分析器
     */
    private fun initAnalyzer(size: Size = initSize()) {
        imageAnalyzer = ImageAnalysis.Builder()
            // 仅将最新图像传送到分析仪，并在到达图像时将其丢弃。
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setTargetResolution(size)
            .setTargetRotation(Surface.ROTATION_0)
            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
            .build()
            .also {
                it.setAnalyzer(
                    executor!!,
                    initCameraAnalyzer()
                )
            }

    }

    protected fun startCamera() {
        var cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            try {
                //相机的生命周期和activity的绑定
                cameraProvider = cameraProviderFuture.get()
//                cameraProvider?.unbindAll()
                var camera = cameraProvider?.bindToLifecycle(
                    this,
                    cameraSelector!!, imageAnalyzer!!
                )
                mCameraInfo = camera?.cameraInfo
                mCameraInfo?.cameraState?.observe(this) {
                    if (it.error != null) {
                        Log.e(TAG, "startCamera: ${it.error?.cause?.cause.toString()}")
                    }
                    Log.d(TAG, "startCamera: camera state ${it.type}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, executor!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        RxBus.get().unregister(this)
    }
}
package com.micro.camera

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import androidx.annotation.GuardedBy
import androidx.annotation.Keep
import com.apkfuns.logutils.LogUtils
import com.joye.jiang.common.sdk.extension.thread2Io
import com.micro.camera.hands.FingerTipHandsResult
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

@Keep
class MLCameraManage private constructor() {

    @GuardedBy("backLock")
    private var backCameraListener: HashMap<Any, CameraCallbacks> = hashMapOf()

    @GuardedBy("backExtraLock")
    private var backExtraCameraListener: HashMap<Any, CameraCallbacks> = hashMapOf()

    private var backLock = Any()

    private var backExtraLock = Any()

    @Keep
    companion object {
        @Keep
        @JvmStatic
        val instance = SingletonHolder.holder

        private var TAG = this::class.java.simpleName
    }

    private object SingletonHolder {
        val holder = MLCameraManage()
    }

    /**
     * 开启后台相机
     */
    fun startBackCameraService(context: Context, cameraId: String? = null) {
        var intent = Intent(context, BackCameraService::class.java)
        cameraId?.let {
            intent.putExtra(BaseCameraService.CAMERA_ID, cameraId)
        }
        context.startService(intent)
        MLHandsManager.instance.init(context)
    }

    /**
     * 开启后台相机
     */
    fun startBackExtraCameraService(context: Context, cameraId: String? = null) {
        var intent = Intent(context, BackExtraCameraService::class.java)
        cameraId?.let {
            intent.putExtra(BaseCameraService.CAMERA_ID, cameraId)
        }
        context.startService(intent)
    }

    /**
     * 修改相机id
     */
    @Keep
    fun changeBackCameraId(context: Context, cameraId: String) {
        stopBackCameraService(context)
        startBackCameraService(context, cameraId)
    }

    /**
     * 停止后台相机
     */
    fun stopBackCameraService(context: Context) {
        var intent = Intent(context, BackCameraService::class.java)
        context.stopService(intent)
        MLHandsManager.instance.dispose()
    }

    /**
     * 停止后置附属相机
     */
    fun stopBackExtraCameraService(context: Context) {
        var intent = Intent(context, BackExtraCameraService::class.java)
        context.stopService(intent)
    }

    /**
     * 修改相机id
     */
    @Keep
    fun changeBackExtraCameraId(context: Context, cameraId: String) {
        stopBackExtraCameraService(context)
        startBackExtraCameraService(context, cameraId)
    }

    /**
     * 发送实时预览数据
     */
    @Keep
    @SuppressLint("CheckResult")
    fun postBackCamera(bitmap: Bitmap) {
        synchronized(backLock) {
            Observable.fromIterable(backCameraListener.values)
                .subscribeOn(Schedulers.io())
                .thread2Io()
                .subscribe({
                    it.builder.preview?.invoke(bitmap)
                }, {
                    LogUtils.e(it)
                })
        }
    }

    /**
     * 发送后置附属实时预览数据
     */
    @Keep
    @SuppressLint("CheckResult")
    fun postBackExtraCamera(bitmap: Bitmap) {
        synchronized(backExtraLock) {
            Observable.fromIterable(backExtraCameraListener.values)
                .subscribeOn(Schedulers.io())
                .thread2Io()
                .subscribe({
                    it.builder.preview?.invoke(bitmap)
                }, {
                    LogUtils.e(it)
                })
        }
    }

    /**
     * 发送食指位置数据
     */
    fun postFingerResult(result: FingerTipHandsResult) {
        synchronized(backLock) {
            Observable.fromIterable(backCameraListener.values)
                .subscribeOn(Schedulers.io())
                .thread2Io()
                .subscribe({
                    it.builder.fingerTip?.invoke(result)
                }, {
                    LogUtils.e(it)
                })
        }
    }

    /**
     * 发送没有手指数据
     */
    fun postNoFinger(bitmap: Bitmap) {
        synchronized(backLock) {
            Observable.fromIterable(backCameraListener.values)
                .subscribeOn(Schedulers.io())
                .thread2Io()
                .subscribe({
                    it.builder.noFingers?.invoke(bitmap)
                }, {
                    LogUtils.e(it)
                })
        }
    }

    /**
     * 监听相机
     */
    @Keep
    fun registerBackCamera(tag: Any, build: CameraCallbacks.Builder.() -> Unit) {
        try {
            synchronized(backLock) {
                backCameraListener[tag] = CameraCallbacks().apply { registerListener(build) }
            }
        } catch (e: Exception) {
            Log.e(TAG, "registerCamera:", e)
        }
    }

    /**
     * 监听相机
     */
    @Keep
    fun registerBackExtraCamera(tag: Any, build: CameraCallbacks.Builder.() -> Unit) {
        try {
            synchronized(backExtraLock) {
                backExtraCameraListener[tag] = CameraCallbacks().apply { registerListener(build) }
            }
        } catch (e: Exception) {
            Log.e(TAG, "registerCamera:", e)
        }
    }

    /**
     * 注销监听相机
     */
    @Keep
    fun unregisterBackCamera(tag: Any) {
        try {
            synchronized(backLock) {
                backCameraListener.remove(tag)
            }
        } catch (e: Exception) {
            LogUtils.e(e)
        }
    }

    /**
     * 注销监听相机
     */
    @Keep
    fun unregisterBackExtraCamera(tag: Any) {
        try {
            synchronized(backLock) {
                backExtraCameraListener.remove(tag)
            }
        } catch (e: Exception) {
            LogUtils.e(e)
        }
    }
}
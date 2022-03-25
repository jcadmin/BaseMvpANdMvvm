package com.micro.camera

import android.graphics.Bitmap
import android.util.Log
import androidx.annotation.GuardedBy
import com.apkfuns.logutils.LogUtils
import com.google.mediapipe.solutions.hands.HandLandmark
import com.google.mediapipe.solutions.hands.Hands
import com.google.mediapipe.solutions.hands.HandsResult
import com.micro.camera.hands.FingerTipHandsResult
import com.micro.camera.warp.CameraWarpManager
import com.micro.camera.warp.WarpBuilder
import kotlin.concurrent.thread

class MediaPipeHands constructor(
    var hans: Hands
) {
    private val TAG = this::class.java.simpleName

    private var currentBitmap: Bitmap? = null

    private var handsCallbacks: CameraCallbacks? = null

    companion object {
        /**
         * 空闲
         */
        const val STATUS_IDLE = 1

        /**
         * 识别中
         */
        const val STATUS_PROGRESSING = 2

    }

    private var bufferSize = 3

    @GuardedBy("lock")
    private var bitmapBuffer = arrayListOf<Bitmap>()

    private val lock = Any()

    init {
        hans.setResultListener {
            success(it)
        }
        hans.setErrorListener { message, e ->
            error(message, e)
        }
    }

    @GuardedBy("lock")
    private var status = STATUS_IDLE

    fun isIdle(): Boolean {
        return status == STATUS_IDLE
    }

    fun registerCallback(builder: CameraCallbacks.Builder.() -> Unit) =
        apply { handsCallbacks = CameraCallbacks().apply { registerListener(builder) } }

    fun send(bitmap: Bitmap) {
        synchronized(lock) {
//            Log.d(TAG, "send: 当前状态:${status}")
            when (status) {
                STATUS_IDLE -> {
                    status = STATUS_PROGRESSING
//                    Log.d(TAG, "send: 发送图片到手势识别")
                    hans.send(bitmap, System.currentTimeMillis())
                    currentBitmap = bitmap.copy(bitmap.config, true)
                }
                STATUS_PROGRESSING -> {
                    if (bitmapBuffer.size >= bufferSize) {
//                        Log.d(TAG, "send: 图片缓存达到最大值(${bufferSize})，清空缓存")
                        bitmapBuffer.clear()
                    }
                    bitmapBuffer.add(bitmap)
                }
                else -> {

                }
            }
        }

    }

    private fun success(hansResult: HandsResult) {
        status = STATUS_IDLE
        checkHandsResult(hansResult)
        sendLastBitmap()
    }

    private fun error(message: String, e: RuntimeException) {
        status = STATUS_IDLE
        sendLastBitmap()
        Log.e(TAG, "GoogleHand: ${message}", e)
    }

    private fun sendLastBitmap() {
        thread {
            synchronized(lock) {
                var lastBitmap = bitmapBuffer.lastOrNull()
                bitmapBuffer.clear()
                if (lastBitmap == null) {
//                    Log.e(TAG, "sendLastBitmap: 图片缓存为空")
                    return@synchronized
                }
//                Log.d(TAG, "sendLastBitmap: 当前状态:${status}")
                when (status) {
                    STATUS_IDLE -> {
                        status = STATUS_PROGRESSING
//                        Log.d(TAG, "sendLastBitmap: 发送图片到手势识别")
                        hans.send(lastBitmap, System.currentTimeMillis())
                        currentBitmap = lastBitmap.copy(lastBitmap.config, true)
                    }
                    STATUS_PROGRESSING -> {

                    }
                    else -> {

                    }
                }
            }
        }
    }

    private fun checkHandsResult(handsresult: HandsResult) {
//        LogUtils.d(
//            "GoogleHand: 手的数量: ${handsresult.multiHandLandmarks().size} 检测耗时:${System.currentTimeMillis() - handsresult.timestamp()}ms"
//        )

        if (handsresult.multiHandLandmarks().size > 0) {
            val wristLandmark =
                Hands.getHandLandmark(handsresult, 0, HandLandmark.INDEX_FINGER_TIP)
            val width: Int = handsresult.inputBitmap().getWidth()
            val height: Int = handsresult.inputBitmap().getHeight()
//            Log.d(
//                TAG, "${
//                    String.format(
//                        "MediaPipe Hand wrist coordinates (pixel values): x=%f, y=%f",
//                        wristLandmark.x * width, wristLandmark.y * height
//                    )
//                } "
//            )

            var startTime = System.currentTimeMillis()
            var point = CameraWarpManager.instance.isPointInScreen(
                WarpBuilder().x((wristLandmark.x * width).toDouble())
                    .y((wristLandmark.y * height).toDouble())
                    .imageWidth(width)
                    .imageHeight(height)
                    .compatible(false)
            )
            LogUtils.d("手指是否在投影屏幕内:${!point.isNullOrEmpty()} 耗时:${System.currentTimeMillis() - startTime}ms")
            //如果手指在屏幕内
            if (!point.isNullOrEmpty()) {
                handsCallbacks?.builder?.fingerTip?.invoke(
                    FingerTipHandsResult(
                        currentBitmap ?: handsresult.inputBitmap(),
                        wristLandmark.x,
                        wristLandmark.y,
                        handsresult.timestamp()
                    )
                )
//                ForegroundCameraManager.instance.postHandKeyResult(handsresult)
//                ForegroundCameraManager.instance.postHandPoint(
//                    wristLandmark.x * width, wristLandmark.y * height
//                )
            } else {//如果手指不在屏幕内
                handsCallbacks?.builder?.noFingers?.invoke(
                    currentBitmap ?: handsresult.inputBitmap()
                )
            }
        } else {//如果不存在手指
//            ForegroundCameraManager.instance.postHandKeyResult(handsresult)
            handsCallbacks?.builder?.noFingers?.invoke(currentBitmap ?: handsresult.inputBitmap())
        }
    }
}
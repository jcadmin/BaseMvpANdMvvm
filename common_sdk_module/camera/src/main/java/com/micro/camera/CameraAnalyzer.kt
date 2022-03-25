package com.micro.camera

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.opengl.GLUtils
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.hwangjr.rxbus.RxBus
import io.reactivex.Observable
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.nio.ByteBuffer
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class CameraAnalyzer(private var context: Context, private var listener: CameraListener?) :
    ImageAnalysis.Analyzer {
    private lateinit var buffBitmap: Bitmap

    private var lastFpsTimestamp = System.currentTimeMillis()
    private var frameCounter = 0

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(image: ImageProxy) {
        Observable.just(image)
            .map {
                val buffer = image.planes[0].buffer
//        val data = ByteArray(buffer.remaining())
//        buffer.get(data)
                var startTime = System.currentTimeMillis()
                val height = image.height
                val width = image.width
                listener?.let {
                    if (!::buffBitmap.isInitialized || buffBitmap.isRecycled) {
                        buffBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                    }
                    buffBitmap.copyPixelsFromBuffer(buffer)
                    var bitmapTime = System.currentTimeMillis()
                    it.preview(buffBitmap)
//                    Log.d(
//                        "CameraAnalyzer",
//                        "analyze: format: ${image.format} rotation:${image.imageInfo.rotationDegrees} time :${System.currentTimeMillis() - startTime}ms bitmapTime:${bitmapTime - startTime}ms preview time :${System.currentTimeMillis() - bitmapTime}ms"
//                    )
                }
                val frameCount = 10
                if (++frameCounter % frameCount == 0) {
                    frameCounter = 0
                    val now = System.currentTimeMillis()
                    val delta = now - lastFpsTimestamp
                    val fps = 1000 * frameCount.toFloat() / delta
                    Log.d(
                        "CameraAnalyzer",
                        "FPS: ${"%.02f".format(fps)} with tensorSize: ${width} x ${height}"
                    )
                    lastFpsTimestamp = now
                }
                it
            }
            .timeout(1, TimeUnit.SECONDS)
            .doFinally {
                image.close()
            }
            .blockingSubscribe({

            }, {
                it.printStackTrace()
            })
//        try {
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//        } finally {
//            image.close()
//        }
    }

    private fun adjustPhotoRotation(bm: Bitmap, orientationDegree: Float): Bitmap? {
        val m = Matrix()
        m.setRotate(orientationDegree, bm.width.toFloat() / 2, bm.height.toFloat() / 2)

        try {
            return Bitmap.createBitmap(bm, 0, 0, bm.width, bm.height, m, true)
        } catch (ex: OutOfMemoryError) {
        }
        return null
    }

    interface CameraListener {

        fun preview(bitmap: Bitmap?)

//        fun preview(
//            byteBuffer: ByteBuffer,
//            format: Int,
//            width: Int,
//            height: Int,
//            timestamp: Long
//        ): Boolean
    }
}
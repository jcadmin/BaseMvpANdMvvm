package com.micro.camera.hands

import android.graphics.Bitmap
import androidx.annotation.Keep
import com.joye.jiang.common.sdk.mul
import com.micro.camera.warp.CameraWarpManager
import com.micro.camera.warp.WarpBuilder

/**
 * 食指指尖结果
 */
@Keep
class FingerTipHandsResult {
    constructor()

    constructor(bitmap: Bitmap?, xPer: Float, yPer: Float, time: Long) {
        this.bitmap = bitmap
        this.xPer = xPer
        this.yPer = yPer
        this.time = time
    }

    var bitmap: Bitmap? = null

    /**
     * 百分比
     */
    var xPer: Float = 0f

    /**
     * 百分比
     */
    var yPer: Float = 0f

    /**
     * 时间
     */
    var time: Long = 0L

    fun screenPoint(): MutableList<Double>? {
        if (bitmap != null) {
            bitmap?.let {
                return CameraWarpManager.instance.cameraPointToScreen(
                    WarpBuilder()
                        .x(xPer.toDouble().mul(it.width.toDouble()))
                        .y(yPer.toDouble().mul(it.height.toDouble()))
                        .imageWidth(it.width)
                        .imageHeight(it.height)
                )
            }
        }
        return null
    }
}
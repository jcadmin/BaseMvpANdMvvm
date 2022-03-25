package com.micro.camera

import android.graphics.Bitmap
import androidx.annotation.Keep
import com.micro.camera.hands.FingerTipHandsResult

@Keep
class CameraCallbacks {

    lateinit var builder: Builder

    // 带Builder返回值的lambda
    fun registerListener(builder: Builder.() -> Unit) {
        this.builder = Builder().also(builder)
    }

    @Keep
    inner class Builder {
        /**
         * 发现手指
         */
        internal var fingerTip: ((FingerTipHandsResult) -> Unit)? = null

        /**
         * 没有手指
         */
        internal var noFingers: ((Bitmap) -> Unit)? = null

        /**
         * 实时图片预览
         */
        internal var preview: ((Bitmap) -> Unit)? = null

        fun fingerTip(action: (FingerTipHandsResult) -> Unit) {
            fingerTip = action
        }

        fun noFingers(action: (bitmap: Bitmap) -> Unit) {
            noFingers = action
        }

        fun preview(action: (bitmap: Bitmap) -> Unit) {
            preview = action
        }
    }
}
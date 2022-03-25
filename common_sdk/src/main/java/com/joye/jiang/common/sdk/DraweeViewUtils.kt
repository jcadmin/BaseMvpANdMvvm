package com.joye.jiang.common.sdk

import android.graphics.drawable.Animatable
import android.net.Uri
import android.util.Log
import androidx.annotation.Keep
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.fresco.animation.drawable.AnimatedDrawable2
import com.facebook.fresco.animation.drawable.AnimationListener
import com.facebook.imagepipeline.image.ImageInfo
import com.facebook.imagepipeline.request.ImageRequestBuilder

@Keep
object DraweeViewUtils {

    fun load(
        resId: Int, simpleDraweeView: SimpleDraweeView,
        loopCount: Int,
        animationListener: AnimationListener? = null
    ) {
        var imageRequestBuilder = ImageRequestBuilder.newBuilderWithResourceId(resId)
        buildController(simpleDraweeView, imageRequestBuilder, loopCount, animationListener)
    }

    fun load(
        url: String, simpleDraweeView: SimpleDraweeView,
        loopCount: Int,
        animationListener: AnimationListener? = null
    ) {
        var imageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
        buildController(simpleDraweeView, imageRequestBuilder, loopCount, animationListener)
    }

    private fun buildController(
        simpleDraweeView: SimpleDraweeView,
        imageRequestBuilder: ImageRequestBuilder,
        loopCount: Int,
        animationListener: AnimationListener? = null
    ) {
        var builder = Fresco.newDraweeControllerBuilder()
        builder.imageRequest = imageRequestBuilder.build()
        builder.autoPlayAnimations = true
        builder.controllerListener = object : BaseControllerListener<ImageInfo>() {
            override fun onFinalImageSet(
                id: String?,
                imageInfo: ImageInfo?,
                animatable: Animatable?
            ) {
                super.onFinalImageSet(id, imageInfo, animatable)
                if (animatable is AnimatedDrawable2) {
                    animatable.animationBackend =
                        LoopCountModifyingBackend(animatable.animationBackend, loopCount)
                    animatable.setAnimationListener(animationListener)
                }
            }

        }
        simpleDraweeView.controller = builder.build()
    }
}
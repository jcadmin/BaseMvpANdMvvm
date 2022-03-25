package com.joye.jiang.common.sdk

import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.fresco.animation.drawable.AnimationListener

fun SimpleDraweeView.load(
    resId: Int, loopCount: Int = 0,
    animationListener: AnimationListener? = null
) {
    DraweeViewUtils.load(resId, this, loopCount, animationListener)
}

fun SimpleDraweeView.load(
    url: String, loopCount: Int = 0,
    animationListener: AnimationListener? = null
) {
    DraweeViewUtils.load(url, this, loopCount, animationListener)
}
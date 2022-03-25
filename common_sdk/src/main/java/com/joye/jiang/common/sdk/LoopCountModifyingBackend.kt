package com.joye.jiang.common.sdk

import com.facebook.fresco.animation.backend.AnimationBackend
import com.facebook.fresco.animation.backend.AnimationBackendDelegate

class LoopCountModifyingBackend constructor(
    animationBackend: AnimationBackend?,
    var mLoopCount: Int
) :
    AnimationBackendDelegate<AnimationBackend>(animationBackend) {


    override fun getLoopCount(): Int {
        return mLoopCount
    }
}
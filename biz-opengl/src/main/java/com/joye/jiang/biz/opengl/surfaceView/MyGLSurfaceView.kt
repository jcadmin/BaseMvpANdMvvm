package com.joye.jiang.biz.opengl.surfaceView

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet

class MyGLSurfaceView : GLSurfaceView {

    var mGLRender: MyGLRender? = null
        private set

    var mNativeRender: MyNativeRender? = null
        private set

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        this.setEGLContextClientVersion(3)
        mNativeRender = MyNativeRender()
        mGLRender = MyGLRender(mNativeRender)
        setRenderer(mGLRender)
        renderMode = RENDERMODE_WHEN_DIRTY
    }

}
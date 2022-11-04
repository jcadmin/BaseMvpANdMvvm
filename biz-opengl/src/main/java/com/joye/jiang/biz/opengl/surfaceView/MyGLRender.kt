package com.joye.jiang.biz.opengl.surfaceView

import android.opengl.GLSurfaceView
import com.alibaba.fastjson.JSON
import com.joey.jiang.common.router.provider.LogService
import com.therouter.TheRouter
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyGLRender(var myNativeRender: MyNativeRender?) : GLSurfaceView.Renderer {

    private val TAG = this::class.java.simpleName

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        TheRouter.get(LogService::class.java)
            ?.trackLog("onSurfaceCreated() called with:gl =${p0} , config = ${JSON.toJSONString(p1)}")
        myNativeRender?.native_OnSurfaceCreated()
    }

    override fun onSurfaceChanged(p0: GL10?, p1: Int, p2: Int) {
        TheRouter.get(LogService::class.java)
            ?.trackLog("onSurfaceChanged() called with:gl =${p0} , width = ${p1} ,height = ${p2}")
        myNativeRender?.native_OnSurfaceChanged(p1, p2)
    }

    override fun onDrawFrame(p0: GL10?) {
        TheRouter.get(LogService::class.java)?.trackLog("onDrawFrame() called with :gl =${p0}")
        myNativeRender?.native_OnDrawFrame()
    }

}
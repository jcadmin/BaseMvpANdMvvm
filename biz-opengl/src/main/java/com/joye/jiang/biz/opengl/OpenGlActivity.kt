package com.joye.jiang.biz.opengl

import com.alibaba.android.arouter.facade.annotation.Route
import com.joey.jiang.common.router.router.OpenGLRouterConstants
import com.joye.jiang.biz.opengl.databinding.OpenglActivityOpenglBinding
import com.joye.jiang.common.base.activity.BaseActivity

@Route(path = OpenGLRouterConstants.ACTIVITY_OPENGL)
class OpenGlActivity : BaseActivity<OpenglActivityOpenglBinding>() {

    override fun initLayoutId(): Int {
        return R.layout.opengl_activity_opengl
    }

    override fun initViews() {

    }

    override fun initData() {

    }
}
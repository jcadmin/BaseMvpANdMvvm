package com.joye.jiang.mybasemvp

import com.joye.jiang.common.base.BasePresenter
import com.joye.jiang.common.base.BaseView
import com.joye.jiang.common.base.activity.MVPActivity
import com.joye.jiang.common.sdk.RouterUtil
import com.joye.jiang.mybasemvp.databinding.ActivitySplashBinding

class SplashActivity : MVPActivity<ActivitySplashBinding>() {
    /**
     * 初始化presenter
     */
    override fun initPresenter(): MutableList<BasePresenter<BaseView>> {
        return mutableListOf()
    }

    /**
     * 初始化布局
     */
    override fun initLayoutId(): Int {
        return R.layout.activity_splash
    }

    /**
     * 初始化控件
     */
    override fun initViews() {

    }

    /**
     * 初始化数据
     */
    override fun initData() {

    }
}
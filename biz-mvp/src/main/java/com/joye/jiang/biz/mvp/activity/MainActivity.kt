package com.joye.jiang.biz.mvp.activity

import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.joye.jiang.common.base.activity.BaseActivity
import com.joye.jiang.common.base.databinding.ActivityBaseBinding
import com.joey.jiang.common.router.router.MvpRouterConstants
import com.joey.jiang.common.router.router.MvvmRouterConstants
import com.joye.jiang.common.sdk.RouterUtil

@Route(path = MvpRouterConstants.ACTIVITY_MAIN)
class MainActivity : BaseActivity<ActivityBaseBinding>() {

    override fun initMainFragment(): Fragment? {
        return RouterUtil.build(MvvmRouterConstants.FRAGMENT_MAIN).launch() as Fragment
    }

    override fun initViews() {

    }

    override fun initData() {

    }
}
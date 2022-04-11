package com.vigiot.biz.mvvm.activity

import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.joye.jiang.common.base.activity.BaseActivity
import com.joye.jiang.common.base.databinding.ActivityBaseBinding
import com.joye.jiang.common.data.router.MvvmRouterConstants
import com.joye.jiang.common.sdk.RouterUtil

@Route(path = MvvmRouterConstants.ACTIVITY_MAIN)
class MainActivity : BaseActivity<ActivityBaseBinding>() {
    
    override fun initMainFragment(): Fragment? {
        return RouterUtil.build(MvvmRouterConstants.FRAGMENT_MAIN).launch() as Fragment
    }

    override fun initViews() {

    }

    override fun initData() {

    }
}
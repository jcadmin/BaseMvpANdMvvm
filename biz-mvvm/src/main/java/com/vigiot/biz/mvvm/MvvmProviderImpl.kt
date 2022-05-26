package com.vigiot.biz.mvvm

import android.app.Application
import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.joey.jiang.common.router.provider.BizMvvmProvider
import com.joey.jiang.common.router.router.MvvmRouterConstants

@Route(path = MvvmRouterConstants.PROVIDER)
class MvvmProviderImpl:BizMvvmProvider {
    override fun onApplicationInit(application: Application) {
       
    }

    override fun init(context: Context?) {
       
    }
}
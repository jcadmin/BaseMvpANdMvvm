package com.joye.jiang.biz.mvp

import android.app.Application
import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.joey.jiang.common.router.provider.BizMvpProvider
import com.joey.jiang.common.router.router.MvpRouterConstants

@Route(path = MvpRouterConstants.PROVIDER)
class BizMvpProviderImpl : BizMvpProvider {

    override fun onApplicationInit(application: Application) {
    }

    override fun init(context: Context?) {

    }
}
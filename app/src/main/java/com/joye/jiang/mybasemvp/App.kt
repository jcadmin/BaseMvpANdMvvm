package com.joye.jiang.mybasemvp

import com.alibaba.android.arouter.facade.annotation.Autowired
import com.joye.jiang.common.base.BaseApplication
import com.joye.jiang.common.data.provider.BizMvpProvider
import com.joye.jiang.common.data.provider.BizMvvmProvider

class App : BaseApplication() {

    @Autowired
    @JvmField
    var bizMvpProvider: BizMvpProvider? = null

    @Autowired
    @JvmField
    var bizMvvmProvider: BizMvvmProvider? = null

    override fun initNow() {
        super.initNow()
        bizMvpProvider?.onApplicationInit(this)
        bizMvvmProvider?.onApplicationInit(this)
    }
}
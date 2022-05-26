package com.joye.jiang.common.base.activity

import android.os.Bundle
import androidx.annotation.Keep
import com.hwangjr.rxbus.RxBus
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity

@Keep
abstract class RxBusActivity : RxAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (registerBus()) {
            RxBus.get().register(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (registerBus()) {
            RxBus.get().unregister(this)
        }
    }

    protected open fun registerBus(): Boolean {
        return false
    }
}
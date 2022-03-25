package com.joye.jiang.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Keep
import com.hwangjr.rxbus.RxBus
import com.trello.rxlifecycle3.components.support.RxFragment

@Keep
abstract class RxBusFragment : RxFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (registerBus()) {
            RxBus.get().register(this)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (registerBus()) {
            RxBus.get().unregister(this)
        }
    }

    protected open fun registerBus(): Boolean {
        return false
    }
}
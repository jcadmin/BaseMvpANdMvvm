package com.joye.jiang.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Keep
import com.joye.jiang.common.sdk.RouterUtil

@Keep
open class BaseFragment : RxBusFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RouterUtil.inject(this)
    }
}
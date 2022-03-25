package com.joye.jiang.common.base.activity

import android.os.Bundle
import androidx.annotation.Keep
import com.joye.jiang.common.sdk.RouterUtil
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity

@Keep
abstract class AbstractActivity : RxBusActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RouterUtil.inject(this)
    }
}
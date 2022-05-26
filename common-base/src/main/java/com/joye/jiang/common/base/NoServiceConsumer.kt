package com.joye.jiang.common.base

import android.accounts.NetworkErrorException
import androidx.annotation.Keep
import com.hjq.toast.ToastUtils
import com.joye.jiang.common.sdk.http.ServerException
import io.reactivex.functions.Consumer

@Keep
open class NoServiceConsumer : Consumer<Throwable> {
    /**
     * Consume the given value.
     * @param t the value
     * @throws Exception on error
     */
    override fun accept(t: Throwable?) {
        t?.let {
            when (it) {
                is ServerException -> {

                }
                else -> {
                    ToastUtils.show("服务器异常")
                }
            }
        }
    }
}
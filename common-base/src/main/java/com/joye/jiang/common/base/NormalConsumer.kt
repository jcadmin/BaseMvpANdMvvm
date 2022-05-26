package com.joye.jiang.common.base

import androidx.annotation.Keep
import com.hjq.toast.ToastUtils
import com.joye.jiang.common.sdk.http.ServerException

@Keep
class NormalConsumer : NoServiceConsumer() {
    /**
     * Consume the given value.
     * @param t the value
     * @throws Exception on error
     */
    override fun accept(t: Throwable?) {
        super.accept(t)
        t?.let {
            when (it) {
                is ServerException -> {
                    ToastUtils.show(it.message ?: "服务器异常")
                }
            }
        }
    }
}
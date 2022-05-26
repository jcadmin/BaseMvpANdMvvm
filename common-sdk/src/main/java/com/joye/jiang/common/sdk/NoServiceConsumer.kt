package com.joye.jiang.common.sdk

import android.util.Log
import androidx.annotation.Keep
import com.apkfuns.logutils.LogUtils
import com.hjq.toast.ToastUtils
import com.joye.jiang.common.sdk.http.ServerException
import io.reactivex.functions.Consumer
import java.net.ConnectException

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
                    when (it.code) {
                        401, 403 -> {
                            ToastUtils.show("没有权限访问")
                            LogUtils.e("没有权限访问")
                        }
                    }
                }
                is ConnectException -> {
                    ToastUtils.show("网络连接异常,请打开wifi并且连接")
                    LogUtils.e(it)
                }
                else -> {
                    ToastUtils.show("服务器异常")
                }
            }
        }
    }
}
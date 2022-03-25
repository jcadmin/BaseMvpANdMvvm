package com.joye.jiang.common.sdk.http

import com.alibaba.android.arouter.facade.template.IProvider
import okhttp3.Interceptor

interface TokenProvider : IProvider {

    /**
     * 加载token头部拦截器
     */
    fun loadTokenHeaderInterceptor(mHeaders: HashMap<String, String>? = null): Interceptor

}
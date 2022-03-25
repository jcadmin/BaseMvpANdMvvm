package com.joye.jiang.common.sdk.http

import androidx.annotation.Keep
import okhttp3.CookieJar
import okhttp3.Interceptor
import java.util.*

@Keep
object HttpConfig {
     var mBaseUrl: String? = null
    internal var mInterceptors: MutableList<Interceptor>? = null
    internal var mCookieJar: CookieJar? = null
    @Keep
    class Builder {
        private val mHttpConfig: HttpConfig = HttpConfig

        fun baseUrl(baseUrl: String): Builder {
            mBaseUrl = baseUrl
            return this
        }

        fun interceptors(interceptor: Interceptor): Builder {
            if (mInterceptors == null) {
                mInterceptors = ArrayList()
            }
            mInterceptors!!.add(interceptor)
            return this
        }

        fun cookie(cookieJar: CookieJar): Builder {
            mCookieJar = cookieJar
            return this
        }

        fun build(): HttpConfig {
            return mHttpConfig
        }
    }

}

package com.joye.jiang.common.sdk.http

import androidx.annotation.Keep
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.apkfuns.logutils.LogUtils
import com.joye.jiang.common.sdk.RouterUtil
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit

@Keep
class RetrofitHttpManager private constructor() {

    @JvmField
    @Autowired
    var tokenProvider: TokenProvider? = null

    private var mRetrofitMap: HashMap<String, Retrofit> = hashMapOf()

    private val builder = OkHttpClient.Builder()
        .connectTimeout(DEFAULT_TIME_OUT.toLong(), TimeUnit.SECONDS)
        .readTimeout(DEFAULT_READ_TIME_OUT.toLong(), TimeUnit.SECONDS)
        .writeTimeout(DEFAULT_WRITE_TIME_OUT.toLong(), TimeUnit.SECONDS)
        .cookieJar(HttpConfig.mCookieJar!!)

    init {
        RouterUtil.inject(this)
        HttpConfig.mInterceptors?.forEach {
            builder.addInterceptor(it)
        }
    }

    fun <T> create(clazz: Class<T>): T {
        return create(
            HttpConfig.mBaseUrl,
            clazz,
            headerInterceptor = tokenProvider?.loadTokenHeaderInterceptor()
        )
    }

    fun <T> create(
        baseUrl: String?,
        clazz: Class<T>,
        converterFactory: Converter.Factory = ResponseConverterFactory.create(),
        headerInterceptor: Interceptor? = null
    ): T {
        when {
            //当传入的域名为空时
            baseUrl.isNullOrEmpty() -> {
                throw  NullPointerException()
            }
            //当域名已经创建时
            mRetrofitMap.containsKey(baseUrl) -> {
                return mRetrofitMap[baseUrl]!!.create(clazz)
            }
            else -> {
                val retrofit = createRetrofit(
                    baseUrl, converterFactory,
                    headerInterceptor
                )
                mRetrofitMap[baseUrl] = retrofit
                return retrofit.create(clazz)
            }
        }
    }

    private fun createRetrofit(
        baseUrl: String?,
        converterFactory: Converter.Factory = ResponseConverterFactory.create(),
        headerInterceptor: Interceptor? = null
    ): Retrofit {
        if (baseUrl.isNullOrEmpty()) {
            throw  NullPointerException()
        } else {
            if (headerInterceptor != null) {
                builder.addInterceptor(headerInterceptor)
            }
            LogUtils.d("retrofit interceptor size:${builder.interceptors().size} ; ${builder.interceptors()}")
            return Retrofit.Builder()
                .client(builder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(converterFactory)
                .baseUrl(baseUrl)
                .build()
        }
    }

    private object SingletonHolder {
        val INSTANCE = RetrofitHttpManager()
    }

    @Keep
    companion object {
        var DEFAULT_TIME_OUT = 5
        var DEFAULT_READ_TIME_OUT = 10
        var DEFAULT_WRITE_TIME_OUT = 10

        @JvmStatic
        val instance: RetrofitHttpManager
            get() = SingletonHolder.INSTANCE
    }
}

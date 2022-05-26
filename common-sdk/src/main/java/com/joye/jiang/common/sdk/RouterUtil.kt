package com.joye.jiang.common.sdk

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.Keep
import androidx.annotation.Nullable
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavigationCallback
import com.alibaba.android.arouter.launcher.ARouter
import java.io.Serializable

@Keep
object RouterUtil {

    private var postcard: Postcard? = null

    @JvmStatic
    fun initRouter(application: Application, debug: Boolean) {
        if (debug) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(application)
    }

    @JvmStatic
    fun inject(thiz: Any) {
        ARouter.getInstance().inject(thiz)
    }

    @JvmStatic
    fun build(path: String): RouterUtil {
        postcard = ARouter.getInstance().build(path)
        return this
    }

    fun with(bundle: Bundle?): RouterUtil {
        postcard?.with(bundle)
        return this
    }

    fun withFlags(flg: Int): RouterUtil {
        postcard?.withFlags(flg)
        return this
    }

    fun addFlags(flg: Int): RouterUtil {
        postcard?.addFlags(flg)
        return this
    }

    fun withString(@Nullable key: String, values: String?): RouterUtil {
        postcard?.withString(key, values)
        return this
    }

    fun withLong(@Nullable key: String, values: Long): RouterUtil {
        postcard?.withLong(key, values)
        return this
    }

    fun withDouble(@Nullable key: String, values: Double): RouterUtil {
        postcard?.withDouble(key, values)
        return this
    }

    fun withInt(@Nullable key: String, values: Int): RouterUtil {
        postcard?.withInt(key, values)
        return this
    }

    fun withBoolean(@Nullable key: String, values: Boolean): RouterUtil {
        postcard?.withBoolean(key, values)
        return this
    }

    fun withSerializable(@Nullable key: String, @Nullable values: Serializable): RouterUtil {
        postcard?.withSerializable(key, values)
        return this
    }

    fun withParcelable(@Nullable key: String, @Nullable values: Parcelable?): RouterUtil {
        postcard?.withParcelable(key, values)
        return this
    }

    fun withObject(@Nullable key: String, @Nullable values: Any): RouterUtil {
        when (values) {
            is String -> {
                return withString(key, values)
            }
            is Int -> {
                return withInt(key, values)
            }
            is Double -> {
                return withDouble(key, values)
            }
            is Long -> {
                return withLong(key, values)
            }
            is Boolean -> {
                return withBoolean(key, values)
            }
            is Parcelable -> {
                return withParcelable(key, values)
            }
            is Serializable -> {
                return withSerializable(key, values)
            }
            else -> {
                postcard?.withObject(key, values)
                return this
            }
        }
    }

    fun greenChannel(): RouterUtil {
        postcard?.greenChannel()
        return this
    }

    fun launch(): Any? {
        return launch(null)
    }

    fun launch(context: Context?): Any? {
        return postcard?.navigation(context)
    }

    fun launch(callback: NavigationCallback): Any? {
        return postcard?.navigation(null, callback)
    }

    fun launch(context: Activity?, callback: NavigationCallback): Any? {
        return postcard?.navigation(context, callback)
    }

    fun launch(context: Activity?, requestCode: Int) {
        postcard?.navigation(context, requestCode)
    }

    @JvmStatic
    fun <T> load(service: Class<out T>): T? {
        return ARouter.getInstance().navigation(service)
    }

}
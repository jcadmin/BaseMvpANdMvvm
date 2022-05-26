package com.joye.jiang.common.sdk

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager.NameNotFoundException
import androidx.annotation.Keep
import com.facebook.drawee.backends.pipeline.Fresco
import com.hjq.toast.ToastUtils
import com.joye.jiang.common.sdk.http.RetrofitUtils
import com.qmuiteam.qmui.util.QMUIPackageHelper

/**
 * 初始化application参数,很多工具类需要application参数,能直接在application初始化就尽量在application里面初始化，
 */
@Keep
class ApplicationUtils private constructor() {
    private lateinit var application: Application

    private var buildTime: String? = null

    @Keep
    companion object {
        @JvmStatic
        val instance = SingletonHolder.holder
    }

    private object SingletonHolder {
        val holder = ApplicationUtils()
    }

    fun init(application: Application, buildTime: String? = null) {
        this.application = application
        this.buildTime = buildTime
        Fresco.initialize(application)
        RouterUtil.initRouter(application, BuildConfig.DEBUG)
        SharedPreferenceUtils.instance.initContext(application)
        ToastUtils.init(application)
        RealmUtils.initApplication(application)
        RetrofitUtils.init(application, "http://192.168.0.88:8301")
    }

    fun getApplication(): Application {
        return application
    }

    /**
     * 获取应用程序的外部版本号
     *
     * @return 外部版本号
     */
    fun getVersionName(): String {
        return QMUIPackageHelper.getAppVersion(application)
    }

    /**
     * 获取编译时间
     */
    fun getBuildTime(): String? {
        return buildTime
    }

    fun openApp(context: Context, packageName: String) {
        var intent = context.packageManager.getLaunchIntentForPackage(packageName)
        context.startActivity(intent)
    }
}
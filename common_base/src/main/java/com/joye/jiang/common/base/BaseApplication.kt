package com.joye.jiang.common.base

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.annotation.Keep
import com.hjq.toast.ToastUtils
import com.joye.jiang.common.sdk.*
import com.joye.jiang.common.sdk.extension.launchWithExpHandler
import com.joye.jiang.common.sdk.extension.thread2Io
import com.joye.jiang.common.sdk.http.RetrofitUtils
import io.reactivex.Observable
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.newSingleThreadContext
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

@Keep
abstract class BaseApplication : Application(), Application.ActivityLifecycleCallbacks {

    private val singleDispatcher by lazy {
        Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    }

    @SuppressLint("CheckResult")
    override fun onCreate() {
        ApplicationUtils.instance.init(this, BuildConfig.BUILD_TIME)
        RouterUtil.initRouter(this, BuildConfig.DEBUG)
        SharedPreferenceUtils.instance.initContext(this)
        ToastUtils.init(this)
        RealmUtils.initApplication(this)
        super.onCreate()
        launchWithExpHandler(singleDispatcher) {
            initNow()
        }
        Observable.timer(2, TimeUnit.SECONDS)
            .thread2Io()
            .subscribe({
                delayInit()
            }, {
                it.printStackTrace()
            })
    }

    protected open fun delayInit() {

    }

    /**
     * 在子线程立即初始化
     */
    protected open fun initNow() {

    }


    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
        AppManager.instance.addActivity(p0)
    }

    override fun onActivityStarted(p0: Activity) {
    }

    override fun onActivityResumed(p0: Activity) {
    }

    override fun onActivityPaused(p0: Activity) {
    }

    override fun onActivityStopped(p0: Activity) {
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
    }

    override fun onActivityDestroyed(p0: Activity) {
        AppManager.instance.removeActivity(p0)
    }
}
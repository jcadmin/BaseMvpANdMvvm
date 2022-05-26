package com.joye.jiang.common.base

import android.app.Application

class CrashHandler private constructor() : Thread.UncaughtExceptionHandler {

    companion object {
        @JvmStatic
        val instance = SingletonHolder.holder
    }

    private object SingletonHolder {
        val holder = CrashHandler()
    }

    private lateinit var application: Application

    private var mDefaultExceptionHandler: Thread.UncaughtExceptionHandler? = null

    fun init(application: Application) {
        this.application = application
        mDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();//获取当前默认ExceptionHandler，保存在全局对象
        Thread.setDefaultUncaughtExceptionHandler(this);//替换默认对象为当前对象
    }


    override fun uncaughtException(t: Thread, e: Throwable) {
    }
}
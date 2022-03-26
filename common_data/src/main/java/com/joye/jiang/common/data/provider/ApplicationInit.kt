package com.joye.jiang.common.data.provider

import android.app.Application

interface ApplicationInit {
    /**
     * 程序初始化
     */
    fun onApplicationInit(application: Application)
}
package com.joye.jiang.common.sdk.aidl

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import com.joye.jiang.common.sdk.R

/**
 * aidl的功能服务，继承本类作为前台服务，否则调不起Service
 */
abstract class BaseAidlService : Service() {

    override fun onBind(intent: Intent?): IBinder?  = null

    override fun onCreate() {
        super.onCreate()
        //8.0开始要加这个，不然启动不了服务
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = channelName()?:"MicroAidl"
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(channelName,appName(),
                NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
            val notification = Notification.Builder(applicationContext, channelName).build()
            startForeground(channelId()?:1, notification)
        }
        initData()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
    }

    /**
     * 通知通道号，默认是1
     */
    protected open fun channelId():Int?=null

    /**
     * 通知通道名，默认是"MicroAidl"
     */
    protected open fun channelName():String?=null

    /**
     * app的名字
     */
    protected abstract fun appName():String

    /**
     * 初始化数据，可以在这里注册单例对象
     */
    protected abstract fun initData()
}
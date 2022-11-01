package com.joey.jiang.common.router.provider

import java.util.concurrent.TimeUnit

interface LogService{

    /**
     * 开始定时上传
     * @param time 间隔时长
     * @param unit 间隔时长单位
     */
    fun startAutoUpload(time: Long, unit: TimeUnit)

    /**
     * 停止定时上传
     */
    fun stopAutoUpload()

    /**
     * 记录日志
     */
    fun trackLog(info: String)
}
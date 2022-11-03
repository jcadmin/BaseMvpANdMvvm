package com.joey.biz.log

import com.apkfuns.logutils.LogUtils
import com.joey.jiang.common.router.provider.LogService
import com.therouter.inject.ServiceProvider
import java.util.concurrent.TimeUnit

@ServiceProvider(returnType = LogService::class)
fun LogService(): LogServiceImpl {
    return LogServiceImpl()
}

class LogServiceImpl : LogService {

    override fun startAutoUpload(time: Long, unit: TimeUnit) {
        UpLoadLogInfoManager.INSTANCE.startAutoUpload(time, unit)
    }

    override fun stopAutoUpload() {
        UpLoadLogInfoManager.INSTANCE.stopAutoUpload()
    }

    override fun trackLog(info: String) {
        LogUtils.i(info)
        LogInfoManager.INSTANCE.addLogInfo(info)
    }

}
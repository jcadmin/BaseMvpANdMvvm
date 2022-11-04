package com.joey.biz.log

import com.joey.biz.log.entity.LogInfoBean
import com.yh.krealmextensions.*
import io.realm.kotlin.deleteFromRealm

class LogInfoManager private constructor() {

    companion object {
        val INSTANCE: LogInfoManager by lazy { LogInfoManager() }
    }

    /**
     * 写入日志
     */
    fun addLogInfo(info: String) {
        synchronized(this) {
            var lastId = LogInfoBean().queryLast()?.id ?: 0L
            var logInfo = LogInfoBean()
            logInfo.id = lastId + 1
            logInfo.info = info
            logInfo.time = System.currentTimeMillis()
            logInfo.createOrUpdate()
        }
    }

    /**
     * 删除日志
     */
    fun deleteLogInfo(infoBeanList: List<LogInfoBean>) {
        infoBeanList.map {
            it.deleteFromRealm()
        }
    }

    /**
     * 更新状态
     */
    fun updateLogInfoStatus(infoBeanList: List<LogInfoBean>, status: Int) {
        infoBeanList.map {
            it.status = status
            it
        }.saveAll()
    }


    /**
     * 加载所有日志信息
     */
    fun loadAllLogInfo(): List<LogInfoBean> {
        return LogInfoBean().queryAll()
    }
}
package com.joey.biz.log

import com.joye.jiang.common.sdk.extension.io2main
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class UpLoadLogInfoManager private constructor() {

    /**
     * 上传状态 true-上传中
     */
    @Volatile
    private var uploadStatus = false

    /**
     * 定时上传任务
     */
    private var autoUploadDisposable: Disposable? = null

    /**
     * 上传日志到服务器任务
     */
    private var uploadDisposable: Disposable? = null

    companion object {
        val INSTANCE: UpLoadLogInfoManager by lazy { UpLoadLogInfoManager() }
    }

    /**
     * 开始定时上传
     */
    fun startAutoUpload(time: Long, unit: TimeUnit) {
        autoUploadDisposable = Observable.interval(0, time, unit).subscribe({
            uploadLogInfo()
        }, {
            it.printStackTrace()
        })
    }

    /**
     * 取消定时上传
     */
    fun stopAutoUpload() {
        cancelUpload()
        autoUploadDisposable?.dispose()
        autoUploadDisposable = null
    }

    /**
     * 是否正在上传
     */
    fun isUploading(): Boolean {
        return uploadStatus
    }

    /**
     * 取消当前上传任务
     */
    private fun cancelUpload() {
        uploadDisposable?.dispose()
        uploadDisposable = null
    }

    /**
     * 上传日志
     */
    private fun uploadLogInfo() {
        if (!uploadStatus) {
            synchronized(this) {
                if (!uploadStatus) {
                    uploadStatus = true
                    var logList = LogInfoManager.INSTANCE.loadAllLogInfo()
                    if (logList.isNotEmpty()) {
                        //上传日志到服务器
                        uploadDisposable = Observable.timer(3, TimeUnit.SECONDS).io2main().map {
                            //上传成功，删除本地日志
                            LogInfoManager.INSTANCE.deleteLogInfo(logList)
                            it
                        }.doFinally {
                            uploadStatus = false
                        }.subscribe({

                        }, {

                        })
                    } else {
                        //没有日志需要上传
                        uploadStatus = false
                    }
                } else {
                    //正在上传
                }
            }
        } else {
            //正在上传
        }
    }
}
package com.joey.biz.log.entity

import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.util.UUID

@RealmClass
open class LogInfoBean : RealmModel {
    companion object {
        /**
         * 默认状态-没有上传
         */
        const val STATUS_DEFAULT = 0

        /**
         * 上传中
         */
        const val STATUS_UPLOADING = 1

        /**
         * 上传成功
         */
        const val STATUS_UPLOAD_SUCCESS = 2
    }

    @PrimaryKey
    var id = 0L

    /**
     * 日志信息
     */
    var info = ""

    /**
     * 状态
     */
    var status = STATUS_DEFAULT

    /**
     * 记录时间
     */
    var time = 0L
}
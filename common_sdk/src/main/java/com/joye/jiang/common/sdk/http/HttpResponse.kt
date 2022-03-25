package com.joye.jiang.common.sdk.http

import androidx.annotation.Keep
import com.alibaba.fastjson.annotation.JSONField
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by Admin
 * Http response 返回格式
 */
@Keep
open class HttpResponse<T> : Serializable {
    companion object {
        const val RESPONSE_STATUS_SUCCESS = 0
    }

    @JvmField
    var code: Int = 0

    @JSONField(name = "message")
    @SerializedName("message")
    @JvmField
    var msg: String? = null

    @JvmField
    var data: T? = null

    open fun isSuccess(): Boolean {
        return code == RESPONSE_STATUS_SUCCESS
    }
}

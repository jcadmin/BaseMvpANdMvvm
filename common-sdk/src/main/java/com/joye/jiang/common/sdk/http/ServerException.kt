package com.joye.jiang.common.sdk.http

import androidx.annotation.Keep

/**
 * ServerException发生后，将自动转换为ResponeThrowable返回
 */
@Keep
open class ServerException(
    var code: Int?,
    override var message: String?,
    var data: HttpResponse<Any>?
) :
    RuntimeException()
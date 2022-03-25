package com.joye.jiang.common.sdk.http

import androidx.annotation.Keep
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@Keep
object IBaseHttpService {

    @JvmStatic
    fun mapToRequestBody(paramsMap: HashMap<String, Any>): RequestBody {
        return PostJsonBody.create(Gson().toJson(paramsMap))
    }

    @JvmStatic
    fun mapToFormData(
        paramsMap: HashMap<String, Any>,
        files: HashMap<String, File> = hashMapOf()
    ): RequestBody {
        var builder = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
        paramsMap.keys.forEach {
            builder.addFormDataPart(it, paramsMap[it]!!.toString())
        }
        files.keys.forEach {
            builder.addFormDataPart(
                it,
                files[it]!!.name,
                files[it]!!.asRequestBody("image/*".toMediaTypeOrNull())
            )
        }
        return builder.build()
    }

}
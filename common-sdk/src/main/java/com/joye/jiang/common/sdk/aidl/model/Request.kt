package com.joye.jiang.common.sdk.aidl.model

import android.os.Parcelable
import android.os.Parcel
import androidx.annotation.Keep

@Keep
class Request constructor(
    var type: Int,//请求类型
    var serviceId: String,//请求哪个服务 (ServiceId)
    var methodName: String,//请求的方法名
    var parameters: Array<Parameters?>?//执行方法的参数
) : Parcelable {

    private constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createTypedArray(Parameters.CREATOR))

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(type)
        dest.writeString(serviceId)
        dest.writeString(methodName)
        dest.writeTypedArray(parameters, flags)
    }

    override fun describeContents() = 0

    companion object {
        //获得单例对象
        const val GET_INSTANCE = 0

        //执行方法
        const val GET_METHOD = 1
        @JvmField
        val CREATOR: Parcelable.Creator<Request> = object : Parcelable.Creator<Request> {
            override fun createFromParcel(parcel: Parcel): Request {
                return Request(parcel)
            }

            override fun newArray(size: Int): Array<Request?> {
                return arrayOfNulls(size)
            }
        }
    }
}
package com.joye.jiang.common.sdk.aidl.model

import android.os.Parcelable
import android.os.Parcel
import androidx.annotation.Keep

@Keep
class Response constructor(
    var source: String?, //执行远程方法的返回 json 结果
    var isSuccess: Boolean//是否成功执行远程方法
): Parcelable {

    private constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readByte().toInt() != 0
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(source)
        dest.writeByte((if (isSuccess) 1 else 0).toByte())
    }

    override fun describeContents() = 0

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Response?> = object : Parcelable.Creator<Response?> {
            override fun createFromParcel(parcel: Parcel): Response {
                return Response(parcel)
            }

            override fun newArray(size: Int): Array<Response?> {
                return arrayOfNulls(size)
            }
        }
    }
}
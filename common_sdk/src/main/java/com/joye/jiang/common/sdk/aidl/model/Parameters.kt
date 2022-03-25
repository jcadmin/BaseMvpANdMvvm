package com.joye.jiang.common.sdk.aidl.model

import android.os.Parcelable
import android.os.Parcel
import androidx.annotation.Keep

@Keep
class Parameters constructor(
    var type: String,//参数类型 class
    var value: String//参数值 json序列化后的字符串
    ) : Parcelable {

    private constructor(parcel: Parcel):this(
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(type)
        dest.writeString(value)
    }

    override fun describeContents() = 0

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Parameters> = object : Parcelable.Creator<Parameters> {
            override fun createFromParcel(parcel: Parcel): Parameters {
                return Parameters(parcel)
            }

            override fun newArray(size: Int): Array<Parameters?> {
                return arrayOfNulls(size)
            }
        }
    }
}
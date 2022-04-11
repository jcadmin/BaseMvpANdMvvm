package com.joye.jiang.common.sdk

import android.content.Context
import android.os.Parcelable
import androidx.annotation.Keep
import com.alibaba.fastjson.JSON
import com.tencent.mmkv.MMKV

@Keep
class SharedPreferenceUtils {
    private var defaultKv: MMKV? = null

    private var userKv: MMKV? = null

    @Keep
    companion object {
        @JvmStatic
        val instance = SingletonHolder.holder
    }

    private object SingletonHolder {
        val holder = SharedPreferenceUtils()
    }

    fun initContext(context: Context) {
        MMKV.initialize(context)
        defaultKv = MMKV.defaultMMKV(MMKV.MULTI_PROCESS_MODE, null)
    }

    fun put(key: String, value: Any, userId: String? = null): Boolean? {
        try {
            var kv: MMKV? = if (userId.isNullOrEmpty()) defaultKv else initUserMK(userId)
            when (value) {
                is Boolean -> {
                    return kv?.encode(key, value)
                }
                is Int -> {
                    return kv?.encode(key, value)
                }
                is Long -> {
                    return kv?.encode(key, value)
                }
                is Float -> {
                    return kv?.encode(key, value)
                }
                is Double -> {
                    return kv?.encode(key, value)
                }
                is ByteArray -> {
                    return kv?.encode(key, value)
                }
                is String -> {
                    return kv?.encode(key, value)
                }
                is Parcelable -> {
                    return kv?.encode(key, value)
                }
                is Set<*> -> {
                    return kv?.encode(key, value.toMutableSet() as MutableSet<String>)
                }
                else -> {
                    return false
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    fun <T> get(key: String, defaultValue: T, userId: String? = null): T? {
        try {
            when (defaultValue) {
                is Boolean -> {
                    return getBool(key, defaultValue, userId) as T
                }
                is Int -> {
                    return getInt(key, defaultValue, userId) as T
                }
                is Long -> {
                    return getLong(key, defaultValue, userId) as T
                }
                is Float -> {
                    return getFloat(key, defaultValue, userId) as T
                }
                is Double -> {
                    return getDouble(key, defaultValue, userId) as T
                }
                is ByteArray -> {
                    return getByteArray(key, defaultValue, userId) as T
                }
                is String -> {
                    return getString(key, defaultValue, userId) as T
                }
                is Parcelable -> {
                    return getParcelable(
                        key,
                        defaultValue.javaClass,
                        defaultValue,
                        userId = userId
                    ) as T
                }
                else -> {
                    return null
                }
            }
        } catch (e: Exception) {
            return null
        }
    }

    fun getBool(key: String, defaultValue: Boolean = false, userId: String? = null): Boolean {
        return getMk(userId)?.decodeBool(key, defaultValue) ?: defaultValue
    }

    fun getInt(key: String, defaultValue: Int = 0, userId: String? = null): Int {
        return getMk(userId)?.decodeInt(key, defaultValue) ?: defaultValue
    }

    fun getLong(key: String, defaultValue: Long = 0L, userId: String? = null): Long {
        return getMk(userId)?.decodeLong(key, defaultValue) ?: defaultValue
    }

    fun getFloat(key: String, defaultValue: Float = 0f, userId: String? = null): Float {
        return getMk(userId)?.decodeFloat(key, defaultValue) ?: defaultValue
    }

    fun getDouble(key: String, defaultValue: Double = 0.0, userId: String? = null): Double {
        return getMk(userId)?.decodeDouble(key, defaultValue) ?: defaultValue
    }

    fun getByteArray(
        key: String,
        defaultValue: ByteArray = byteArrayOf(),
        userId: String? = null
    ): ByteArray {
        return getMk(userId)?.decodeBytes(key, defaultValue) ?: defaultValue
    }

    fun getString(key: String, defaultValue: String = "", userId: String? = null): String {
        return getMk(userId)?.decodeString(key, defaultValue) ?: defaultValue
    }

    fun getStringSet(
        key: String,
        defaultValue: Set<String> = mutableSetOf(),
        userId: String? = null
    ): Set<String> {
        return getMk(userId)?.getStringSet(key, defaultValue) ?: defaultValue
    }

    fun <T : Parcelable> getParcelable(
        key: String,
        `class`: Class<T>,
        defaultValue: T? = null, userId: String? = null
    ): Parcelable? {
        return getMk(userId)?.decodeParcelable(key, `class`, defaultValue) ?: defaultValue
    }

    fun putStringSet(key: String, value: Set<String>, userId: String? = null) {
        getMk(userId)?.encode(key, value)
    }

    private fun getMk(userId: String? = null): MMKV? {
        return if (userId.isNullOrEmpty()) defaultKv else initUserMK(userId)
    }

    private fun initUserMK(userId: String): MMKV? {
        synchronized(this) {
            if (userKv == null) {
                userKv = MMKV.mmkvWithID(userId, MMKV.MULTI_PROCESS_MODE)
            }
        }
        return userKv
    }

}
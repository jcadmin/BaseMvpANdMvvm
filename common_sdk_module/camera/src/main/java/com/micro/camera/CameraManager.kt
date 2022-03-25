package com.micro.camera

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.Keep

@Keep
class CameraManager {
    private var cameraIds = mutableMapOf<Int, String>()

    @Keep
    companion object {
        @JvmStatic
        val instance = SingletonHolder.holder
    }

    private object SingletonHolder {
        val holder = CameraManager()
    }

    @SuppressLint("RestrictedApi")
    fun init(context: Context) {
        var cameraManager =
            context.getSystemService(Context.CAMERA_SERVICE) as android.hardware.camera2.CameraManager
        for (cameraId in cameraManager.cameraIdList) {
            cameraIds[cameraId.toIntOrNull() ?: 0] = cameraId
        }
    }

    fun cameraIdList(): MutableList<String> {
        return cameraIds.values.toMutableList()
    }
}
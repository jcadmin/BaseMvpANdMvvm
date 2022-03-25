package com.micro.camera

import android.annotation.SuppressLint
import androidx.camera.camera2.internal.Camera2CameraInfoImpl
import androidx.camera.core.CameraFilter
import androidx.camera.core.CameraInfo
import java.util.*

class MicroCameraFilter(var cameraId: String) : CameraFilter {

    @SuppressLint("RestrictedApi")
    override fun filter(cameraInfos: MutableList<CameraInfo>): MutableList<CameraInfo> {
        val result: MutableList<CameraInfo> = ArrayList()
        for (cameraInfo in cameraInfos) {
            val lensFacing = (cameraInfo as Camera2CameraInfoImpl).cameraId
            if (lensFacing == cameraId) {
                result.add(cameraInfo)
            }
        }
        return result
    }
}
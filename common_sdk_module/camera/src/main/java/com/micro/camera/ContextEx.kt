package com.micro.camera

import android.content.Context
import androidx.annotation.Keep

@Keep
fun Context.startBackCameraService(cameraId: String? = null) {
    MLCameraManage.instance.startBackCameraService(this, cameraId)
}

@Keep
fun Context.stopBackCameraService() {
    MLCameraManage.instance.stopBackCameraService(this)
}

@Keep
fun Context.startBackExtraCameraService(cameraId: String? = null) {
    MLCameraManage.instance.startBackExtraCameraService(this, cameraId)
}

@Keep
fun Context.stopBackExtraCameraService() {
    MLCameraManage.instance.stopBackExtraCameraService(this)
}

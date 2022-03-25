package com.micro.camera

import android.graphics.Bitmap
import android.util.Size
import androidx.camera.core.ImageAnalysis
import com.joye.jiang.common.sdk.MachineManager

class BackCameraService : BaseCameraService() {

    override fun initSize(): Size {
        if (MachineManager.instance.isRK3399()) {
            return Size(1440, 1080)
        } else if (MachineManager.instance.isRK3566R()) {
            return Size(720, 960)
        } else return super.initSize()
    }

    /**
     * 初始化相机id
     */
    override fun initCameraId(): Int {
        return 1
    }

    /**
     * 初始化图像分析
     */
    override fun initCameraAnalyzer(): ImageAnalysis.Analyzer {
        return CameraAnalyzer(this, object : CameraAnalyzer.CameraListener {
            override fun preview(bitmap: Bitmap?) {
                bitmap?.let {
                    MLCameraManage.instance.postBackCamera(it)
                }
            }
        })
    }

}
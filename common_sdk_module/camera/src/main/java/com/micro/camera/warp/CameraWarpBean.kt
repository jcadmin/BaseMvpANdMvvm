package com.micro.camera.warp

import android.util.Size
import androidx.annotation.Keep
import org.opencv.core.Point

/**
 * 投影映射矩阵
 */
@Keep
class CameraWarpBean {
    /**
     * 屏幕尺寸
     */
    var screenSize: MutableList<Int> = mutableListOf()

    /**
     * 摄像头尺寸
     */
    var cameraSize: MutableList<Int> = mutableListOf()

    /**
     * 原始点位
     */
    var srcPointList = mutableListOf<MutableList<Double>>()

    /**
     * 目标点位
     */
    var dstPointList = mutableListOf<MutableList<Double>>()
}
package com.micro.camera

import androidx.annotation.Keep
import com.micro.opencv.utils.distance
import org.opencv.core.Point

/**
 * 手指点击判断
 */
@Keep
class FingerClick {
    /**
     * 两次手指点位位移最小距离，如果超过这个距离，则认为是不同的点位
     */
    private val MIN_DISTANCE = 8

    /**
     * 两次手指检测的时间阈值，时间超过阈值则认为点击 ms
     */
    private val FINGER_TIME_THRESHOLD = 100

    private var firstPoint: Point? = null

    private var firstTime: Long = 0L

    private var lastPoint: Point? = null

    /**
     * 判断是否是点击
     * true：判断为点击行为
     */
    fun isClick(point: Point): Boolean {
        if (firstPoint == null) {
            firstPoint = point
            lastPoint = point
            firstTime = System.currentTimeMillis()
            return false
        }
        if (lastPoint == null) {
            return false
        }
        if (point.distance(lastPoint!!) > MIN_DISTANCE) {
            reset()
            return false
        }
        lastPoint = point
        if (System.currentTimeMillis() - firstTime > FINGER_TIME_THRESHOLD) {
            reset()
            return true
        }
        return false
    }

    /**
     * 重置状态
     */
    fun reset() {
        if (firstPoint != null) {
            firstPoint = null
            firstTime = 0L
            lastPoint = null
        }
    }
}
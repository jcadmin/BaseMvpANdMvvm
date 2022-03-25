package com.micro.camera

import androidx.annotation.Keep
import com.apkfuns.logutils.LogUtils
import com.stericson.RootShell.execution.Command
import com.stericson.RootTools.RootTools
import org.opencv.core.Point
import kotlin.concurrent.thread

/**
 * 监控手指事件，然后点击
 */
@Keep
class MLFingerClickManager {

    private var fingerClick: FingerClick? = null

    private object SingletonHolder {
        val holder = MLFingerClickManager()
    }

    @Keep
    companion object {
        @JvmStatic
        val instance = SingletonHolder.holder
    }

    fun start() {
        fingerClick = FingerClick()
        MLCameraManage.instance.registerBackCamera(this) {
            fingerTip {
                it.screenPoint()?.let {
                    if (fingerClick?.isClick(Point(it[0], it[1])) == true) {
                        handKeyDown(it[0].toInt(), it[1].toInt())
                    }
                }
            }
            noFingers {
                fingerClick?.reset()
            }
        }
    }

    fun stop() {
        MLCameraManage.instance.unregisterBackCamera(this)
        fingerClick?.reset()
        fingerClick = null
    }

    private fun handKeyDown(x: Int, y: Int) {
        LogUtils.d("模拟点击 ${x} , ${y}")
        thread {
            try {
                RootTools.getShell(true)
                    .add(Command(0, "input tap ${x} ${y}"))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
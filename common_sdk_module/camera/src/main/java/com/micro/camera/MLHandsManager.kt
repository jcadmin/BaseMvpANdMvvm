package com.micro.camera

import android.content.Context
import com.google.mediapipe.solutions.hands.Hands
import com.google.mediapipe.solutions.hands.HandsOptions

class MLHandsManager private constructor() {

    private var googleHands: Hands? = null

    private var mediaPipeHands: MediaPipeHands? = null

    private object SingletonHolder {
        val holder = MLHandsManager()
    }

    companion object {
        @JvmStatic
        val instance = SingletonHolder.holder

        private val TAG = this::class.java.simpleName
    }

    fun init(context: Context) {
        if (googleHands != null) {
            return
        }
        googleHands = Hands(
            context, HandsOptions.builder()
                .setStaticImageMode(false)
                .setModelComplexity(0)
                .setMaxNumHands(2)
                .setRunOnGpu(true)
                .build()
        )
        googleHands?.let {
            mediaPipeHands = MediaPipeHands(it)
            mediaPipeHands?.registerCallback {
                fingerTip {
                    MLCameraManage.instance.postFingerResult(it)
                }
                noFingers {
                    MLCameraManage.instance.postNoFinger(it)
                }
            }
        }
        MLCameraManage.instance.registerBackCamera(this) {
            preview {
                mediaPipeHands?.send(it)
            }
        }
    }

    fun dispose() {
        MLCameraManage.instance.unregisterBackCamera(this)
        googleHands = null
        mediaPipeHands = null
    }
}
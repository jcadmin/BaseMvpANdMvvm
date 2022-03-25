package com.joye.jiang.common.base

import androidx.annotation.Keep

@Keep
interface BasePresenter<T : BaseView> {
    /**
     * 初始化view
     */
    fun initView(view: T)

    fun onCreate() {

    }

    fun onStart() {

    }

    fun onResume() {

    }

    fun onPause() {

    }

    fun onStop() {

    }

    fun onDestroy() {

    }

}
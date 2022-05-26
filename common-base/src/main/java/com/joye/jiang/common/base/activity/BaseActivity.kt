package com.joye.jiang.common.base.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.annotation.Keep
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.joye.jiang.common.base.BasePresenter
import com.joye.jiang.common.base.BaseView
import com.joye.jiang.common.base.R
import com.joye.jiang.common.sdk.FragmentUtils
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.util.QMUIStatusBarHelper

@Keep
abstract class BaseActivity<B : ViewDataBinding> : AbstractActivity() {

    protected var presenters = mutableListOf<BasePresenter<BaseView>>()

    protected var binding: B? = null

    protected var mainFragment: Fragment? = null

    /**
     * 初始化布局
     */
    @Keep
    protected open fun initLayoutId(): Int = R.layout.activity_base

    /**
     * 初始化presenter
     */
    @Keep
    protected open fun initPresenter(): MutableList<BasePresenter<BaseView>>? = null

    /**
     * 初始化控件
     */
    @Keep
    abstract fun initViews()

    /**
     * 初始化数据
     */
    @Keep
    abstract fun initData()

    @Keep
    protected open fun initMainFragment(): Fragment? = mainFragment

    /**
     * 是否全屏
     */
    @Keep
    protected open fun isFullScreen() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isFullScreen()) {
            QMUIDisplayHelper.setFullScreen(this)
            window.decorView.apply {
                // Hide both the navigation bar and the status bar.
                // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
                // a general rule, you should design your app to hide the status bar whenever you
                // hide the navigation bar.
                systemUiVisibility =
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE or View.SYSTEM_UI_FLAG_FULLSCREEN
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    windowInsetsController?.hide(WindowInsets.Type.navigationBars())
                    windowInsetsController?.hide(WindowInsets.Type.statusBars())
                    windowInsetsController?.systemBarsBehavior =
                        WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }
            }
        } else {
            QMUIStatusBarHelper.translucent(this)
        }
        var layoutId = if (initLayoutId() != 0) initLayoutId() else R.layout.activity_base
        binding = DataBindingUtil.setContentView(this, layoutId)
        initFragment()
        presenters = initPresenter() ?: mutableListOf()
        presenters.forEach {
            it.onCreate()
        }
        initViews()
        initData()
    }

    private fun initFragment() {
        mainFragment = initMainFragment()
        mainFragment?.let {
            FragmentUtils.replace(supportFragmentManager, R.id.fl_content, it)
        }
    }

    override fun onStart() {
        super.onStart()

        presenters.forEach {
            it.onStart()
        }
    }

    override fun onResume() {
        super.onResume()
        if (isFullScreen()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.decorView.apply {
                    windowInsetsController?.hide(WindowInsets.Type.navigationBars())
                    windowInsetsController?.hide(WindowInsets.Type.statusBars())
                }
            }
        }
        presenters.forEach {
            it.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        presenters.forEach {
            it.onPause()
        }
    }

    override fun onStop() {
        super.onStop()

        presenters.forEach {
            it.onStop()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenters.forEach {
            it.onDestroy()
        }
    }
}
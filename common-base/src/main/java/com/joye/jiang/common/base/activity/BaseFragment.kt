package com.joye.jiang.common.base.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.joye.jiang.common.base.BaseFragment
import com.joye.jiang.common.base.BasePresenter
import com.joye.jiang.common.base.BaseView

@Keep
abstract class BaseFragment<B : ViewDataBinding> : BaseFragment() {

    protected var presenters = mutableListOf<BasePresenter<BaseView>>()

    protected var binding: B? = null

    private var isLoaded = false

    /**
     * 初始化布局
     */
    abstract fun initLayoutId(): Int

    /**
     * 懒加载
     */
    protected open fun lazyInit() {

    }

    abstract fun initView()

    abstract fun initData()

    protected open fun initPresenters(): MutableList<BasePresenter<BaseView>>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, initLayoutId(), null, false)
        presenters = initPresenters() ?: mutableListOf()
        presenters.forEach {
            it.onCreate()
        }
        initView()
        initData()
        return binding?.root
    }

    override fun onStart() {
        super.onStart()

        presenters.forEach {
            it.onStart()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isLoaded && !isHidden) {
            lazyInit()
            isLoaded = true
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

    override fun onDestroyView() {
        super.onDestroyView()
        presenters.forEach {
            it.onDestroy()
        }
        isLoaded = false
    }


}
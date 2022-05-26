package com.joye.jiang.biz.mvp.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.joye.jiang.biz.mvp.R
import com.joye.jiang.biz.mvp.contract.MainContract
import com.joye.jiang.biz.mvp.databinding.MvpFragmentMainBinding
import com.joye.jiang.biz.mvp.presenter.MainPresenter
import com.joye.jiang.common.base.BasePresenter
import com.joye.jiang.common.base.BaseView
import com.joye.jiang.common.base.activity.BaseFragment
import com.joey.jiang.common.router.router.MvpRouterConstants

@Route(path = MvpRouterConstants.FRAGMENT_MAIN)
class MainFragment : BaseFragment<MvpFragmentMainBinding>(), MainContract.View {

    private val mainPresenter by lazy {
        MainPresenter()
    }

    override fun initPresenters(): MutableList<BasePresenter<BaseView>>? {
        mainPresenter.initView(this)
        return mutableListOf(mainPresenter as BasePresenter<BaseView>)
    }

    override fun initLayoutId(): Int {
        return R.layout.mvp_fragment_main
    }

    override fun initView() {

    }

    override fun initData() {

    }
}
package com.joye.jiang.biz.mvp.presenter

import com.joye.jiang.biz.mvp.contract.MainContract
import java.lang.ref.SoftReference

class MainPresenter : MainContract.Presenter {

    private var view: MainContract.View? = null

    override fun initView(view: MainContract.View) {
        this.view = SoftReference(view).get()
    }
}
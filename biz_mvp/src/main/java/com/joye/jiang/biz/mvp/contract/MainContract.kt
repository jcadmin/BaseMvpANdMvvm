package com.joye.jiang.biz.mvp.contract

import com.joye.jiang.common.base.BasePresenter
import com.joye.jiang.common.base.BaseView

interface MainContract {
    interface View : BaseView {

    }

    interface Presenter : BasePresenter<View> {

    }
}
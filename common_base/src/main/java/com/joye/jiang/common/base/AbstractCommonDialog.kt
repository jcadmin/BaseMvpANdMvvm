package com.joye.jiang.common.base

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import androidx.annotation.Keep
import com.qmuiteam.qmui.util.QMUIDisplayHelper

@Keep
abstract class AbstractCommonDialog @JvmOverloads constructor(val mContext: Context, themeResId: Int = R.style.common_dialog) :
    Dialog(mContext, themeResId) {
    protected var mInflater: LayoutInflater? = null
    protected var TAG = AbstractCommonDialog::class.java.simpleName

    init {
        mInflater = LayoutInflater.from(mContext)
    }

    override fun show() {
        super.show()
        var lp = window!!.attributes
        lp.width =
            QMUIDisplayHelper.getScreenWidth(context) - (context.resources.getDimension(R.dimen.sm_px_50) * 2).toInt()
        window!!.setGravity(Gravity.CENTER)
        window!!.attributes = lp
    }
}
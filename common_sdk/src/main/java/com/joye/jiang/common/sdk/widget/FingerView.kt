package com.joye.jiang.common.sdk.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Keep

@Keep
class FingerView : View {

    private var mContext: Context? = null

    private var mPaint: Paint? = null

    private var point: Point? = null

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        this.mContext = context
    }

    fun updatePoint(point: Point?) {
        if (this.point == null && point == null) {
            return
        }
        this.point = point
        postInvalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawColor(Color.TRANSPARENT)
        canvas?.let {
            point?.let {
                canvas.drawCircle(it.x.toFloat(), it.y.toFloat(), 3.0f, getPaint())
            }
        }
    }

    private fun getPaint(): Paint {
        if (mPaint == null) {
            mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            mPaint!!.color = Color.RED
            mPaint!!.strokeWidth = 2f
        }
        return mPaint!!
    }
}
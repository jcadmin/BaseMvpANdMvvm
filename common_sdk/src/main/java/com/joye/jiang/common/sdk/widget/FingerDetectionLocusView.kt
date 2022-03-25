package com.joye.jiang.common.sdk.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Keep

@Keep
class FingerDetectionLocusView : View {

    private var mContext: Context? = null

    private var mPaint: Paint? = null

    private var points: MutableList<Point> = mutableListOf()

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        this.mContext = context
    }

    fun addPoint(point: Point) {
        points.add(point)
        postInvalidate()
    }

    /**
     * 清除轨迹
     */
    fun clearPoint() {
        points.clear()
        postInvalidate()
    }

    private fun getPaint(): Paint {
        if (mPaint == null) {
            mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            mPaint!!.color = Color.RED
            mPaint!!.strokeWidth = 2f
        }
        return mPaint!!
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var oldPoint: Point? = null
        for (point in points) {
            canvas?.drawCircle(point.x.toFloat(), point.y.toFloat(), 3.0f, getPaint())
            /*if (oldPoint != null && oldPoint != point) {
                canvas?.drawLine(
                    oldPoint.x.toFloat(),
                    oldPoint.y.toFloat(),
                    point.x.toFloat(),
                    point.y.toFloat(), getPaint()
                )
            }*/
            oldPoint = point
        }
        /*if (points.isNotEmpty()) {
            var lastPoint = points.last()
            var rect = Rect(
                lastPoint.x.toInt() - 20,
                lastPoint.y.toInt() - 10,
                lastPoint.x.toInt() + 20,
                lastPoint.y.toInt() - 10
            )

            canvas?.drawRect(rect, getPaint())
        }*/
    }

}
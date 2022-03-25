package com.joye.jiang.common.sdk.widget

import android.graphics.*
import android.graphics.drawable.Drawable


class CoverDrawable(private val drawable: Drawable) : Drawable() {
    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path: Path = Path()

    /**
     * 绘制圆
     *
     * @param drawable
     * @param x
     * @param y
     * @param radius
     */
    constructor(
        drawable: Drawable,
        x: Int, y: Int, radius: Int
    ) : this(drawable) {
        path.addCircle(x.toFloat(), y.toFloat(), radius.toFloat(), Path.Direction.CW)
    }

    /**
     * 绘制圆角矩形
     *
     * @param drawable
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @param rx
     * @param ry
     */
    constructor(
        drawable: Drawable,
        left: Int, top: Int, right: Int, bottom: Int, rx: Int, ry: Int
    ) : this(drawable) {
        path.addRoundRect(
            left.toFloat(),
            top.toFloat(),
            right.toFloat(),
            bottom.toFloat(),
            rx.toFloat(),
            ry.toFloat(),
            Path.Direction.CW
        )
    }

    override fun draw(canvas: Canvas) {
        drawable.bounds = bounds
        if (path.isEmpty) {
            drawable.draw(canvas)
        } else {
            //将绘制操作保存在新的图层，因为图像合成是很昂贵的操作，将用到硬件加速，这里将图像合成的处理放到离屏缓存中进行
            val saveCount = canvas.saveLayer(
                0f,
                0f,
                canvas.width.toFloat(),
                canvas.height.toFloat(),
                paint
            )
            //绘制目标图
            drawable.draw(canvas)
            //设置混合模式
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            //绘制原图
            canvas.drawPath(path, paint)
            //清除混合模式
            paint.xfermode = null
            //还原画布
            canvas.restoreToCount(saveCount)
        }
    }

    override fun setAlpha(i: Int) {
        drawable.alpha = i
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        drawable.colorFilter = colorFilter
    }

    override fun getOpacity(): Int {
        return drawable.opacity
    }

    init {
        paint.color = -0x1
    }
}
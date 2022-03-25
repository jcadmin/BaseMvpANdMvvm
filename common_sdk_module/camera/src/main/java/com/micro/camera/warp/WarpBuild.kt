package com.micro.camera.warp

import androidx.annotation.Keep

@Keep
class WarpBuilder constructor() {

    private var x: Double = 0.0

    private var y: Double = 0.0

    private var imageWidth = 0

    private var imageHeight = 0

    /**
     * 是否兼容相机没有初始化，相机没有初始化时返回相机坐标
     * 如果相机已经初始化，则compatible无效
     */
    private var compatible = false

    fun x(x: Double): WarpBuilder {
        this.x = x
        return this
    }

    fun y(y: Double): WarpBuilder {
        this.y = y
        return this
    }

    fun imageWidth(imageWidth: Int): WarpBuilder {
        this.imageWidth = imageWidth
        return this
    }

    fun imageHeight(imageHeight: Int): WarpBuilder {
        this.imageHeight = imageHeight
        return this
    }

    /**
     * 是否兼容相机没有初始化，相机没有初始化时返回相机坐标,为了测试用,正式环境一般都是已经初始化
     * 如果相机已经初始化，则compatible无效
     */
    fun compatible(compatible: Boolean): WarpBuilder {
        this.compatible = compatible
        return this
    }

    fun build(): WarpBean {
        var bean = WarpBean(x, y, imageWidth, imageHeight)
        bean.compatible = compatible
        return bean
    }
}
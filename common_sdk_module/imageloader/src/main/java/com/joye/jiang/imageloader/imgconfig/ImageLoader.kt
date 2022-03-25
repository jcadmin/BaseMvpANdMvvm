package com.joye.jiang.imageloader.imgconfig

import android.net.Uri
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import com.bumptech.glide.load.resource.gif.GifDrawable
import java.io.File
import java.util.*

/**
 * 图片加载对象
 * 使用Builder模式，一步一步的创建一个复杂对象的创建者模式;
 * 它允许用户在不知道内部构建细节的情况下，可以更精细的控制对象的构建流程
 * 使用：
 * ImageLoader<String> imageLoader = ImageLoader.createBuilder(url)
 * .setLoadErrorResId(-1)
 * .setPlaceHolderResId(-1)
 * .setImageView(null)
 * .build();
</String> */
class ImageLoader<SourceType> private constructor(builder: Builder<SourceType>) {
    /**
     * 图片url
     *
     * @return
     */
    val source //资源来源
            : SourceType

    /**
     * 占位图(默认图)资源id
     *
     * @return
     */
    val placeHolderResId //占位图(默认图)资源id
            : Int

    /**
     * 占位图(加载出错图)资源id
     *
     * @return
     */
    val loadErrorResId //图片加载失败后显示图片的资源id
            : Int

    /**
     * 图片控件
     *
     * @return
     */
    val imageView //图片控件
            : ImageView

    /**
     * 是否GIF
     *
     * @return
     */
    val isGif //是否GIF图片
            : Boolean

    /**
     * gif播放次数
     */
    val gifLoopCount: Int

    /**
     * 圆角
     *
     * @return
     */
    val radius //圆角
            : Int
    val scaleType = ScaleType.CENTER_INSIDE

    /**
     * 图片渲染模式
     *
     * @return
     */
    // 图片外形类型
    var imageTransfor: List<Int> = ArrayList<Int>()

    /**
     * ImageLoader builder工具
     *
     * @author LiJiaJian
     * @date 2016/5/26 10:09
     */
    class Builder<SourceType>(val source: SourceType) {
        var placeHolderResId //占位图(默认图)资源id
                = 0
        var loadErrorResId //图片加载失败后显示图片的资源id
                = 0
        lateinit var imageView: ImageView //图片控件

        var isGif //是否GIF图片
                = false

        var gifLoopCount = -1

        var radius //圆角
                = 0
        private var scaleType = ScaleType.CENTER_INSIDE

        // 图片外形类型
        val imageTransfor: MutableList<Int> = ArrayList()


        /**
         * 设置占位图(默认图)资源id
         *
         * @param placeHolderResId
         * @return
         */
        fun setPlaceHolderResId(placeHolderResId: Int): Builder<SourceType> {
            this.placeHolderResId = placeHolderResId
            return this
        }

        /**
         * 设置图片加载失败后显示图片的资源id
         *
         * @param loadErrorResId
         * @return
         */
        fun setLoadErrorResId(loadErrorResId: Int): Builder<SourceType> {
            this.loadErrorResId = loadErrorResId
            return this
        }

        /**
         * 设置图片控件
         *
         * @param imageView
         * @return
         */
        fun setImageView(imageView: ImageView): Builder<SourceType> {
            this.imageView = imageView
            return this
        }

        /**
         * 标记是否gif
         *
         * @return
         */
        fun asGif(loopCount: Int = GifDrawable.LOOP_FOREVER): Builder<SourceType> {
            isGif = true
            this.gifLoopCount = loopCount
            return this
        }


        /**
         * 构造ImageLoader对象
         *
         * @return
         */
        fun build(): ImageLoader<SourceType> {
            return ImageLoader<SourceType>(this)
        }

        /**
         * 圆角
         *
         * @param radius
         * @return
         */
        fun radius(radius: Int): Builder<SourceType> {
            this.radius = radius
            return this
        }

        /**
         * 图片显示模式
         *
         * @param scaleType
         * @return
         */
        fun scaleType(scaleType: ScaleType): Builder<SourceType> {
            this.scaleType = scaleType
            return this
        }

        /**
         * 图片渲染模式
         *
         * @param transfors
         * @return
         */
        fun imageTransfor(@ImageTransfor vararg transfors: Int): Builder<SourceType> {
            for (transfor in transfors) {
                imageTransfor.add(transfor)
            }
            return this
        }
    }

    companion object {
        /**
         * 创建网络资源Buidler对象
         *
         * @param url
         * @return
         */
        fun createBuilder(url: String): Builder<String> {
            return Builder(url)
        }

        /**
         * 创建磁盘文件资源Buidler对象
         *
         * @param file
         * @return
         */
        fun createBuilder(file: File): Builder<File> {
            return Builder(file)
        }

        /**
         * 创建Uri资源Buidler对象
         *
         * @param uri
         * @return
         */
        fun createBuilder(uri: Uri): Builder<Uri> {
            return Builder(uri)
        }

        /**
         * 创建本地资源Buidler对象
         *
         * @param resId
         * @return
         */
        fun createBuilder(resId: Int): Builder<Int> {
            return Builder(resId)
        }
    }

    init {
        source = builder.source
        placeHolderResId = builder.placeHolderResId
        loadErrorResId = builder.loadErrorResId
        imageView = builder.imageView
        isGif = builder.isGif
        radius = builder.radius
        imageTransfor = builder.imageTransfor
        gifLoopCount = builder.gifLoopCount
    }
}
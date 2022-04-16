package com.joye.jiang.imageloader.provider

import android.app.Fragment
import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.joye.jiang.imageloader.imgconfig.ImageLoader

abstract class BaseImageLoaderProvider(context: Context?) {
    /**
     * 初始化配置
     */
    protected abstract fun initConfigs(context: Context?)

    /**
     * 释放图片占有的内存
     *
     * @param context
     */
    abstract fun releaseMemoryCache(context: Context)

    /**
     * 加载图片
     *
     * @param context  上下文
     * @param loader   图片加载对象
     * @param listener 图片加载监听
     */
    abstract fun loadImage(
        context: Context,
        loader: ImageLoader<*>,
        listener: ImageLoaderListener? = null
    )
    
    /**
     * 加载gif图片
     */
    abstract fun loadGif(loader: ImageLoader<*>, listener: GifLoaderListener?=null)


    interface ImageLoaderListener {
        /**
         * 图片加载失败
         */
        fun loadFailed()

        /**
         * 图片加载成功
         */
        fun loadSuccess(bitmap: Bitmap?)
    }

    interface GifLoaderListener {
        /**
         * Gif加载失败
         */
        fun loadFailed()

        /**
         * Gif加载成功
         */
        fun loadSuccess(gifDrawable: GifDrawable?)
    }

    /**
     * 初始化
     */
    init {
        initConfigs(context)
    }
}
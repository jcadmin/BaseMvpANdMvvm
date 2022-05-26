package com.joye.jiang.imageloader.manager

import android.content.Context
import android.graphics.Bitmap
import com.joye.jiang.imageloader.imgconfig.ImageLoader
import com.joye.jiang.imageloader.provider.BaseImageLoaderProvider
import com.joye.jiang.imageloader.provider.impl.GlideImageLoaderProvider

/**
 * 图片加载管理器
 */
class ImageLoaderManager private constructor() {
    /**
     * 图片加载提供者对象
     */
    private var mProvider: BaseImageLoaderProvider? = null

    /**
     * 本地缓存路径
     */
    var diskCachePath: String? = null
        private set

    /**
     * 初始化管理器
     *
     * @param applicationContext
     */
    fun init(applicationContext: Context?, diskCachePath: String?) {
        checkedContextNotNull(applicationContext)
        // 目前使用Google开源图片加载框架Glide
        // 后续如果切换别的图片框架重写一个BaseImageLoaderProvider替换下面GlideImageLoaderProvider即可
        mProvider = GlideImageLoaderProvider(applicationContext)
        this.diskCachePath = diskCachePath
    }

    /**
     * 检查上下文是否为NULL
     *
     * @param context
     */
    private fun checkedContextNotNull(context: Context?) {
        requireNotNull(context) { "applicationContext can not be null~    please run init~" }
    }

    /**
     * 加载图片
     *
     * @param context 上下文
     * @param loader  图片加载对象
     */
    fun loadImage(
        context: Context?,
        loader: ImageLoader<*>?,
        listener: BaseImageLoaderProvider.ImageLoaderListener? = null
    ) {
        mProvider!!.loadImage(context!!, loader!!, listener)
    }

    fun loadAvatar(context: Context?, loader: ImageLoader<*>?) {
        mProvider!!.loadImage(context!!, loader!!)
    }

    fun releaseMemoryCache(context: Context?) {
        mProvider!!.releaseMemoryCache(context!!)
    }

    fun loadGif(
        loader: ImageLoader<*>,
        listener: BaseImageLoaderProvider.GifLoaderListener? = null
    ) {
        mProvider!!.loadGif(loader, listener)
    }

    companion object {
        /**
         * Log TAG
         */
        private val TAG = ImageLoaderManager::class.java.simpleName

        @JvmStatic
        val instance = SingletonHolder.holder
    }

    private object SingletonHolder {
        val holder = ImageLoaderManager()
    }
}
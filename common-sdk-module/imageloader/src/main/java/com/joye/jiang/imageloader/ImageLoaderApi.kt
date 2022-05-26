package com.joye.jiang.imageloader

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.joye.jiang.imageloader.imgconfig.ImageConstants
import com.joye.jiang.imageloader.imgconfig.ImageLoader
import com.joye.jiang.imageloader.manager.ImageLoaderManager
import com.joye.jiang.imageloader.provider.BaseImageLoaderProvider
import java.io.File

object ImageLoaderApi {
    const val RESOURCE_ID_NONE = -1

    /**
     * 图片加载
     *
     * @param imageLoader
     * @param context
     */
    fun loadImage(imageLoader: ImageLoader<*>?, listener: BaseImageLoaderProvider.ImageLoaderListener? = null) {
        ImageLoaderManager.instance.loadImage(imageLoader?.imageView?.context, imageLoader, listener)
    }

    /**
     * 最简单的图片加载
     *
     * @param url
     * @param imageView
     * @param context
     */
    fun loadImage(url: String, imageView: ImageView) {
        loadImage(url, RESOURCE_ID_NONE, RESOURCE_ID_NONE, imageView)
    }

    /**
     * 图片加载
     *
     * @param url
     * @param placeHolderResId
     * @param loadErrorResId
     * @param imageView
     * @param context
     */
    fun loadImage(
        url: String,
        placeHolderResId: Int,
        loadErrorResId: Int,
        imageView: ImageView
    ) {
        loadImage(url, placeHolderResId, loadErrorResId, 0, imageView)
    }

    /**
     * 加载高斯模糊图片
     *
     * @param url
     * @param radius    模糊度       0:25
     * @param imageView
     */
    fun loadImageBlur(url: String, radius: Int, imageView: ImageView) {
        loadImage(url, RESOURCE_ID_NONE, RESOURCE_ID_NONE, radius, imageView)
    }

    /**
     * 加载头像
     *
     * @param url
     * @param imageView
     */
    /*
    public static void loadAvatar(String url, ImageView imageView) {
        loadImage(url, R.mipmap.default_head, R.mipmap.default_head, 0, imageView);
    }*/
    /**
     * 加载图片
     *
     * @param url
     * @param imageView
     */
    fun loadImage(
        url: String?,
        imageView: ImageView,
        listener: BaseImageLoaderProvider.ImageLoaderListener? = null
    ) {
        loadImage(url, RESOURCE_ID_NONE, RESOURCE_ID_NONE, 0, imageView, listener)
    }

    fun loadImage(
        url: String?,
        radius: Int,
        imageView: ImageView,
        listener: BaseImageLoaderProvider.ImageLoaderListener? = null
    ) {
        loadImage(url, RESOURCE_ID_NONE, RESOURCE_ID_NONE, radius, imageView, listener)
    }

    fun loadImage(
        url: String?,
        placeHolderResId: Int,
        loadErrorResId: Int,
        imageView: ImageView,
        listener: BaseImageLoaderProvider.ImageLoaderListener? = null
    ) {
        loadImage(url, placeHolderResId, loadErrorResId, 0, imageView, listener)
    }

    /**
     * @param url
     * @param placeHolderResId
     * @param loadErrorResId
     * @param radius           圆角
     * @param imageView
     */
    fun loadImage(
        url: String?,
        placeHolderResId: Int,
        loadErrorResId: Int,
        radius: Int,
        imageView: ImageView,
        listener: BaseImageLoaderProvider.ImageLoaderListener? = null
    ) {
        if (url == null) {
            return
        }
        val imageLoader: ImageLoader<String> = ImageLoader.Companion.createBuilder(url)
            .setPlaceHolderResId(placeHolderResId)
            .setLoadErrorResId(loadErrorResId)
            .setImageView(imageView)
            .scaleType(imageView.scaleType)
            .radius(radius)
            .build()
        ImageLoaderManager.instance.loadImage(imageView.context, imageLoader, listener)
    }

    /**
     * 最简单的图片加载
     *
     * @param resId
     * @param imageView
     * @param context
     */
    fun loadGif(
        url: String,
        imageView: ImageView,
        gifLoaderListener: BaseImageLoaderProvider.GifLoaderListener? = null
    ) {
        loadGif(url, RESOURCE_ID_NONE, RESOURCE_ID_NONE, imageView, gifLoaderListener)
    }

    /**
     * GIF图片加载
     *
     * @param url
     * @param placeHolderResId
     * @param loadErrorResId
     * @param imageView
     * @param context
     */
    fun loadGif(
        url: String?,
        placeHolderResId: Int,
        loadErrorResId: Int,
        imageView: ImageView,
        gifLoaderListener: BaseImageLoaderProvider.GifLoaderListener? = null
    ) {
        if (url == null) {
            return
        }
        val imageLoader: ImageLoader<String> = ImageLoader.Companion.createBuilder(url)
            .setPlaceHolderResId(placeHolderResId)
            .asGif()
            .setLoadErrorResId(loadErrorResId)
            .setImageView(imageView)
            .build()
        ImageLoaderManager.instance.loadGif(imageLoader, gifLoaderListener)
    }


    /**
     * 最简单的图片加载
     *
     * @param file
     * @param imageView
     * @param context
     */
    fun loadImage(file: File, imageView: ImageView) {
        loadImage(file, RESOURCE_ID_NONE, RESOURCE_ID_NONE, imageView)
    }

    /**
     * 图片加载
     *
     * @param file
     * @param placeHolderResId
     * @param loadErrorResId
     * @param imageView
     * @param context
     */
    fun loadImage(
        file: File,
        placeHolderResId: Int,
        loadErrorResId: Int,
        imageView: ImageView
    ) {
        loadImage(Uri.fromFile(file), placeHolderResId, loadErrorResId, imageView)
    }


    /**
     * 最简单的图片加载
     *
     * @param uri
     * @param imageView
     * @param context
     */
    fun loadImage(uri: Uri, imageView: ImageView) {
        loadImage(uri, RESOURCE_ID_NONE, RESOURCE_ID_NONE, imageView)
    }


    /**
     * 图片加载
     *
     * @param uri
     * @param placeHolderResId
     * @param loadErrorResId
     * @param imageView
     * @param context
     */
    fun loadImage(
        uri: Uri,
        placeHolderResId: Int,
        loadErrorResId: Int,
        imageView: ImageView
    ) {
        val imageLoader: ImageLoader<Uri> = ImageLoader.Companion.createBuilder(uri)
            .setPlaceHolderResId(placeHolderResId)
            .setLoadErrorResId(loadErrorResId)
            .setImageView(imageView)
            .build()
        ImageLoaderManager.instance.loadImage(imageView.context, imageLoader)
    }

    /**
     * 最简单的图片加载
     *
     * @param resId
     * @param imageView
     * @param context
     */
    fun loadImage(resId: Int, imageView: ImageView) {
        loadImage(resId, RESOURCE_ID_NONE, RESOURCE_ID_NONE, imageView)
    }

    fun loadImage(
        resId: Int,
        placeHolderResId: Int,
        loadErrorResId: Int,
        imageView: ImageView
    ) {
        val imageLoader: ImageLoader<Int> = ImageLoader.Companion.createBuilder(resId)
            .setPlaceHolderResId(placeHolderResId)
            .setLoadErrorResId(loadErrorResId)
            .setImageView(imageView)
            .build()
        ImageLoaderManager.instance.loadImage(imageView.context, imageLoader)
    }


    fun releaseMemoryCache(context: Context?) {
        ImageLoaderManager.instance.releaseMemoryCache(context)
    }
}
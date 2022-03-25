package com.joye.jiang.imageloader.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory
import com.bumptech.glide.module.AppGlideModule
import com.joye.jiang.imageloader.manager.ImageLoaderManager

@GlideModule
class GlideModuleConfig : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        builder.setDiskCache(
            DiskLruCacheFactory(
                ImageLoaderManager.instance.diskCachePath, 1024 * 1024 * 100
            )
        )
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
        // 写入咱们的okhttp
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        // 写入咱们的okhttp的拦截器,在拦截器中监听进度
//        builder.addInterceptor(new ProgressInterceptor());
//        OkHttpClient okHttpClient = builder.build();
//        // glide吧urlConnection替换为okhttp
//        registry.replace(GlideUrl.class, InputStream.class, new OkHttpGlideUrlLoader.Factory(okHttpClient));
    }
}
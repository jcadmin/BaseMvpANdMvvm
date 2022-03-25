package com.joye.jiang.imageloader.imgconfig

import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoader.LoadData
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import okhttp3.OkHttpClient
import java.io.InputStream


class OkHttpGlideUrlLoader(private val okHttpClient: OkHttpClient) : ModelLoader<GlideUrl, InputStream> {
    override fun handles(glideUrl: GlideUrl): Boolean {
        return false
    }

    class Factory : ModelLoaderFactory<GlideUrl, InputStream> {
        private var client: OkHttpClient? = null

        constructor() {}
        constructor(client: OkHttpClient?) {
            this.client = client
        }

        @Synchronized
        private fun getOkHttpClient(): OkHttpClient {
            if (client == null) {
                client = OkHttpClient()
            }
            return client!!
        }

        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<GlideUrl, InputStream> {
            return OkHttpGlideUrlLoader(getOkHttpClient())
        }

        override fun teardown() {}
    }

    override fun buildLoadData(
        glideUrl: GlideUrl,
        width: Int,
        height: Int,
        options: Options
    ): LoadData<InputStream>? {
//        return new OkHttpFetcher(okHttpClient, glideUrl);
        return null
    } //    @Override
}
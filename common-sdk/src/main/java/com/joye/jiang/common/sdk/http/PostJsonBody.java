package com.joye.jiang.common.sdk.http;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;

import static kotlin.text.Charsets.UTF_8;

@Keep
public class PostJsonBody extends RequestBody {

    private static final MediaType JSON = MediaType.parse("application/json");
    private static final Charset charset = UTF_8;

    private String content;

    public PostJsonBody(@NonNull String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return JSON;
    }

    @Override
    public void writeTo(@NonNull BufferedSink sink) throws IOException {
        byte[] bytes = content.getBytes(charset);
        if (bytes == null) throw new NullPointerException("content == null");
        Util.checkOffsetAndCount(bytes.length, 0, bytes.length);
        sink.write(bytes, 0, bytes.length);
    }

    public static RequestBody create(@NonNull String content) {
        return new PostJsonBody(content);
    }
}

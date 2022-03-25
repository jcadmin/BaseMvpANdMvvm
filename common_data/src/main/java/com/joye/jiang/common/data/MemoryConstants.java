package com.joye.jiang.common.data;

import android.os.Environment;

import androidx.annotation.IntDef;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class MemoryConstants {

    /**
     * Byte与Byte的倍数
     */
    public static final int BYTE = 1;
    /**
     * KB与Byte的倍数
     */
    public static final int KB = 1024;
    /**
     * MB与Byte的倍数
     */
    public static final int MB = 1048576;
    /**
     * GB与Byte的倍数
     */
    public static final int GB = 1073741824;

    @IntDef({BYTE, KB, MB, GB})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Unit {
    }

    /**
     * app图片缓存路径
     */
    public static String IMAGE_CACHE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "joye.jiang" + File.separator + "cache" + File.separator + "images";
    public static String DATA_CACHE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "joye.jiang" + File.separator + "cache" + File.separator + "data";


    /**
     * 留海屏高度
     */
    public static int NOTCH_HEIGHT = 0;
    public static int LAYOUT_HEIGHT = 0;
}

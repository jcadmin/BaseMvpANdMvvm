package com.joye.jiang.imageloader.imgconfig

import androidx.annotation.IntDef
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@IntDef(
    ImageConstants.IMAGE_TRANSFOR_DEFAULT,
    ImageConstants.IMAGE_TRANSFOR_CROP_CIRCLE,
    ImageConstants.IMAGE_TRANSFOR_CROP,
    ImageConstants.IMAGE_TRANSFOR_CROP_SQUARE,
    ImageConstants.IMAGE_TRANSFOR_CROP_CORNER,
    ImageConstants.IMAGE_TRANSFOR_COLOR_FILTER,
    ImageConstants.IMAGE_TRANSFOR_GRAY_SCALE,
    ImageConstants.IMAGE_TRANSFOR_BLUR,
    ImageConstants.IMAGE_TRANSFOR_MASK
)
@Retention(
    RetentionPolicy.SOURCE
)
annotation class ImageTransfor
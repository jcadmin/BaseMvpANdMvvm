package com.micro.camera.warp

import androidx.annotation.Keep

@Keep
class WarpBean constructor(
    var x: Double,
    var y: Double,
    var imageWidth: Int,
    var imageHeight: Int
) {
    var compatible = false
}
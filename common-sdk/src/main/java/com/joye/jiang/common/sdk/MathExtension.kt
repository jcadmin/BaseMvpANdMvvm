package com.joye.jiang.common.sdk

import androidx.annotation.Keep
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

/**
 * 保留小数位
 */
@Keep
fun String.formatPre(pre: Int): String =
    BigDecimal(this).setScale(pre, RoundingMode.DOWN).toDouble().toString()
@Keep
fun Double.formatPreHalfUp(pre: Int): Double =
    this.toString().formatPreHalfUp(pre)
@Keep
fun String.formatPreHalfUp(pre: Int): Double =
    BigDecimal(this).setScale(pre, RoundingMode.HALF_UP).toDouble()
@Keep
fun String.formatPreDown(pre: Int): Double =
    BigDecimal(this).setScale(pre, RoundingMode.DOWN).toDouble()
@Keep
fun String.formatPreUp(pre: Int): String =
    BigDecimal(this).setScale(pre, RoundingMode.UP).toDouble().toString()
@Keep
fun Double.formatPre(pre: Int): String = this.toString().formatPre(pre)
@Keep
fun Double.formatPreUp(pre: Int): Double =
    this.toString().formatPreUp(pre).toDouble()
@Keep
fun Double.formatPreDown(pre: Int): Double =
    this.toString().formatPreDown(pre).toDouble()
@Keep
fun Float.formatToInt(): Int = BigDecimal(this.toString()).setScale(0, RoundingMode.HALF_UP).toInt()

/**
 * 乘法
 */
@Keep
fun Int.mul(double: Double): Double =
    this.toDouble().mul(double)
@Keep
fun Double.mul(double: Double): Double =
    BigDecimal(this.toString()).multiply(BigDecimal(double.toString())).toDouble()
@Keep
fun Long.mul(long: Long): Long =
    BigDecimal(this.toString()).multiply(BigDecimal(long.toString())).toLong()
@Keep
fun BigInteger.mul(double: Double): BigInteger = BigInteger.valueOf(this.toDouble().mul(double).toLong())
@Keep
fun BigDecimal.mul(double: Double): BigDecimal = BigDecimal.valueOf(this.toDouble().mul(double))

/**
 * 减法
 */
@Keep
fun Int.sub(double: Double): Double =
    this.toDouble().sub(double)

/**
 * 减法
 */
@Keep
fun Long.sub(double: Long): Double =
    this.toDouble().sub(double.toDouble())
@Keep
fun Double.sub(double: Double): Double =
    BigDecimal(this.toString()).subtract(BigDecimal(double.toString())).toDouble()


/**
 * 除法
 */
@Keep
fun Int.divide(double: Double): Double =
    this.toDouble().divide(double)
@Keep
fun Long.divide(double: Double): Double =
    this.toDouble().divide(double)
@Keep
fun Float.divide(double: Double): Double =
    this.toDouble().divide(double)
@Keep
fun Double.divide(double: Double): Double =
    divideUp(double, 8)
@Keep
fun Double.divideDown(double: Double): Double =
    BigDecimal(this.toString()).divide(
        BigDecimal(double.toString()),
        8,
        RoundingMode.DOWN
    ).toDouble()
@Keep
fun Double.divideUp(double: Double, scale: Int): Double = BigDecimal(this.toString()).divide(
    BigDecimal(double.toString()),
    scale,
    RoundingMode.HALF_UP
).toDouble()





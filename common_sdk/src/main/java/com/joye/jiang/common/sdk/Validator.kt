package com.joye.jiang.common.sdk

import java.util.regex.Pattern

/**
 * 正则匹配
 */
object Validator {

    /**
     * 正则表达式，中文
     */
    private const val REGEX_CHINESE = "^[\\u4e00-\\u9fa5]\$"

    /**
     * 校验汉字
     */
    fun isChinese(string: String): Boolean {
        return Pattern.matches(REGEX_CHINESE, string)
    }
}
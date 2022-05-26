package com.joye.jiang.common.sdk

import android.content.Context
import androidx.annotation.Keep
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception

@Keep
object AssetUtils {

    fun getJson(fileName: String, context: Context): String {
        var stringBuilder = StringBuilder()
        try {
            var assetManager = context.assets
            var bf = BufferedReader(InputStreamReader(assetManager.open(fileName)))
            var line = ""
            while ((bf.readLine().also { line = it }) != null) {
                stringBuilder.append(line)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }
}
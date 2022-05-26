package com.joye.jiang.common.sdk

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSuggestion
import android.os.Build
import android.os.Bundle
import android.provider.Settings.ACTION_WIFI_ADD_NETWORKS
import android.provider.Settings.EXTRA_WIFI_NETWORK_LIST
import androidx.annotation.Keep
import androidx.annotation.RequiresApi
import com.apkfuns.logutils.LogUtils
import com.hjq.toast.ToastUtils
import java.util.concurrent.Executor

@Keep
class WifiUtils constructor(var context: Activity) {
    private var wifiManager: WifiManager? = null

    init {
        wifiManager =
            context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    fun isWifiEnable(): Boolean {
        return wifiManager?.isWifiEnabled ?: false
    }

    fun openWifi() {
        wifiManager?.setWifiEnabled(true)
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.R)
    fun connectWifi(ssid: String, pwd: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            wifiManager?.addSuggestionConnectionStatusListener(
                { command -> LogUtils.d("connectWifi  execute ${command}") }
            ) { wifiNetworkSuggestion, failureReason ->
                ToastUtils.debugShow("连接wifi${wifiNetworkSuggestion.ssid}结果${failureReason}")
                LogUtils.d("connectWifi ${wifiNetworkSuggestion.ssid} result:${failureReason}")
            }
            var suggestion = WifiNetworkSuggestion.Builder()
                .setSsid(ssid)
                .setWpa2Passphrase(pwd)
                .setIsAppInteractionRequired(true)
                .build()
            val intentFilter = IntentFilter(WifiManager.ACTION_WIFI_NETWORK_SUGGESTION_POST_CONNECTION);

            val broadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    if (!intent.action.equals(WifiManager.ACTION_WIFI_NETWORK_SUGGESTION_POST_CONNECTION)) {
                        return;
                    }
                    // do post connect processing here
                    LogUtils.d("wifi suggestion receiver")
                }
            };
            context.registerReceiver(broadcastReceiver, intentFilter);
            var status = wifiManager?.addNetworkSuggestions(mutableListOf(suggestion))
            LogUtils.d("connectWifi status ${status}")

        }

    }

}
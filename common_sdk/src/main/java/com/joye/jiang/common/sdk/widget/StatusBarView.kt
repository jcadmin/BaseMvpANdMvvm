package com.joye.jiang.common.sdk.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.annotation.Keep
import androidx.databinding.DataBindingUtil
import com.apkfuns.logutils.LogUtils
import com.joye.jiang.common.sdk.DateFormatUtils
import com.joye.jiang.common.sdk.R
import com.joye.jiang.common.sdk.databinding.ViewStatusBarBinding
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import java.io.BufferedReader
import java.io.FileReader
import java.util.*
import java.util.concurrent.TimeUnit

@Keep
class StatusBarView : LinearLayout {

    private var binding: ViewStatusBarBinding? = null

    private var timerDisposable: io.reactivex.disposables.Disposable? = null

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
    }

    @SuppressLint("CheckResult")
    private fun initView(context: Context?) {
//        var view = LayoutInflater.from(context).inflate(R.layout.view_status_bar, this)
//        view.tag = "layout/view_status_bar_0"
//        binding = DataBindingUtil.bind(view)
//        cat thermal_zone0/temp
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.view_status_bar,
            this,
            true
        )
        Observable.interval(0, 1, TimeUnit.SECONDS)
            .doOnSubscribe {
                timerDisposable = it
            }
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                updateTime()
                updateCpuTemp()
                cpuFreq()
            }
            .retry()
            .subscribe({

            }, {
                it.printStackTrace()
            })

    }

    private fun updateTime() {
        var time = System.currentTimeMillis()
        binding?.tvStatusBarTime?.text = DateFormatUtils.format(time, "HH:mm")
        binding?.tvStatusBarDay?.text = DateFormatUtils.format(time, "yyyy/MM/dd")
        binding?.tvStatusBarWeek?.text = DateFormatUtils.getWeekOfDate(Date(time))
    }

    @SuppressLint("SetTextI18n")
    private fun updateCpuTemp() {
        try {
            var fr = FileReader("/sys/class/thermal/thermal_zone0/temp")
            var br = BufferedReader(fr)
            var text = br.readLine()
//            LogUtils.d("cpu 温度${text}")
            if (text.isNotEmpty()) {
                binding?.tvCpuTemp?.text = "${text.toLong().div(1000)}℃"
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun cpuFreq() {
        try {
            var fr = FileReader("/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq")
            var br = BufferedReader(fr)
            var text = br.readLine()
//            LogUtils.d("cpu 当前频率${text}")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        timerDisposable?.dispose()
        timerDisposable = null
    }
}
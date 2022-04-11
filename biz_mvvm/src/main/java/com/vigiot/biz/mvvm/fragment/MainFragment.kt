package com.vigiot.biz.mvvm.fragment

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.alibaba.android.arouter.facade.annotation.Route
import com.apkfuns.logutils.LogUtils
import com.jakewharton.rxbinding4.view.clicks
import com.joye.jiang.common.base.activity.BaseFragment
import com.joye.jiang.common.data.router.MvvmRouterConstants
import com.joye.jiang.common.sdk.extension.launchAndRepeatWithViewLifecycle
import com.vigiot.biz.mvvm.R
import com.vigiot.biz.mvvm.databinding.MvvmFragmentMainBinding
import com.vigiot.biz.mvvm.viewModel.MainViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Route(path = MvvmRouterConstants.FRAGMENT_MAIN)
class MainFragment : BaseFragment<MvvmFragmentMainBinding>() {

    private val mainViewModel by lazy { ViewModelProvider(requireActivity())[MainViewModel::class.java] }

    override fun initLayoutId(): Int {
        return R.layout.mvvm_fragment_main
    }

    override fun initView() {
        binding?.tvLiveData?.clicks()?.subscribe({
            mainViewModel.liveDataUpdateTips()
        }, {
            it.printStackTrace()
        })
        binding?.tvSharedFlow?.clicks()?.subscribe({
            mainViewModel.sharedFlowUpdateTips()
        }, {
            it.printStackTrace()
        })
    }

    override fun initData() {
        mainViewModel.tipsData.observe(viewLifecycleOwner) {
            binding?.tvTips?.text = it
        }
        launchAndRepeatWithViewLifecycle {
            mainViewModel.tipsSharedFlow.collect {
                binding?.tvTips?.text = it
            }
        }

        launchAndRepeatWithViewLifecycle {
            mainViewModel.tipsSharedFlow.collect {
                binding?.tvTips?.text = it
            }
        }
    }
}
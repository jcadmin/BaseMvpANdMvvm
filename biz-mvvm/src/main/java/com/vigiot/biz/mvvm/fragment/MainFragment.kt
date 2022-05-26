package com.vigiot.biz.mvvm.fragment

import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.jakewharton.rxbinding4.view.clicks
import com.joye.jiang.common.base.activity.BaseFragment
import com.joey.jiang.common.router.router.MvvmRouterConstants
import com.joye.jiang.common.sdk.extension.launchAndRepeatWithViewLifecycle
import com.vigiot.biz.mvvm.R
import com.vigiot.biz.mvvm.databinding.MvvmFragmentMainBinding
import com.vigiot.biz.mvvm.viewModel.MainViewModel
import kotlinx.coroutines.flow.collect

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

        launchAndRepeatWithViewLifecycle {
            mainViewModel.userInfo.collect {
                binding?.tvName?.text = it.name
            }
        }

        mainViewModel.loadUserInfo(5)
    }
}
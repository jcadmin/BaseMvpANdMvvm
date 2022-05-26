package com.vigiot.biz.mvvm.viewModel

import androidx.lifecycle.*
import com.apkfuns.logutils.LogUtils
import com.joey.jiang.common.usecase.LoadUserInfoUserCase
import com.joye.jiang.common.data.entity.UserInfo
import com.joye.jiang.common.sdk.extension.io2main
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.internal.notify

class MainViewModel : ViewModel() {

    var tipsData = MutableLiveData<String>()
        private set
    
    var tipsSharedFlow = MutableSharedFlow<String>()
        private set

    var userInfo = MutableSharedFlow<UserInfo>()
        private set

    private val loadUserInfoUserCase by lazy { LoadUserInfoUserCase() }

    fun liveDataUpdateTips() {
        viewModelScope
        tipsData.postValue("mvvm页面修改提示 LiveData")
    }

    fun sharedFlowUpdateTips() {
        viewModelScope.launch(Dispatchers.Main) {
            tipsSharedFlow.emit("mvvm页面修改提示 shared")
        }

    }

    fun loadUserInfo(id: Int) {
        loadUserInfoUserCase.loadUserInfo(id)
            .io2main()
            .subscribe({
                viewModelScope.launch(Dispatchers.Main) {
                    userInfo.emit(it)
                }
            }, {
                LogUtils.e(it)
            })
    }
}
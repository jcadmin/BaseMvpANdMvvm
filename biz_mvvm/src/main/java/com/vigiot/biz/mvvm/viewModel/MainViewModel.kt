package com.vigiot.biz.mvvm.viewModel

import androidx.lifecycle.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.internal.notify

class MainViewModel : ViewModel() {

    var tipsData = MutableLiveData<String>()

    var tipsSharedFlow = MutableSharedFlow<String>()
    
    fun liveDataUpdateTips() {
        tipsData.postValue("mvvm页面修改提示 LiveData")
    }

    fun sharedFlowUpdateTips() {
        viewModelScope.launch {
            tipsSharedFlow.emit("mvvm页面修改提示 shared")
        }

    }
}
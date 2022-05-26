package com.joey.jiang.common.usecase

import com.joye.jiang.common.data.entity.UserInfo
import com.joye.jiang.common.data.repository.LoadUserInfoRepository
import com.joye.jiang.common.data.repository.LoadUserInfoRepositoryImpl
import io.reactivex.rxjava3.core.Observable

class LoadUserInfoUserCase(var loadUserInfoRepository: LoadUserInfoRepository = LoadUserInfoRepositoryImpl()) {

    fun loadUserInfo(id: Int): Observable<UserInfo> {
        var hasNetWork = true
        if (hasNetWork) {
            return loadUserInfoRepository.loadUserInfo(id)
        } else {
            return Observable.just(UserInfo("name 无网络"))
        }
    }
}
package com.joey.jiang.common.usecase

import com.joye.jiang.common.data.entity.UserInfo
import com.joye.jiang.common.data.repository.LoadUserInfoRepository
import com.joye.jiang.common.data.repository.LoadUserInfoRepositoryImpl
import io.reactivex.rxjava3.core.Observable

class UserInfoUserCase(var loadUserInfoRepository: LoadUserInfoRepository = LoadUserInfoRepositoryImpl()) {

    fun loadUserInfo(id: Int): Observable<UserInfo> {
        return loadUserInfoRepository.loadUserInfo(id)
    }
}
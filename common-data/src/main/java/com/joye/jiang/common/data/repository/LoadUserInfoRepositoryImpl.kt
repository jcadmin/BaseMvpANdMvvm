package com.joye.jiang.common.data.repository

import com.joye.jiang.common.data.entity.UserInfo
import com.joye.jiang.common.sdk.http.ServerException
import io.reactivex.rxjava3.core.Observable

class LoadUserInfoRepositoryImpl : LoadUserInfoRepository {

    override fun loadUserInfo(id: Int): Observable<UserInfo> {
        return Observable.just(id)
            .map {
                if (id == 1) {
                    throw ServerException(500, "用户不存在", null)
                } else {
                    UserInfo("name")
                }
            }
    }
}
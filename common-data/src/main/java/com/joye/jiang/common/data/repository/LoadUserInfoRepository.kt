package com.joye.jiang.common.data.repository

import com.joye.jiang.common.data.entity.UserInfo
import io.reactivex.rxjava3.core.Observable


interface LoadUserInfoRepository {
    
    fun loadUserInfo(id:Int): Observable<UserInfo>
}
package me.taosunkist.hello.data.net.module

import io.reactivex.Single
import me.taosunkist.hello.data.net.ServerApi
import me.taosunkist.hello.data.net.ServerApiCore
import me.taosunkist.hello.data.net.model.ApiResponse
import me.taosunkist.hello.data.net.model.UserInfo

class UserService {
    companion object{
        val shared = UserService()
    }

    fun fetchUserInfo(): Single<ApiResponse<UserInfo>> {
       return ServerApiCore.serverApi.fetchUserInfo()
    }
}
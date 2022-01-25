package me.taosunkist.hello.data.net

import io.reactivex.Single
import me.taosunkist.hello.data.net.model.ApiResponse
import me.taosunkist.hello.data.net.model.UserInfo
import retrofit2.http.GET

interface ServerApi {

    @GsonConverter("app")
    @GET("v1/user/info")
    fun fetchUserInfo(): Single<ApiResponse<UserInfo>>
}

package me.taosunkist.hello.data.net.module

import androidx.test.core.app.ActivityScenario.launch
import io.reactivex.Single
import kotlinx.coroutines.*
import me.taosunkist.hello.data.UserStore
import me.taosunkist.hello.data.model.User
import me.taosunkist.hello.data.net.ServerApi
import me.taosunkist.hello.data.net.ServerApiCore
import me.taosunkist.hello.data.net.model.ApiResponse
import me.taosunkist.hello.data.net.model.UserDetails
import kotlin.concurrent.thread
import kotlin.time.Duration

class UserService {

    companion object {
        val shared = UserService()
    }

    private var coroutineScope: CoroutineScope = CoroutineScope(Job() + Dispatchers.IO)


    fun fetchUserInfo(): Single<ApiResponse<User>> {
        return ServerApiCore.serverApi.fetchUserInfo()
    }

    suspend fun fetchUserDetails(): ApiResponse<UserDetails> {
        delay(1500L)
        return ApiResponse.success(UserDetails.fake(UserStore.shared.user!!))
    }
}
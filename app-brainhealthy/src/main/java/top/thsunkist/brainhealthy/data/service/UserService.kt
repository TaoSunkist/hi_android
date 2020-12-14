package top.thsunkist.brainhealthy.data.service

import top.thsunkist.brainhealthy.data.model.*
import top.thsunkist.brainhealthy.network.ServerApiCore
import top.thsunkist.brainhealthy.network.model.*
import io.reactivex.Single

class UserService {


	companion object {
		val shared: UserService = UserService()
	}

	fun hello(): Single<ApiResponse<Any>> = ServerApiCore.serverApi.hello()

	fun fetchUserDetailInfo(userFake: Boolean = true): Single<User> {
		return Single.create {
			Thread.sleep(500)
			it.onSuccess(User.fake())
		}
	}

}
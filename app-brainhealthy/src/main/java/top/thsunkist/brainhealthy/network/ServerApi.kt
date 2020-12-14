package top.thsunkist.brainhealthy.network

import top.thsunkist.brainhealthy.data.model.*
import top.thsunkist.brainhealthy.network.model.*
import io.reactivex.Single
import retrofit2.http.*

interface ServerApi {

	@FormUrlEncoded
	@POST("/v1/user/login")
	fun login(
		@Field("thirdPartyId") userId: String,
		@Field("thirdPartyType") type: Int,
		@Field("thirdPartyToken") token: String,
		@Field("sex") sex: Int?,
		@Field("avatar") avatar: String?,
		@Field("nick") nickname: String?,
	): Single<ApiResponse<UserResponse>>

	@GET("/api/hello")
	fun hello(): Single<ApiResponse<Any>>
}

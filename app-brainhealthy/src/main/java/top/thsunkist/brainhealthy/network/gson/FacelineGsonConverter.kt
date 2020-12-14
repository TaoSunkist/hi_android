package top.thsunkist.brainhealthy.network.gson

import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import top.thsunkist.brainhealthy.data.UserStore
import top.thsunkist.brainhealthy.network.interceptors.NextVideoException
import top.thsunkist.brainhealthy.network.interceptors.NextVideoException.Companion.BLOCKED
import top.thsunkist.brainhealthy.network.interceptors.NextVideoException.Companion.FORCE_LOGOUT
import top.thsunkist.brainhealthy.network.model.ApiResponse
import okhttp3.ResponseBody
import retrofit2.Converter
import top.thsunkist.brainhealthy.BrainhealthyApplication
import java.io.IOException

class FacelineGsonConverter<T> internal constructor(private val gson: Gson, private val typeToken: TypeToken<*>) :
	Converter<ResponseBody, T?> {

	@Throws(IOException::class)
	override fun convert(value: ResponseBody): T? {

		val rawJson = value.string()
		return try {

			val apiResponse = BrainhealthyApplication.GSON.fromJson(rawJson, ApiResponse::class.java)

			when (apiResponse.code) {
				FORCE_LOGOUT -> {
					UserStore.shared.logout()
					throw NextVideoException(code = apiResponse.code, errorMessage = apiResponse.message)
				}
				BLOCKED -> {
					UserStore.shared.logout()
					throw NextVideoException(code = apiResponse.code, errorMessage = apiResponse.message, bundle = Bundle().apply {
						putLong(NextVideoException.keyOfBlockedExpireTime, System.currentTimeMillis())
					})
				}
				200 -> {
					gson.fromJson<T>(rawJson, typeToken.type)
				}
				else -> {
					throw NextVideoException(code = apiResponse.code, errorMessage = apiResponse.message)
				}
			}
		} catch (e: Exception) {
			throw  e
		}
	}
}
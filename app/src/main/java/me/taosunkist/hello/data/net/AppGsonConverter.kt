package me.taosunkist.hello.data.net

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import me.taosunkist.hello.HiApplication
import me.taosunkist.hello.data.net.model.ApiResponse
import okhttp3.ResponseBody
import retrofit2.Converter
import java.io.IOException

class AppGsonConverter<T> internal constructor(private val gson: Gson, private val typeToken: TypeToken<*>) :
    Converter<ResponseBody, T?> {

    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T? {

        val rawJson = value.string()
        return try {

            val apiResponse = HiApplication.GSON.fromJson(rawJson, ApiResponse::class.java)

            when (apiResponse.code) {

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
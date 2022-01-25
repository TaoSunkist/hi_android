package me.taosunkist.hello.data.net

import com.google.gson.Gson
import okhttp3.RequestBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * 定义一个用于处理
 */
class AppGsonConverterFactory private constructor(val gson: Gson) : Converter.Factory() {


    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        return null
    }

    companion object {
        @JvmOverloads
        fun create(gson: Gson): AppGsonConverterFactory {
            return AppGsonConverterFactory(gson)
        }
    }

}

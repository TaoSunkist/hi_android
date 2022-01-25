package me.taosunkist.hello.data.net

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.RequestBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type
/**
 * 定义一个用于处理
 */
class AppGsonConverterFactory private constructor(val gson: Gson) : Converter.Factory() {


    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): AppGsonConverter<Any> {
        return AppGsonConverter(gson, TypeToken.get(type))
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return null
    }

    companion object {
        @JvmOverloads
        fun create(gson: Gson): AppGsonConverterFactory {
            return AppGsonConverterFactory(gson)
        }
    }

}

package me.taosunkist.hello.data.net

import com.google.gson.Gson
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

/* use differ json-converter, parse different of json-structure 使用不同的json-converter，解析不同的json-structure */
class GsonConverterFactoryConstructer private constructor(val gson: Gson) : Converter.Factory() {

    private val appGsonConverterFactory = AppGsonConverterFactory.create(gson)

    private val gsonConverterFactory: GsonConverterFactory = GsonConverterFactory.create(gson)

    override fun responseBodyConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit): Converter<ResponseBody, *>? {

        for (annotation in annotations) {
            if ((annotation is GsonConverter) && annotation.value == GsonConverter.COMMON) {
                return gsonConverterFactory.responseBodyConverter(type, annotations, retrofit)
            }
        }
        return appGsonConverterFactory.responseBodyConverter(type, annotations, retrofit)
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit,
    ): Converter<*, RequestBody>? {
        return null
    }

    companion object {

        @JvmOverloads
        fun create(gson: Gson): GsonConverterFactoryConstructer {

            return GsonConverterFactoryConstructer(gson)
        }
    }
}

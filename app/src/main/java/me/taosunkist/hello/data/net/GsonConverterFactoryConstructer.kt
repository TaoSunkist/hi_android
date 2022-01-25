package me.taosunkist.hello.data.net

import com.google.gson.reflect.TypeToken
import com.google.gson.Gson
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

/* use differ json-converter, parse different of json-structure 使用不同的json-converter，解析不同的json-structure */
/**
 * 定义两种不同解析数据格式，根据注解进行不同的数据格式解析
 * 根据不同的json对应不同的解析
 */
class GsonConverterFactoryConstructer private constructor(val gson: Gson) : Converter.Factory() {

    private val facelineGsonConverterFactory = AppGsonConverterFactory.create(gson)

    private val gsonConverterFactory: GsonConverterFactory = GsonConverterFactory.create(gson)
    //定义两种不同解析数据格式，根据注解进行不同的数据格式解析
    override fun responseBodyConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit): Converter<ResponseBody, *>? {

        for (annotation in annotations) {
            if ((annotation is GsonConverter) && annotation.value == GsonConverter.COMMON) {
                return gsonConverterFactory.responseBodyConverter(type, annotations, retrofit)
            }
        }
        return facelineGsonConverterFactory.responseBodyConverter(type, annotations, retrofit)
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
        fun create(gson: Gson): GsonConverterFactoryConstructer {

            return GsonConverterFactoryConstructer(gson)
        }
    }
}

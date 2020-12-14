package top.thsunkist.brainhealthy.network.gson

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.RequestBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class FacelineGsonConverterFactory private constructor(val gson: Gson) : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): FacelineGsonConverter<Any> {
        return FacelineGsonConverter(gson, TypeToken.get(type))
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
        fun create(gson: Gson): FacelineGsonConverterFactory {
            return FacelineGsonConverterFactory(gson)
        }
    }

}

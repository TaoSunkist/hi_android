package top.thsunkist.brainhealthy.network.gson

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import top.thsunkist.brainhealthy.utilities.GsonConverter
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

/* use differ json-converter, parse different of json-structure */
class GsonConverterFactoryConstructer private constructor(val gson: Gson) : Converter.Factory() {

    private val facelineGsonConverterFactory = FacelineGsonConverterFactory.create(gson)

    private val gsonConverterFactory: GsonConverterFactory = GsonConverterFactory.create(gson)

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

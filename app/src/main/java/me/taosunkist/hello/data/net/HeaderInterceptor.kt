package me.taosunkist.hello.data.net

import android.os.Build
import me.taosunkist.hello.data.UserStore
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class HeaderInterceptor : Interceptor {

    companion object {
        const val SIGNATURE = "signature"
        const val TIMESTAMP = "timestamp"
        const val CONTENT_TYPE = "Content-Type"
        const val NONCE = "nonce"
        const val TOKEN = "token"
        const val DEVICE_TYPE = "deviceType"
        const val DEVICE_VERSION = "deviceVersion"
        const val ANDROID = "android"
        const val KEY: String = "biz"
        const val IMEI: String = "imei"
        const val APP_VERSION = "appVersion"
        const val BITS = "BITS"
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val builder = request.newBuilder()

        val newRequest = builder.build()
        val response = chain.proceed(newRequest)

        return response
    }
}

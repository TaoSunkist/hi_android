package top.thsunkist.brainhealthy.network.interceptors

import android.os.Build
import top.thsunkist.brainhealthy.data.UserStore
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class HeaderInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val builder = request.newBuilder()

        /* TODO */
        builder
            .addHeader("Content-Type", "application/json")
            .addHeader("timestamp", (System.currentTimeMillis() / 1000).toString())
            .addHeader("nonce", (10000000..99999999).random().toString())
            .addHeader("token", UserStore.shared.authenticationToken ?: "")
            .addHeader("deviceType", "android")
            .addHeader("deviceVersion", Build.VERSION.SDK_INT.toString())

        val response = chain.proceed(builder.build())
        response.headers["token"]?.let { UserStore.shared.authenticationToken = it }

        return response
    }
}

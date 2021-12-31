package me.taosunkist.hello.data.net

import android.os.Build
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory
import com.qiahao.nextvideo.BuildConfig
import com.qiahao.nextvideo.data.UserStore
import com.qiahao.nextvideo.data.service.match.MatchingServiceLauncher
import com.qiahao.nextvideo.utilities.DecEncUtility
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
        val nonce = (10000000..99999999).random().toString()
        val timestampInSecs = (System.currentTimeMillis() / 1000).toString()
        val signature = DecEncUtility.sha1(timestampInSecs + KEY + nonce)
        builder.addHeader(CONTENT_TYPE, "application/json")
            .addHeader(TIMESTAMP, timestampInSecs)
            .addHeader(NONCE, nonce)
            .addHeader(SIGNATURE, signature)
            .addHeader(TOKEN, UserStore.shared.authenticationToken ?: "")
            .addHeader(DEVICE_TYPE, ANDROID)
            .addHeader(DEVICE_VERSION, Build.VERSION.SDK_INT.toString())
            .addHeader(APP_VERSION, BuildConfig.VERSION_NAME)
            .addHeader(name = BITS, value = MatchingServiceLauncher.matchingFloatViewStatusPool.values.joinToString(","))
        /** device id is variable */
        try {
            builder.addHeader(IMEI, PushServiceFactory.getCloudPushService().deviceId)
        } catch (t: Throwable) {
        }

        val newRequest = builder.build()
        val response = chain.proceed(newRequest)
        response.headers[TOKEN]?.let {
            if (it.isBlank().not()) {
                UserStore.shared.authenticationToken = it
            }
        }

        return response
    }
}

package me.taosunkist.hello.data.net

import android.os.Bundle
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class NextVideoException(val code: Int, val errorMessage: String?, var bundle: Bundle? = null) : java.lang.Exception() {

    companion object {

        const val TOKEN_EXPIRE = 1002

        const val TOKEN_INVALID = 1001

        const val BLOCKED = 9000

        const val keyOfBlockedExpireTime = "keyOfBlockedExpireTime"
    }

    override fun getLocalizedMessage(): String? = errorMessage
}

/**
 * 拦截器对应请求错误的处理
 */
internal class ErrorInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val response: Response = try {
            chain.proceed(chain.request())
        } catch (exception: IOException) {
            throw IOException("network failed.")
        }
        return if (response.code != 200) {
            throw NextVideoException(code = response.code, errorMessage = response.message)
        } else {
            response
        }
    }
}

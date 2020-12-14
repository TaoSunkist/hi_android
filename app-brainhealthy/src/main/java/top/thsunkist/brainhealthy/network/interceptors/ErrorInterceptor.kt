package top.thsunkist.brainhealthy.network.interceptors

import android.os.Bundle
import okhttp3.Interceptor
import okhttp3.Response


/*
TODO wait for update
封装了包含我们的服务器error json的IOException.
code 说明
200 提交正确，响应正确
1开头 服务器/通用态错误
10001 服务器发生内部错误
10002 字段校验错误，如：漏提交、未提交
10003 数据库错误

2开头 用户态错误
20001 无此用户
20002 密码错误

3开头 充值/支付态错误
30001

xxxxxxxx
 */
class NextVideoException(val code: Int, val errorMessage: String?, var bundle: Bundle? = null) : java.lang.Exception() {

    companion object {

        const val FORCE_LOGOUT = 10005

        const val APP_INTERNAL_ERR = 99999

        const val ARGUMENT_MISSING = 10002

        const val BLOCKED = 20005

        const val keyOfBlockedExpireTime = "keyOfBlockedExpireTime"
    }


    override fun getLocalizedMessage(): String? = errorMessage
}

internal class ErrorInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val response: Response = chain.proceed(request)
        /*try {
            response = chain.proceed(request)
        } catch (exception: IOException) {
            throw IOException(NextVideoApplication.CONTEXT.getString(R.string.the_network_error))
        }*/
        return if (response.code != 200) {
            throw NextVideoException(code = response.code, errorMessage = response.message)
        } else {
            response
        }
    }
}

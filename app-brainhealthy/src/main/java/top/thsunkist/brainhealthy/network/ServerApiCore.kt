package top.thsunkist.brainhealthy.network

import com.jakewharton.rxrelay2.BehaviorRelay
import top.thsunkist.brainhealthy.BuildConfig
import top.thsunkist.brainhealthy.BrainhealthyApplication
import top.thsunkist.brainhealthy.network.gson.GsonConverterFactoryConstructer
import top.thsunkist.brainhealthy.network.interceptors.ErrorInterceptor
import top.thsunkist.brainhealthy.network.interceptors.HeaderInterceptor
import top.thsunkist.brainhealthy.network.interceptors.LoggingInterceptor
import top.thsunkist.brainhealthy.network.model.ApiResponse
import top.thsunkist.brainhealthy.network.model.validate
import top.thsunkist.brainhealthy.utilities.MMKVID
import top.thsunkist.brainhealthy.utilities.getString
import top.thsunkist.brainhealthy.utilities.printf
import top.thsunkist.brainhealthy.utilities.putString
import com.tencent.mmkv.MMKV
import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

fun <T : Any> Single<ApiResponse<T>>.unwrap(validateResult: Boolean = true): Single<T> {
    return map {
        val data = it.data
        if (validateResult) {
            validate(obj = data)
        }
        if (it.isOk()) {
            data
        } else {
            throw IOException(it.message ?: "服务器数据异常")
        }
    }
}

fun <T : Any> ApiResponse<T>.unwrap(validateResult: Boolean = true): T? {
    val data = data
    if (validateResult) {
        validate(obj = data)
    }
    return if (isOk()) {
        data
    } else {
        throw IOException(message ?: "服务器数据异常")
    }
}


class ServerApiCore {

    companion object {

        private const val KEY_SELECTED_SERVER: String = "keySelectedServer"

        private const val TIMEOUT_DEBUG: Long = 10000

        private const val TIMEOUT_PROD: Long = 60000

        lateinit var sharedInstance: ServerApiCore

        val serverApi: ServerApi
            get() = sharedInstance.serverApi!!


        fun init() {
            sharedInstance = ServerApiCore()
        }
    }

    private var serverApi: ServerApi? = null

    val selectedServerKey = BehaviorRelay.createDefault(kProductionServer)

    val selectedServerInfo: ServerInfo
        get() = availableServers[selectedServerKey.value]!!

    init {

        var storedServerKey = if (BuildConfig.DEBUG.not()) {
            MMKV.defaultMMKV().getString(MMKVID.COMMON, KEY_SELECTED_SERVER, kProductionServer)
        } else {
            MMKV.defaultMMKV().getString(MMKVID.COMMON, KEY_SELECTED_SERVER, kTestServer)
        }

        if (BuildConfig.ALLOW_SERVER_SWITCH.not()) {
            storedServerKey = kProductionServer
        }

        selectedServerKey.accept(storedServerKey)
        setupServer(serverInfoInfo = availableServers[selectedServerKey.value]!!)
    }

    fun switchToServer(serverInfo: ServerInfo) {

        if (serverInfo.key == selectedServerKey.value) {
            return
        }

        selectedServerKey.accept(serverInfo.key)
        setupServer(serverInfo)
        MMKV.defaultMMKV().putString(MMKVID.COMMON, KEY_SELECTED_SERVER, selectedServerKey.value.toString())
    }

    private fun setupServer(serverInfoInfo: ServerInfo) {

        if (BuildConfig.DEBUG) {
            printf("========================")
            printf("Switch to Server: ${serverInfoInfo.key}")
            printf("Server URL      : ${serverInfoInfo.baseUrl}")
            printf("========================")
        }

        val timeout: Long = if (BuildConfig.DEBUG) TIMEOUT_DEBUG else TIMEOUT_PROD
        val clientBuilder = OkHttpClient.Builder()

        clientBuilder.addInterceptor(HeaderInterceptor())
            .addInterceptor(ErrorInterceptor())
            .connectTimeout(timeout, TimeUnit.MILLISECONDS)

        if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(LoggingInterceptor())
        }

        val okHttpClient = clientBuilder.build()
        val retrofit = Retrofit.Builder()
            .baseUrl(serverInfoInfo.baseUrl)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactoryConstructer.create(BrainhealthyApplication.GSON))
            .build()
        serverApi = retrofit.create(ServerApi::class.java)
    }
}
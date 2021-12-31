package me.taosunkist.hello.data.net

import com.jakewharton.rxrelay2.BehaviorRelay
import com.qiahao.nextvideo.HiloApplication
import com.qiahao.nextvideo.data.model.DownloadProgressModel
import com.qiahao.nextvideo.network.model.ApiResponse
import com.qiahao.nextvideo.utilities.printf
import com.tencent.mmkv.MMKV
import io.reactivex.Observable
import io.reactivex.Single
import me.taosunkist.hello.HiApplication
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import top.thsunkist.appkit.BuildConfig
import top.thsunkist.appkit.utility.printf
import java.io.*
import java.util.concurrent.TimeUnit

fun <T : Any> Single<ApiResponse<T>>.unwrap(validateResult: Boolean = false): Single<T> {
    return map {
        val data = it.data
        /*if (validateResult) {
            validate(obj = data)
        }*/
        if (it.isOk()) {
            data
        } else {
            throw IOException(it.message ?: "服务器数据异常")
        }
    }
}

/* TODO refactor */
fun <T : Any> Single<ApiResponse<T>>.unwrap(validateResult: Boolean = false, allowEmpty: Boolean = true): Single<T> {
    return map {
        val data = it.data
        /*if (validateResult) {
            validate(obj = data)
        }*/
        if (it.isOk()) {
            data
        } else {
            throw IOException(it.message ?: "服务器数据异常")
        }
    }
}

fun <T : Any> ApiResponse<T>.unwrap(validateResult: Boolean = false): T? {
    val data = data
    /*if (validateResult) {
        validate(obj = data)
    }*/
    return if (isOk()) {
        data
    } else {
        throw IOException(message ?: "服务器数据异常")
    }
}

fun <T : Any> ApiResponse<T>.unwrap(validateResult: Boolean = false, allowEmpty: Boolean = true): T? {
    val data = data
    /*if (validateResult) {
        validate(obj = data)
    }*/
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

        val sharedInstance: ServerApiCore = ServerApiCore()

        val serverApi: ServerApi
            get() = sharedInstance.serverApi!!

    }

    private var serverApi: ServerApi? = null

    private val selectedServerKey = BehaviorRelay.createDefault(kProductionServer)

    val selectedServerInfo: ServerInfo
        get() = availableServers[selectedServerKey.value]!!

    init {

        var storedServerKey = if (BuildConfig.DEBUG.not()) {
            MMKV.defaultMMKV().getString(KEY_SELECTED_SERVER, kProductionServer)
        } else {
            MMKV.defaultMMKV().getString(KEY_SELECTED_SERVER, kTestServer)
        }

        storedServerKey = kProductionServer

        selectedServerKey.accept(storedServerKey)
        setupServer(serverInfoInfo = availableServers[selectedServerKey.value]!!)
    }

    fun switchToServer(serverInfo: ServerInfo) {

        if (serverInfo.key == selectedServerKey.value) {
            return
        }

        selectedServerKey.accept(serverInfo.key)
        setupServer(serverInfo)
        MMKV.defaultMMKV().putString( KEY_SELECTED_SERVER, selectedServerKey.value.toString())
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
            .addInterceptor(ErrorInterceptor())//错误的拦截器
            .connectTimeout(timeout, TimeUnit.MILLISECONDS)

        if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(LoggingInterceptor())
        }

        val okHttpClient = clientBuilder.build()
        val retrofit = Retrofit.Builder()
            .baseUrl(serverInfoInfo.baseUrl)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactoryConstructer.create(HiApplication.GSON))//对应不同数据 使用不同的解释器
            .build()
        serverApi = retrofit.create(ServerApi::class.java)
    }

    /**
     * 下载某个url的内容到target.
     */
    fun download(url: String, target: File): Observable<DownloadProgressModel> {
        return Observable.create {

            val okHttpClient = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response: Response
            try {
                response = okHttpClient.newCall(request).execute()
            } catch (e: IOException) {
                if (it.isDisposed.not()) {
                    it.onError(e)
                }
                return@create
            }

            if (response.body == null) {
                it.onError(FileNotFoundException("download failed."))
                return@create
            }
            val ins = response.body?.byteStream()

            val input = BufferedInputStream(ins)
            val output = FileOutputStream(target)
            var shouldCancel = false

            it.setCancellable {
                shouldCancel = true
            }

            val data = ByteArray(2048)
            var total = 0
            var count = input.read(data)
            while (count != -1) {
                if (shouldCancel) {
                    break
                }
                val progress = total.toFloat() / (response.body?.contentLength() ?: 1).toFloat()
                it.onNext(DownloadProgressModel(progress = progress, file = null))
                total += count
                try {
                    output.write(data, 0, count)
                    count = input.read(data)
                } catch (e: IOException) {
                    if (it.isDisposed.not()) {
                        it.onError(e)
                    }
                    break
                }
            }

            try {
                output.flush()
                output.close()
                input.close()
                it.onNext(DownloadProgressModel(progress = 1f, file = target))
                it.onComplete()
            } catch (e: IOException) {
                if (it.isDisposed.not()) {
                    it.onError(e)
                }
            }
        }
    }
}
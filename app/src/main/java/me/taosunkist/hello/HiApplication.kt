package me.taosunkist.hello

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.multidex.MultiDexApplication
import com.google.gson.Gson
import com.mooveit.library.Fakeit
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import com.tencent.mmkv.MMKV
import io.reactivex.disposables.CompositeDisposable
import top.thsunkist.tatame.utilities.weak

class HiApplication : MultiDexApplication() {

    lateinit var refWatcher: RefWatcher

    companion object {

        private val TAG = HiApplication::class.java.name

        val GSON = Gson()

        val MAIN_HANDLER = Handler(Looper.getMainLooper())

        var CONTEXT: Context? by weak()

        var appCompositeDisposable = CompositeDisposable()
    }

    override fun onCreate() {
        super.onCreate()

        CONTEXT = this

        MMKV.initialize(this)
        Fakeit.init()
        setupLeakCanary()
    }

    private fun setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        refWatcher = LeakCanary.install(this)
    }
}
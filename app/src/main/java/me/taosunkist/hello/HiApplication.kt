package me.taosunkist.hello

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.multidex.MultiDexApplication
import com.google.gson.Gson
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.disposables.CompositeDisposable
import me.taosunkist.hello.utility.weak

/**
 * Created by sunkist on 2019-07-27
 */
class HiApplication : MultiDexApplication() {
	override fun onCreate() {
		super.onCreate()
	}

	companion object {
		private val TAG = HiApplication::class.java.name
		val GSON = Gson()
		val MAIN_HANDLER = Handler(Looper.getMainLooper())
		var CONTEXT: Context? by weak()

		/* 正在前台的Activity总数，如果 >0 就代表app在foreground */
		var activeActivityCount = BehaviorRelay.createDefault(0)
		var appCompositeDisposable = CompositeDisposable()
	}
}
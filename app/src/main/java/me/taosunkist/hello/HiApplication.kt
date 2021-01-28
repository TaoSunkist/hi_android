package me.taosunkist.hello

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.multidex.MultiDexApplication
import com.google.gson.Gson
import com.mooveit.library.Fakeit
import dagger.hilt.android.HiltAndroidApp
import io.reactivex.disposables.CompositeDisposable
import me.taosunkist.hello.utility.weak

/**
 * Created by sunkist on 2019-07-27
 */
@HiltAndroidApp
class HiApplication : MultiDexApplication() {
	override fun onCreate() {
		super.onCreate()
		Fakeit.init()
	}

	companion object {
		private val TAG = HiApplication::class.java.name
		val GSON = Gson()
		val MAIN_HANDLER = Handler(Looper.getMainLooper())
		var CONTEXT: Context? by weak()

		var appCompositeDisposable = CompositeDisposable()
	}
}
package top.thsunkist.brainhealthy

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import top.thsunkist.brainhealthy.network.ServerApiCore
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.mmkv.MMKV
import io.reactivex.disposables.CompositeDisposable

class BrainhealthyApplication : Application() {

	companion object {

		private val TAG = BrainhealthyApplication::class.java.name

		val GSON = Gson()

		lateinit var CONTEXT: Context

		val appCompositeDisposable = CompositeDisposable()
	}

	override fun onCreate() {
		super.onCreate()

		if (packageName == getProcessName()) {
			CONTEXT = applicationContext
			CrashReport.initCrashReport(this, CrashReport.UserStrategy(this))
			/* k-v store component */
			MMKV.initialize(this)
			/* user information cache */
//			UserStore.init(this)
			/* alicloud-push */
			/* app server api */
			ServerApiCore.init()
			/*initLeakCanary()*/
			/*RxJavaPlugins.setErrorHandler { throwable: Throwable ->
				throwable.printStackTrace()
			}*/
		}


	}

}
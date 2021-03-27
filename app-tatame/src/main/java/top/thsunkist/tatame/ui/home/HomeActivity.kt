package top.thsunkist.tatame.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityCompat
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import top.thsunkist.tatame.ui.reusable.viewcontroller.BaseActivity
import top.thsunkist.tatame.ui.reusable.viewcontroller.controller.NavigationController
import top.thsunkist.tatame.utilities.Dimens
import top.thsunkist.tatame.utilities.MainThread
import top.thsunkist.tatame.utilities.PermissionGranter
import top.thsunkist.tatame.utilities.weak

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class HomeActivity : BaseActivity(), PermissionGranter {

	private val compositeDisposable = CompositeDisposable()
	private var navigationController: NavigationController? by weak()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		Dimens.safeAreaRelay.firstElement().observeOn(MainThread)
			.subscribe {
				navigationController = NavigationController(root = HomeViewController()).apply {
					controllerWindow.setRootViewController(this)
				}
				/* 我们的Root永远都是Home，也就是说HomeVC要有能力处理用户没有登录的情况
				   这样的好处是如果有朝一日我们允许可以用户未登录状态下也能先看看内容，现在
				   如果用户没有登录，我们就直接盖上AuthenticationViewController */
			}.addTo(compositeDisposable = compositeDisposable)
	}

	override fun onDestroy() {
		super.onDestroy()
		compositeDisposable.clear()
	}

	/* Permission Granter */

	private var permissionHandler: Function1<Int, Unit>? = null
	private var permissionRequestCode = 217

	override fun requestPermission(permission: String, resultHandler: Function1<Int, Unit>) {
		if (this.permissionHandler != null) throw IllegalStateException("Cannot request another while pending")
		this.permissionHandler = resultHandler
		ActivityCompat.requestPermissions(
			this, arrayOf(permission),
			this.permissionRequestCode
		)
	}

	override fun onRequestPermissionsResult(
		requestCode: Int,
		permissions: Array<String>,
		grantResults: IntArray
	) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
		if (this.permissionHandler != null && requestCode == this.permissionRequestCode &&
			grantResults.size == 1
		) {
			this.permissionHandler!!.invoke(grantResults[0])
			this.permissionHandler = null
			this.permissionRequestCode++
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		/* 别删除这个,Umeng分享需要. */
	}
}

package top.thsunkist.tatame.ui.reusable.viewcontroller.controller

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import io.reactivex.disposables.CompositeDisposable
import top.thsunkist.tatame.utilities.Dimens

abstract class BaseViewController : ViewController(), LifecycleOwner {

	val compositeDisposable = CompositeDisposable()

	private lateinit var lifecycleRegistry: LifecycleRegistry

	override fun viewDidLoad(view: View) {
		super.viewDidLoad(view)
		lifecycleRegistry = LifecycleRegistry(this)
		lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
	}

	override fun viewWillAppear(animated: Boolean) {
		super.viewWillAppear(animated)
		lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
	}

	override fun viewDidAppear(animated: Boolean) {
		super.viewDidAppear(animated)
		lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
	}

	override fun viewWillDisappear(animated: Boolean) {
		super.viewWillDisappear(animated)
		lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
	}

	override fun viewDidDisappear(animated: Boolean) {
		super.viewDidDisappear(animated)
		lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
	}

	override fun viewWillUnload(view: View) {
		super.viewWillUnload(view)
		this.compositeDisposable.clear()
		lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
	}

	override fun getLifecycle(): Lifecycle {
		return lifecycleRegistry
	}

	fun fitSafeArea(contentView: View) {
		contentView.setPadding(0, Dimens.safeArea.top, 0, Dimens.safeArea.bottom)
	}

	fun showProgressDialog(message: String) {
		activity.showProgressDialog(message = message)
	}

	fun showProgressDialog(messageRes: Int) {
		activity.showProgressDialog(messageRes = messageRes)
	}

	fun dismissProgressDialog(animated: Boolean = true, completion: (() -> Unit)? = null) {
		activity.dismissProgressDialog(animated = animated, completion = completion)
	}


}
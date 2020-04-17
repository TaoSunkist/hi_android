package me.taosunkist.hello.ui.reusable.viewcontroller.controller

import android.view.View
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import me.taosunkist.hello.Dimens

abstract class BaseViewController : ViewController() {

    val compositeDisposable = CompositeDisposable()

    override fun viewWillUnload(view: View) {
        super.viewWillUnload(view)
        this.compositeDisposable.clear()
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
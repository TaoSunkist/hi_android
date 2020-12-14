package top.thsunkist.brainhealthy.ui.reusable.viewcontroller.controller

import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import top.thsunkist.brainhealthy.BrainhealthyApplication
import top.thsunkist.brainhealthy.ui.reusable.viewcontroller.BaseActivity
import io.reactivex.disposables.CompositeDisposable

fun BaseViewController.getString(@StringRes stringRes: Int, vararg args: Any?): String {
    return BrainhealthyApplication.CONTEXT.getString(stringRes, *args)
}

abstract class BaseViewController : ViewController() {

    val compositeDisposable = CompositeDisposable()

    override fun doCreateView(container: ViewGroup, activity: BaseActivity) {
        super.doCreateView(container, activity)
    }

    override fun viewWillUnload(view: View) {
        super.viewWillUnload(view)
        this.compositeDisposable.clear()
    }

    fun showProgressDialog(message: String) {
        requireActivity.showProgressDialog(message = message)
    }

    fun showProgressDialog(messageRes: Int) {
        requireActivity.showProgressDialog(messageRes = messageRes)
    }

    fun dismissProgressDialog() {
        requireActivity.dismissProgressDialog()
    }
}
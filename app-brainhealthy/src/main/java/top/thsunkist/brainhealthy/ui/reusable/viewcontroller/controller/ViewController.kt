package top.thsunkist.brainhealthy.ui.reusable.viewcontroller.controller

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleObserver
import top.thsunkist.brainhealthy.ui.reusable.viewcontroller.BaseActivity
import top.thsunkist.brainhealthy.ui.reusable.viewcontroller.presentation.PresentationStyle
import top.thsunkist.brainhealthy.utilities.weak

abstract class ViewController : LifecycleObserver {

    companion object {
        var verbose: Boolean = true
    }

    var navigationController: NavigationController? by weak()

    private var _view: View? = null
    val view: View
        get() = _view!!

    private var _activity: BaseActivity? by weak()

    val requireActivity: BaseActivity
        get() = _activity!!

    val requireContext: Context
        get() = _activity!!

    private var _isVisible: Boolean = false
    val isVisible: Boolean
        get() = _isVisible

    var presentationStyle = PresentationStyle()

    /* optmize this*/
    var tag: String? = null

    val res: Resources
        get() = _activity!!.resources

    /* Child must override */
    abstract fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View

    open fun viewDidLoad(view: View) {
        if (verbose) {
            Log.i("oneHook", "${javaClass.simpleName} ($tag) VIEW DID LOAD")
        }
        _activity?.lifecycle?.addObserver(this)
    }

    open fun viewWillUnload(view: View) {
        if (verbose) {
            Log.i("oneHook", "${javaClass.simpleName} ($tag) VIEW WILL UNLOAD")
        }
    }

    open fun viewWillAppear(animated: Boolean) {
        if (verbose) {
            Log.i(
                "oneHook",
                "${javaClass.simpleName} ($tag) VIEW WILL APPEAR (${view.measuredWidth}, ${view.measuredHeight})"
            )
        }
    }

    open fun viewDidAppear(animated: Boolean) {
        _isVisible = true
        if (verbose) {
            Log.i(
                "oneHook",
                "${javaClass.simpleName} ($tag) VIEW DID APPEAR (${view.measuredWidth}, ${view.measuredHeight})"
            )
        }
    }

    open fun viewWillDisappear(animated: Boolean) {
        if (verbose) {
            Log.i("oneHook", "${javaClass.simpleName} ($tag) VIEW WILL DISAPPEAR")
        }
    }

    open fun viewDidDisappear(animated: Boolean) {
        _isVisible = false
        if (verbose) {
            Log.i("oneHook", "${javaClass.simpleName} ($tag) VIEW DID DISAPPEAR")
        }
    }

    /* Internal Methods */
    open fun doCreateView(container: ViewGroup, activity: BaseActivity) {
        _activity = activity
        _view = onCreateView(LayoutInflater.from(container.context), container)
        viewDidLoad(view)
        if (verbose) {
            Log.i("oneHook", "${javaClass.simpleName} ($tag) CREATE VIEW")
        }
    }

    open fun doDestroyView(container: ViewGroup) {
        _activity?.lifecycle?.removeObserver(this)
        this.viewWillUnload(view)
        container.removeView(view)
        _activity = null
        _view = null
        navigationController = null

        if (verbose) {
            Log.i("oneHook", "${javaClass.simpleName} ($tag) DESTROY VIEW")
        }
    }

    /**
     * If return true, means view controller would like to handle this
     * by itself.
     */
    open fun onBackPressed(): Boolean {
        return false
    }

    /* Public methods */
    open fun present(
        viewController: ViewController,
        animated: Boolean,
        completion: (() -> Unit)? = null
    ) {
        requireActivity.controllerWindow.push(
            viewController = viewController,
            activity = requireActivity,
            animated = animated,
            completion = completion
        )
    }

    open fun dismiss(animated: Boolean, completion: (() -> Unit)? = null) {
        requireActivity.controllerWindow.pop(animated = animated, completion = completion)
    }

    /* 是否自己处理statusBar */
    open fun processStatusBarCover(): Int {
        return requireActivity.statusBarHeight
    }

    open fun onNewIntent(newIntent: Intent) {}
}
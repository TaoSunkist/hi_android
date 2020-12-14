package top.thsunkist.brainhealthy.ui.reusable.viewcontroller.controller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import top.thsunkist.brainhealthy.ui.reusable.viewcontroller.host.ControllerHost

open class NavigationController(var root: ViewController) : ViewController() {

    private var controllerHost: ControllerHost? = null

    private var pendingRoot: ViewController? = root

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        controllerHost = ControllerHost(activity = requireActivity)
        return controllerHost!!
    }

    override fun viewWillAppear(animated: Boolean) {
        super.viewWillAppear(animated)

        if (pendingRoot != null) {
            pendingRoot!!.navigationController = this
            controllerHost!!.push(viewController = pendingRoot!!, activity = requireActivity, animated = false, completion = null)
            pendingRoot = null
        }
    }

    override fun doDestroyView(container: ViewGroup) {
        controllerHost?.popAll()
        root.navigationController = null
        super.doDestroyView(container)
    }

    fun push(viewController: ViewController, animated: Boolean) {
        viewController.navigationController = this
        controllerHost?.push(viewController = viewController, activity = requireActivity, animated = animated, completion = null)
    }

    fun popViewController(animated: Boolean) {
        controllerHost?.pop(animated, completion = null)
    }

    override fun onBackPressed(): Boolean {
        return controllerHost?.onBackPressed() ?: false
    }
}
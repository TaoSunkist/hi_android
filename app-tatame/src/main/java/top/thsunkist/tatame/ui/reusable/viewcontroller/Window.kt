package top.thsunkist.tatame.ui.reusable.viewcontroller

import top.thsunkist.tatame.ui.reusable.viewcontroller.controller.ViewController
import top.thsunkist.tatame.ui.reusable.viewcontroller.host.ControllerHost
import top.thsunkist.tatame.ui.reusable.viewcontroller.presentation.PresentingAnimation

class Window(activity: BaseActivity) : ControllerHost(activity = activity) {

    fun setRootViewController(
        controller: ViewController
    ) {
        if (activity == null) {
            return
        }
        if (topViewController == null) {
            push(
                viewController = controller,
                activity = activity!!,
                animated = false,
                completion = null
            )
        } else if (topViewController?.tag != controller.tag) {
            popAllButTop()
            topViewController?.presentationStyle?.animation = PresentingAnimation.BOTTOM
            addBottom(viewController = controller, activity = activity!!)
            pop(animated = true, completion = null)
        }
    }
}
package top.thsunkist.brainhealthy.ui.reusable.viewcontroller.controller

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import top.thsunkist.brainhealthy.databinding.DialogStyledBinding

abstract class BaseStyledDialogViewController : BaseViewController() {

    internal lateinit var binding: DialogStyledBinding

    init {
        presentationStyle.apply {
            fullscreen = false
            gravity = Gravity.CENTER
        }
    }


    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {

        return DialogStyledBinding.inflate(inflater, container, false).apply {
            binding = this
            binding.commonIdContentContainer.addView(
                onCreateContentView(inflater, binding.commonIdContentContainer)
            )
        }.root
    }

    abstract fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup): View

    protected fun addBottomNavigationButton(button: ImageButton) {
        binding.commonIdButtonContainer.addView(button)
    }
}
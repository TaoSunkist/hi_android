package top.thsunkist.tatame.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import top.thsunkist.tatame.R
import top.thsunkist.tatame.databinding.ViewControllerHomeBinding
import top.thsunkist.tatame.ui.giftbox.GiftBoxViewController
import top.thsunkist.tatame.ui.reusable.viewcontroller.controller.BaseViewController

class HomeViewController : BaseViewController() {

    init {
        tag = "home"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val binding: ViewControllerHomeBinding = DataBindingUtil.inflate(inflater, R.layout.view_controller_home, container, false)
        return binding.also { it.viewControllerGiftboxuiButton.setOnClickListener { launchGiftBoxUI() } }.root
    }

    private fun launchGiftBoxUI() = present(viewController = GiftBoxViewController(), animated = true)
}
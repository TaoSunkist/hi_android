package me.taosunkist.hello.ui.controller.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import me.taosunkist.hello.R
import me.taosunkist.hello.databinding.ViewControllerHomeBinding
import me.taosunkist.hello.ui.controller.databinding.DatabindingViewController
import me.taosunkist.hello.ui.controller.giftbox.GiftBoxViewController
import me.taosunkist.hello.ui.reusable.viewcontroller.controller.BaseViewController

class HomeViewController : BaseViewController() {

	init {
		tag = "home"
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
		val binding: ViewControllerHomeBinding = DataBindingUtil.inflate(inflater, R.layout.view_controller_home, container, false)
		return binding.also {
			it.viewControllerDatabindinguiButton.setOnClickListener { launchDatabindingUI() }
			it.viewControllerGiftboxuiButton.setOnClickListener { launchGiftBoxUI() }
		}.root
	}

	private fun launchGiftBoxUI() = present(viewController = GiftBoxViewController(), animated = true)
	private fun launchDatabindingUI() = present(viewController = DatabindingViewController(), animated = true)
}
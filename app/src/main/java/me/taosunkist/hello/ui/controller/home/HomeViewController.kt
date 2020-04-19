package me.taosunkist.hello.ui.controller.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.taosunkist.hello.R
import me.taosunkist.hello.ui.controller.databinding.DatabindingViewController
import me.taosunkist.hello.ui.reusable.viewcontroller.controller.BaseViewController

class HomeViewController : BaseViewController() {

	init {
		tag = "home"
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
		return inflater.inflate(R.layout.view_controller_home, container, false).apply {
			this.findViewById<View>(R.id.view_controller_databindviewcontroller_appcompatbutton).setOnClickListener { launchDatabindingUI() }
		}
	}

	private fun launchDatabindingUI() {
		present(viewController = DatabindingViewController(), animated = true)
	}
}
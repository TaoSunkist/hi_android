package me.taosunkist.hello.ui.controller.databinding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import io.reactivex.rxkotlin.addTo
import me.taosunkist.hello.R
import me.taosunkist.hello.databinding.ViewControllerDatabindingBinding
import me.taosunkist.hello.ui.reusable.viewcontroller.controller.BaseViewController
import me.taosunkist.hello.utility.MainThread
import java.util.concurrent.TimeUnit

data class DatabindingUIModel(var interval: Long = 0)

class DatabindingViewController : BaseViewController() {

	companion object {
		const val TAG = "databinding"
	}

	init {
		presentationStyle.apply {
			tag = TAG
		}
	}

	lateinit var binding: ViewControllerDatabindingBinding

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
		binding = DataBindingUtil.inflate(inflater, R.layout.view_controller_databinding, container, false)
		return binding.root
	}

	override fun viewDidLoad(view: View) {
		super.viewDidLoad(view)
		io.reactivex.Observable.interval(1, TimeUnit.SECONDS).observeOn(MainThread).subscribe {
			println("$it")
		}.addTo(compositeDisposable = compositeDisposable)
	}
}
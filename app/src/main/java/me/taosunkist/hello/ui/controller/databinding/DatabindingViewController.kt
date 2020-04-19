package me.taosunkist.hello.ui.controller.databinding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.addTo
import me.taosunkist.hello.DataBinderMapperImpl
import me.taosunkist.hello.R
import me.taosunkist.hello.databinding.ViewControllerDatabindingBinding
import me.taosunkist.hello.ui.reusable.viewcontroller.controller.BaseViewController
import me.taosunkist.hello.ui.reusable.viewcontroller.controller.ViewController
import me.taosunkist.hello.utility.MainThread
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

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
		binding.uiModel = DatabindingUIModel(interval = 1)
		return binding.root
	}

	override fun viewDidLoad(view: View) {
		super.viewDidLoad(view)
		binding.viewControllerDatabindIntervalTextview
		io.reactivex.Observable.interval(1, TimeUnit.SECONDS).observeOn(MainThread).subscribe {
			binding.uiModel!!.interval = it
			println("$it")
		}.addTo(compositeDisposable = compositeDisposable)
	}
}
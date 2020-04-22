package me.taosunkist.hello.ui.controller.giftbox

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import com.mooveit.library.Fakeit
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.addTo
import me.taosunkist.hello.R
import me.taosunkist.hello.databinding.ViewControllerDatabindingBinding
import me.taosunkist.hello.databinding.ViewControllerGiftBoxBinding
import me.taosunkist.hello.ui.controller.giftbox.model.OpenBoxResultUIModel
import me.taosunkist.hello.ui.reusable.viewcontroller.controller.BaseViewController
import me.taosunkist.hello.ui.reusable.viewcontroller.controller.ViewController
import me.taosunkist.hello.utility.MainThread
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

data class GiftBoxUIModel(val name: String) {
	companion object {
		fun fake(): GiftBoxUIModel {
			return GiftBoxUIModel(name = Fakeit.book().title())
		}
	}
}

class GiftBoxViewController : BaseViewController(), OpenBoxResultViewControllerDelegate {

	companion object {
		const val TAG = "giftbox"
	}

	init {
		presentationStyle.apply {
			tag = TAG
		}
	}

	lateinit var binding: ViewControllerGiftBoxBinding

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
		binding = DataBindingUtil.inflate(inflater, R.layout.view_controller_gift_box, container, false)
		binding.uiModel = GiftBoxUIModel.fake()
		return binding.root
	}

	override fun viewDidLoad(view: View) {
		super.viewDidLoad(view)
		binding.viewControllerGiftBoxView.setOnClickListener { fetchLottery() }
		showGiftBox()
	}

	private fun fetchLottery() {
		binding.viewControllerGiftBoxView.shakeAnimation.cancel()
		binding.viewControllerGiftBoxView.playOpenBoxAnimation(activity)
		binding.viewControllerGiftBoxView.animationStateRelay.observeOn(MainThread).subscribe {
			openTreasureBox()
		}.addTo(compositeDisposable = compositeDisposable)
	}

	private fun openTreasureBox() {
		binding.viewControllerGiftBoxView.shakeAnimation.cancel()
		val controller = OpenBoxResultViewController(OpenBoxResultUIModel.fake())
		controller.delegate = this
		present(viewController = controller, animated = true)
	}

	private fun showGiftBox() = run {
		binding.viewControllerGiftBoxView.shakeAnimation.start()
		view.postDelayed({
			binding.viewControllerGiftBoxView.fallBoxAnimation()
		}, 50)
	}

	override fun openBoxResultViewControllerResultReady(prize: Int) {
		dismiss(animated = true, completion = {
			binding.viewControllerGiftBoxView.shakeAnimation.start()
		})
	}
}
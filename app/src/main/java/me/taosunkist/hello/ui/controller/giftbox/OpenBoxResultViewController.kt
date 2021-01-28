package me.taosunkist.hello.ui.controller.giftbox

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import androidx.databinding.DataBindingUtil
import me.taosunkist.hello.R
import me.taosunkist.hello.databinding.ControllerViewGiftBoxResultBinding
import me.taosunkist.hello.ui.controller.giftbox.model.OpenBoxResultUIModel
import me.taosunkist.hello.ui.reusable.viewcontroller.controller.BaseViewController
import me.taosunkist.hello.ui.reusable.viewcontroller.presentation.PresentingAnimation
import me.taosunkist.hello.utility.weak

interface OpenBoxResultViewControllerDelegate {
	fun openBoxResultViewControllerResultReady(prize: Int)
}

class OpenBoxResultViewController(private val openBoxResultUIModel: OpenBoxResultUIModel) : BaseViewController() {

	companion object {
		const val TAG = "tagGiftBoxResult"
	}

	var delegate: OpenBoxResultViewControllerDelegate? by weak()

	lateinit var binding: ControllerViewGiftBoxResultBinding


	init {
		tag = TAG
		presentationStyle.apply {
			dim = true
			allowDismiss = false
			allowTapOutsideToDismiss = false
			gravity = Gravity.CENTER
			animation = PresentingAnimation.BOTTOM
			overCurrentContext = true
		}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
		binding = DataBindingUtil.inflate(inflater, R.layout.controller_view_gift_box_result, container, false)
		binding.uiModel = OpenBoxResultUIModel.fake()
		return binding.root
	}

	override fun viewDidLoad(view: View) {
		super.viewDidLoad(view)

		binding.controllerViewGiftBoxResultPrizeTakePrizeTextView.setOnClickListener { takePrizePressed() }

		binding.controllerViewGiftBoxResultPrizeDescribeTextView.text = openBoxResultUIModel.message
		binding.controllerViewGiftBoxResultDiamondImageView.setImageResource(openBoxResultUIModel.getPrizePromptIconResID())

		val rotateAnimation = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply {
			duration = 5000
			fillAfter = true
			repeatCount = Animation.INFINITE
			interpolator = LinearInterpolator()
		}
		binding.controllerViewGiftBoxResultDiamondHaloImageView.animation = rotateAnimation

		val scaleAnimation = ScaleAnimation(
			0.85f, 1f,
			0.85f, 1f,
			Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply {
			duration = 800
			repeatCount = 5
			interpolator = BounceInterpolator()
		}

		binding.controllerViewGiftBoxResultPrizeTakePrizeTextView.startAnimation(scaleAnimation)

		binding.controllerViewGiftBoxResultDiamondImageView.viewTreeObserver.addOnGlobalLayoutListener {
			val scaleAnimation = ScaleAnimation(
				0.8f, 1f,
				0.8f, 1f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply {
				duration = 200
				interpolator = BounceInterpolator()
			}
			val alphaAnimation = AlphaAnimation(0f, 1f).apply {
				fillAfter = true
			}
			val transAnimation = TranslateAnimation(0f, 0f,
				-(view.measuredHeight.toFloat()) * 0.5f, 0f).apply {
				fillAfter = true
				interpolator = BounceInterpolator()
			}

			val diamondImageViewAnimationSet = AnimationSet(false)
			diamondImageViewAnimationSet.addAnimation(alphaAnimation)
			diamondImageViewAnimationSet.addAnimation(scaleAnimation)
			diamondImageViewAnimationSet.addAnimation(transAnimation)
			diamondImageViewAnimationSet.duration = 1500
			diamondImageViewAnimationSet.fillAfter = true
			binding.controllerViewGiftBoxResultDiamondImageView.startAnimation(diamondImageViewAnimationSet)
		}
	}

	private fun takePrizePressed() = run {
		delegate?.openBoxResultViewControllerResultReady(prize = openBoxResultUIModel.prize)
	}

}
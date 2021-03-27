package top.thsunkist.tatame.ui.reusable.widget

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import top.thsunkist.tatame.utilities.Dimens
import top.thsunkist.tatame.utilities.setFrame


class LongPageVerticalLayout : ViewGroup {

	companion object {

	}

	var firstLayout = FrameLayout(context).apply {
		layoutParams = LayoutParams(Dimens.screenWidth, Dimens.screenHeight)
		setBackgroundColor(Color.GREEN)
	}

	var secondLayout = FrameLayout(context).apply {
		layoutParams = LayoutParams(Dimens.screenWidth, Dimens.screenHeight)
		setBackgroundColor(Color.YELLOW)
	}

	constructor(context: Context) : super(context)

	constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

	constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

	init {
		addView(firstLayout)
		addView(secondLayout)
		firstLayout.setOnClickListener { firstLayoutPressed() }
		secondLayout.setOnClickListener { secondLayoutPressed() }
	}

	override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
		firstLayout.setFrame(0, 0, Dimens.screenWidth, Dimens.screenHeight)
		secondLayout.setFrame(0, Dimens.screenHeight, Dimens.screenWidth, Dimens.screenHeight)
	}

	private fun firstLayoutPressed() {
		val fromY = Dimens.screenHeight.toFloat()
		secondLayout.setFrame(0, 0, Dimens.screenWidth, Dimens.screenHeight)
		val toY = 0f
		val objectAnimator = ObjectAnimator.ofFloat(
			secondLayout,
			"translationY",
			fromY,
			toY
		)
		objectAnimator.duration = 250
		objectAnimator.start()
	}

	private fun secondLayoutPressed() {
		val fromY = 0f
		val toY = Dimens.screenHeight.toFloat()
		secondLayout.setFrame(0, 0, Dimens.screenWidth, Dimens.screenHeight)
		val objectAnimator = ObjectAnimator.ofFloat(
			secondLayout,
			"translationY",
			fromY,
			toY
		)
		objectAnimator.duration = 250
		objectAnimator.start()
	}

//	override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
//		return true
//	}
}

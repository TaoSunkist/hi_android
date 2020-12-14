package top.thsunkist.brainhealthy.ui.reusable.views

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.TextViewCompat
import top.thsunkist.brainhealthy.ui.reusable.viewcontroller.animation.AnimationListener
import top.thsunkist.brainhealthy.utilities.*
import top.thsunkist.brainhealthy.utilities.view.Dimens
import top.thsunkist.brainhealthy.utilities.view.setFrame
import kotlin.math.max

class AnimatedNumberTextView : FrameLayout {

	constructor(context: Context) : super(context)

	constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

	private var spacing: Int = Dimens.dpToPx(4)

	private val textView = AppCompatTextView(context).apply {
		layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
		setLines(1)
		textSize = Dimens.fontSizeSmall
		gravity = Gravity.START or Gravity.CENTER_VERTICAL
		setTextColor(Color.WHITE)
		ellipsize = TextUtils.TruncateAt.END
	}

	val animationNumberView = AppCompatTextView(context).apply {
		layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
		setLines(1)
		textSize = Dimens.fontSizeSmall
		gravity = Gravity.CENTER
		setTextColor(Color.WHITE)
		val autoSizeMaxTextSize = 18
		val autoSizeMinTextSize = 10
		val autoSizeStepGranularity = 2
		TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(
			this,
			autoSizeMinTextSize,
			autoSizeMaxTextSize,
			autoSizeStepGranularity,
			TypedValue.COMPLEX_UNIT_SP
		)
		alpha = 0f

		setShadowLayer(
			Dimens.dpToPx(1).toFloat(),
			Dimens.dpToPx(1).toFloat(),
			Dimens.dpToPx(1).toFloat(),
			Color.parseColor("#50000000")
		)
	}

	val iconImageView = AppCompatImageView(context).apply {
		layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
		scaleType = ImageView.ScaleType.CENTER

	}

	init {
		clipChildren = false
		clipToPadding = false
		addView(iconImageView)
		addView(textView)
		addView(animationNumberView)
		number = 0f
	}

	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		measureChild(iconImageView, widthMeasureSpec, heightMeasureSpec)
		measureChild(textView, widthMeasureSpec, heightMeasureSpec)
		animationNumberView.measure(widthMeasureSpec, heightMeasureSpec)
		setMeasuredDimension(
			iconImageView.measuredWidth + spacing + textView.measuredWidth,
			max(iconImageView.measuredHeight, textView.measuredHeight)
		)
	}

	override fun onLayout(p0: Boolean, l: Int, t: Int, r: Int, b: Int) {
		iconImageView.setFrame(
			0,
			(b - t - iconImageView.measuredHeight) / 2,
			iconImageView.measuredWidth,
			iconImageView.measuredHeight
		)
		val textLeft = iconImageView.measuredWidth + spacing
		textView.setFrame(
			textLeft,
			(b - t - textView.measuredHeight) / 2,
			(r - l) - textLeft,
			textView.measuredHeight
		)
		textView.layoutParams.width = (r - l) - textLeft

		animationNumberView.setFrame(
			-animationNumberView.measuredWidth / 2 + iconImageView.measuredWidth / 2,
			Dimens.dpToPx(30),
			animationNumberView.measuredWidth,
			animationNumberView.measuredHeight
		)
	}

	var number: Float = 0f
		set(newValue) {
			field = newValue
			this.textView.text = number.format2Bit()
		}

	fun simpleAnimatedTo(number: Float): Animator {
		return ObjectAnimator.ofFloat(this, "number", this.number, number)
			.setDuration(2000)
	}

	fun animateTo(number: Float): AnimatorSet {
		animationNumberView.translationY = 0f
		val diff = number - this.number
		animationNumberView.text = if (diff > 0) "+$diff" else diff.toString()
		val plusFadeInAnimator =
			ObjectAnimator.ofFloat(animationNumberView, "alpha", 0f, 1f).setDuration(100)
		val plusTranslationAnimator = ObjectAnimator.ofFloat(
			animationNumberView, "translationY", -Dimens.dpToPx(20)
			.toFloat()
		)
			.setDuration(100)
		val plusFadeOutAnimator =
			ObjectAnimator.ofFloat(animationNumberView, "alpha", 1f, 0f).setDuration(100)
		val numberAnimator = ObjectAnimator.ofFloat(this, "number", this.number, number)
			.setDuration(2000)
		val numberPopAnimation1 = createPopAnimation(animationNumberView)

		val iconPopAnimation = createPopAnimation(iconImageView)
		val numberPopAnimation2 = createPopAnimation(textView)

		return playSequentially(
			plusFadeInAnimator,
			numberPopAnimation1,
			playTogether(plusTranslationAnimator, plusFadeOutAnimator, delay = 400),
			playTogether(iconPopAnimation, numberPopAnimation2),
			numberAnimator
		)
			.apply {
				addListener(object : AnimationListener() {
					override fun onAnimationEnd(p0: Animator) {
						animationNumberView.translationY = 0f
					}
				})
			}
	}

	fun setNumberTextSize(textSize: Float) {
		animationNumberView.textSize = textSize
	}
}
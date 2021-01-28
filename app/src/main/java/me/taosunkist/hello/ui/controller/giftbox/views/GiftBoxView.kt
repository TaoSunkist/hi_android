package me.taosunkist.hello.ui.controller.giftbox.views

import android.animation.*
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity.BOTTOM
import android.view.Gravity.CENTER_HORIZONTAL
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.animation.Animation
import android.view.animation.AnticipateInterpolator
import android.view.animation.BounceInterpolator
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.jakewharton.rxrelay2.PublishRelay
import me.taosunkist.hello.R
import me.taosunkist.hello.utility.Dimens
import java.util.concurrent.atomic.AtomicBoolean

interface AnimatorListener : Animator.AnimatorListener {

	override fun onAnimationRepeat(animation: Animator) {}

	override fun onAnimationEnd(animation: Animator, isReverse: Boolean) {}

	override fun onAnimationEnd(animation: Animator) {}

	override fun onAnimationCancel(animation: Animator) {}

	override fun onAnimationStart(animation: Animator, isReverse: Boolean) {}

	override fun onAnimationStart(animation: Animator) {}
}

class GiftBoxView : FrameLayout {

	constructor(context: Context) : super(context)
	constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
	constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

	companion object {
		const val BOX_COVER_RIGHT_RATE = 0.075f
		const val BOX_COVER_BOTTOM_RATE = 0.5f
	}

	val animationStateRelay = PublishRelay.create<Boolean>()

	private var boxLidDrawable: Drawable = context.getDrawable(R.drawable.ic_box_lid)!!
	private var boxBodyDrawable: Drawable = context.getDrawable(R.drawable.ic_box_body)!!
	private var boxLidImageView: AppCompatImageView
	private var boxBodyImageView: AppCompatImageView
	val shakeAnimation: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(this, PropertyValuesHolder.ofKeyframe(View.SCALE_X,
		Keyframe.ofFloat(.3f, 1.1f),
		Keyframe.ofFloat(.4f, 1.1f),
		Keyframe.ofFloat(.5f, 1.1f)), PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
		Keyframe.ofFloat(.3f, 1.1f),
		Keyframe.ofFloat(.4f, 1.1f),
		Keyframe.ofFloat(.5f, 1.1f)), PropertyValuesHolder.ofKeyframe(View.ROTATION,
		Keyframe.ofFloat(0f, 0f),
		Keyframe.ofFloat(.1f, -3f * 1.0f),
		Keyframe.ofFloat(.2f, -3f * 1.0f),
		Keyframe.ofFloat(.3f, 3f * 1.0f),
		Keyframe.ofFloat(.4f, -3f * 1.0f),
		Keyframe.ofFloat(.5f, 3f * 1.0f),
		Keyframe.ofFloat(.6f, -3f * 1.0f),
		Keyframe.ofFloat(.7f, 3f * 1.0f),
		Keyframe.ofFloat(.8f, -3f * 1.0f),
		Keyframe.ofFloat(.9f, 3f * 1.0f),
		Keyframe.ofFloat(1f, 0f)
	)).setDuration(2000).apply { repeatCount = Animation.INFINITE }

	init {
		boxLidImageView = AppCompatImageView(context).apply {
			layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
			(layoutParams as LayoutParams).gravity = (CENTER_HORIZONTAL or BOTTOM)
			(layoutParams as LayoutParams).setMargins(0, 0,
				(boxBodyDrawable.intrinsicWidth * BOX_COVER_RIGHT_RATE).toInt(),
				(boxBodyDrawable.intrinsicHeight * BOX_COVER_BOTTOM_RATE).toInt()
			)
			setImageDrawable(this@GiftBoxView.boxLidDrawable)
			translationZ = 1f
		}
		boxBodyImageView = AppCompatImageView(context).apply {
			layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
			(layoutParams as LayoutParams).gravity = (CENTER_HORIZONTAL or BOTTOM)
			setImageDrawable(this@GiftBoxView.boxBodyDrawable)
			translationZ = 1f
		}
		addView(boxBodyImageView)
		addView(boxLidImageView)
	}

	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec)
		setMeasuredDimension(boxBodyDrawable.intrinsicWidth * 2, (boxBodyDrawable.intrinsicHeight * 2.5f).toInt())
	}

	val isAnimationRunning = AtomicBoolean(false)

	fun fallBoxAnimation() {
		if (isAnimationRunning.getAndSet(true)) return

		boxLidImageView.translationZ = 1f
		val viewBounceAnimator = ObjectAnimator.ofFloat(
			this, "translationY", -(Dimens.screenHeight / 2).toFloat() + measuredHeight, 0f
		).apply {
			duration = 1500
			interpolator = BounceInterpolator()
		}


		viewBounceAnimator.addListener(object : AnimatorListener {
			override fun onAnimationEnd(animation: Animator) = run {
				isAnimationRunning.set(false)
			}

			override fun onAnimationEnd(animation: Animator, isReverse: Boolean) = run { onAnimationEnd(animation) }

			override fun onAnimationStart(animation: Animator) = run { visibility = View.VISIBLE }
			override fun onAnimationStart(animation: Animator, isReverse: Boolean) = run { onAnimationStart(animation) }
		})
		viewBounceAnimator.start()
	}


	fun playOpenBoxAnimation(activity: AppCompatActivity) {
		if (isAnimationRunning.getAndSet(true)) return

		boxLidImageView.translationZ = 1f
		val rotationAnimator: ObjectAnimator = ObjectAnimator.ofFloat(boxLidImageView, "rotation", 0f, 70f).apply { duration = 600 }
		val animator1 = ObjectAnimator.ofFloat(boxLidImageView, "x", boxBodyImageView.x * 1.5f)
		val animator2 = ObjectAnimator.ofFloat(boxLidImageView, "y", boxBodyImageView.y * 0.8f)
		val animatorSet = AnimatorSet()

		val anim = ObjectAnimator.ofObject(
			boxLidImageView, "height",
			TypeEvaluator<Float> { fraction, _, _ ->
				if (fraction > 0.5f && boxLidImageView.translationZ > 0f) {
					boxLidImageView.translationZ = 0f
					val explosionField = ExplosionField.attach2Window(activity)
					explosionField.explode(this@GiftBoxView)
					rotationAnimator.start()
				}
				fraction
			}, boxBodyImageView.x, boxBodyImageView.y
		)
		animator2.interpolator = AnticipateInterpolator(30.0f)
		animatorSet.duration = 1600
		animatorSet.addListener(object : AnimatorListener {
			override fun onAnimationEnd(animation: Animator) {
				animationStateRelay.accept(true)
				isAnimationRunning.set(false)
				resetBoxAnimation()
			}

			override fun onAnimationEnd(animation: Animator, isReverse: Boolean) = onAnimationEnd(animation)
		})
		animatorSet.playTogether(animator1, animator2, anim)
		animatorSet.start()
	}

	fun resetBoxAnimation() {
		if (isAnimationRunning.getAndSet(true)) return

		boxLidImageView.translationZ = 0f
		var animatorSet = AnimatorSet()
		val animatorAlpha: ObjectAnimator = ObjectAnimator.ofFloat(boxLidImageView, "rotation", 70f, 0f).apply { duration = 600 }
		val animator1 = ObjectAnimator.ofFloat(boxLidImageView, "x", boxBodyImageView.x * 0.50f)
		val animator2 = ObjectAnimator.ofFloat(boxLidImageView, "y", boxBodyImageView.y * 0.72f)
		val anim = ObjectAnimator.ofObject(
			boxLidImageView, "height",
			TypeEvaluator<Float> { fraction, _, _ ->
				if (fraction > 0.6f && boxLidImageView.translationZ <= 0f) {
					boxLidImageView.translationZ = 5f
					animatorAlpha.start()
				}
				fraction
			}, boxBodyImageView.x, boxBodyImageView.y
		)
		animator2.interpolator = AnticipateInterpolator(-25.0f)
		animatorSet.duration = 1000
		animatorSet.addListener(object : AnimatorListener {
			override fun onAnimationEnd(animation: Animator, isReverse: Boolean) = onAnimationEnd(animation)
			override fun onAnimationEnd(animation: Animator) = run { isAnimationRunning.set(false) }
		})
		animatorSet.playTogether(animator1, animator2, anim)
		animatorSet.start()
	}

	override fun setOnClickListener(l: OnClickListener?) {
		boxLidImageView.setOnClickListener(l)
		boxBodyImageView.setOnClickListener(l)
	}
}
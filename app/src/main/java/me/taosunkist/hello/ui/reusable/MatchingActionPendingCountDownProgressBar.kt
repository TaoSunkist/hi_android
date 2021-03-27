package me.taosunkist.hello.ui.reusable

import android.animation.Animator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import top.thsunkist.library.utilities.Dimens

enum class Style {
    LOADING, CONNECTING
}

class MatchingActionPendingCountDownProgressBar : View {

    var interruptAlphaAnimation = true

    var style: Style = Style.LOADING
        set(value) {
            field = value
            progress = 100f
            this.invalidate()
        }

    var progress: Float = 0.0f
        set(value) {
            field = value
            sweepAngle = (3.6f * progress)
            this.invalidate()
            if (progress == 100f) {
                showAlphaAnimation.setAnimationListener(object : AnimationListener() {
                    override fun onAnimationEnd(animation: Animation) =
                            if (interruptAlphaAnimation) startAnimation(hideAlphaAnimation) else hideAlphaAnimation.cancel()
                })
                hideAlphaAnimation.setAnimationListener(object : AnimationListener() {
                    override fun onAnimationEnd(animation: Animation) =
                            if (interruptAlphaAnimation) startAnimation(showAlphaAnimation) else showAlphaAnimation.cancel()
                })
                startAnimation(showAlphaAnimation)
            } else {
                clearAnimation()
            }
        }

    private lateinit var paintBgCircle: Paint
    private lateinit var paintBgCircle2: Paint
    private lateinit var paintProgressCircle: Paint

    private var sweepAngle = 0.0f
    private val connectingColor = Color.parseColor("#13c66c")
    private val progressBarRect = RectF()
    private val showAlphaAnimation = AlphaAnimation(0.2f, 1f).apply { duration = 400 }
    private val hideAlphaAnimation = AlphaAnimation(1f, 0.2f).apply { duration = 400 }
    private var coordinatePoint: Float = 0f


    constructor(context: Context?) : super(context) {
        this.init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        this.init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
    ) {
        this.init()
    }

    private fun init() {
        paintBgCircle = Paint()
        paintBgCircle.isAntiAlias = true
        paintBgCircle.style = Paint.Style.STROKE
        paintBgCircle.strokeWidth = Dimens.marginSmall.toFloat()
        paintBgCircle.color = Color.YELLOW

        paintProgressCircle = Paint()
        paintProgressCircle.isAntiAlias = true
        paintProgressCircle.strokeWidth = Dimens.dpToPx(20).toFloat()
        paintProgressCircle.color = Color.GREEN

        paintBgCircle2 = Paint()
        paintBgCircle2.isAntiAlias = true
        paintBgCircle2.color = Color.BLUE
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        coordinatePoint = (this.measuredWidth / 2).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (style == Style.LOADING) {
            paintBgCircle.color = Color.TRANSPARENT
            paintProgressCircle.strokeWidth = Dimens.marginSmall.toFloat() / 2
        } else {
            paintBgCircle.color = Color.TRANSPARENT
            paintProgressCircle.color = Color.TRANSPARENT
            paintProgressCircle.color = connectingColor
            paintProgressCircle.strokeWidth = Dimens.marginSmall.toFloat()
        }

        progressBarRect.left = paintProgressCircle.strokeWidth
        progressBarRect.top = paintProgressCircle.strokeWidth
        progressBarRect.right = (this.measuredWidth).toFloat() - paintProgressCircle.strokeWidth
        progressBarRect.bottom = (this.measuredWidth).toFloat() - paintProgressCircle.strokeWidth

        canvas.drawArc(
                progressBarRect,
                -90f,
                360f,
                false,
                paintBgCircle2
        )

        canvas.drawArc(
                progressBarRect,
                -90f,
                sweepAngle,
                false,
                paintProgressCircle
        )

        canvas.drawArc(
                RectF(Dimens.dpToPx(50).toFloat(),
                        Dimens.dpToPx(50).toFloat(),
                        (this.measuredWidth).toFloat() - Dimens.dpToPx(50).toFloat(),
                        (this.measuredWidth).toFloat() - Dimens.dpToPx(50).toFloat()),
                -90f,
                360f,
                false,
                Paint().apply {
                    color = Color.WHITE
                }
        )
    }

    fun closeAlphaAnimation() {
        interruptAlphaAnimation = false
        visibility = INVISIBLE
    }

    abstract class AnimationListener : Animator.AnimatorListener, Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animator) {
        }

        override fun onAnimationRepeat(animation: Animation) {
        }

        override fun onAnimationEnd(animation: Animator, isReverse: Boolean) {
            super.onAnimationEnd(animation, isReverse)
        }

        override fun onAnimationEnd(animation: Animator) {
        }

        override fun onAnimationEnd(animation: Animation) {
        }

        override fun onAnimationCancel(animation: Animator) {
        }

        override fun onAnimationStart(animation: Animator, isReverse: Boolean) {
            super.onAnimationStart(animation, isReverse)
        }

        override fun onAnimationStart(animation: Animator) {
        }

        override fun onAnimationStart(animation: Animation) {
        }
    }
}
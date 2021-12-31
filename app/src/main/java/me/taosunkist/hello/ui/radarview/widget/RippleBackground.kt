package me.taosunkist.hello.ui.radarview.widget

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.RelativeLayout
import me.taosunkist.hello.R
import top.thsunkist.appkit.utility.Debug
import java.util.*
import kotlin.math.min

class RippleBackground : RelativeLayout {

    companion object {

        private const val DEFAULT_RIPPLE_COUNT = 6

        private const val DEFAULT_DURATION_TIME = 3000

        private const val DEFAULT_SCALE = 6.0f

        private const val DEFAULT_FILL_TYPE = 0
    }

    private var rippleColor = 0

    private var rippleStrokeWidth = 0f

    private var rippleRadius = 0f

    private var rippleDurationTime = 0

    private var rippleAmount = 0

    private var rippleDelay = 0

    private var rippleScale = 0f

    private var rippleType = 0

    private var paint: Paint = Paint().apply {
        isAntiAlias = true
    }

    var isRippleAnimationRunning = false
        private set

    private val animatorSet: AnimatorSet = AnimatorSet()

    private var animatorList: ArrayList<Animator> = ArrayList()

    private lateinit var rippleParams: LayoutParams

    private val rippleViewList = ArrayList<RippleView>()

    constructor(context: Context?) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        if (isInEditMode) return

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RippleBackground)

        with(typedArray) {
            rippleColor = getColor(R.styleable.RippleBackground_rb_color, resources.getColor(R.color.colorAccent))
            rippleStrokeWidth = getDimension(R.styleable.RippleBackground_rb_strokeWidth, resources.getDimension(R.dimen.rippleStrokeWidth))
            rippleRadius = getDimension(R.styleable.RippleBackground_rb_radius, resources.getDimension(R.dimen.rippleRadius))
            rippleDurationTime = getInt(R.styleable.RippleBackground_rb_duration, DEFAULT_DURATION_TIME)
            rippleAmount = getInt(R.styleable.RippleBackground_rb_rippleAmount, DEFAULT_RIPPLE_COUNT)
            rippleScale = getFloat(R.styleable.RippleBackground_rb_scale, DEFAULT_SCALE)
            rippleType = getInt(R.styleable.RippleBackground_rb_type, DEFAULT_FILL_TYPE)
            typedArray.recycle()
        }


        rippleDelay = rippleDurationTime / rippleAmount
        if (rippleType == DEFAULT_FILL_TYPE) {
            rippleStrokeWidth = 0f
            paint.style = Paint.Style.FILL
        } else {
            paint.style = Paint.Style.STROKE
        }
        paint.color = rippleColor

        rippleParams = LayoutParams((2 * (rippleRadius + rippleStrokeWidth)).toInt(), (2 * (rippleRadius + rippleStrokeWidth)).toInt())
        rippleParams.addRule(CENTER_IN_PARENT, TRUE)

        animatorSet.interpolator = AccelerateDecelerateInterpolator()

        for (i in 0 until rippleAmount) {
            val rippleView = RippleView(getContext())
            addView(rippleView, rippleParams)
            rippleViewList.add(rippleView)
            val scaleXAnimator = ObjectAnimator.ofFloat(rippleView, "ScaleX", 1.0f, rippleScale).apply {
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.RESTART
                startDelay = (i * rippleDelay).toLong()
                duration = rippleDurationTime.toLong()
            }
            animatorList.add(scaleXAnimator)
            val scaleYAnimator = ObjectAnimator.ofFloat(rippleView, "ScaleY", 1.0f, rippleScale).apply {
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.RESTART
                startDelay = (i * rippleDelay).toLong()
                duration = rippleDurationTime.toLong()
            }
            animatorList.add(scaleYAnimator)
            val alphaAnimator = ObjectAnimator.ofFloat(rippleView, "Alpha", 1.0f, 0f).apply {
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.RESTART
                startDelay = (i * rippleDelay).toLong()
                duration = rippleDurationTime.toLong()
            }
            animatorList.add(alphaAnimator)
        }
        animatorSet.playTogether(animatorList)
    }

    private inner class RippleView(context: Context?) : View(context) {
        override fun onDraw(canvas: Canvas) {
            val radius = min(width, height) / 2
            canvas.drawCircle(radius.toFloat(), radius.toFloat(), radius - rippleStrokeWidth, paint)
        }

        init {
            this.visibility = VISIBLE
        }
    }

    fun start() {
        if (isRippleAnimationRunning.not()) {
            animatorSet.end()
            for (rippleView in rippleViewList) {
                rippleView.visibility = VISIBLE
            }
            animatorSet.start()
            isRippleAnimationRunning = true
        }
    }

    fun switchMode() {
        for (rippleView in rippleViewList) {
            rippleView.visibility = VISIBLE
            rippleColor = Debug.colors.random()
            paint.color = rippleColor
            rippleView.invalidate()
        }
    }

    fun stop() {
        if (isRippleAnimationRunning) {
            animatorSet.end()
            isRippleAnimationRunning = false
        }
    }
}
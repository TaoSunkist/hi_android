package top.thsunkist.brainhealthy.ui.reusable.views

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import top.thsunkist.brainhealthy.ui.reusable.viewcontroller.animation.AnimationListener
import top.thsunkist.brainhealthy.utilities.format2Bit

class TickTextView : AppCompatTextView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    private var number: Float = 0f
        set(newValue) {
            field = newValue
            text = number.format2Bit()
        }

    init {
        layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        setLines(1)
        gravity = Gravity.CENTER
        setTextColor(Color.WHITE)
    }

    fun animateTo(number: Float): ObjectAnimator? {
        val numberAnimator = ObjectAnimator.ofFloat(this, "number", this.number, number).setDuration(800)
        translationY = 0f

        val diff = number - this.number
        text = if (diff > 0) "+$diff" else diff.toString()
        numberAnimator.addListener(object : AnimationListener() {
            override fun onAnimationEnd(p0: Animator) {
                translationY = 0f
            }
        })
        numberAnimator.start()
        return numberAnimator
    }
}
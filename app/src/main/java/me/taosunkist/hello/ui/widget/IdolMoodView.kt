package me.taosunkist.hello.ui.widget

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import com.google.gson.annotations.SerializedName
import me.taosunkist.hello.R

enum class Mood(val value: String, @DrawableRes val resId: Int) {
    @SerializedName("LOW")
    LOW(value = "LOW", resId = R.drawable.ic_depressed_mood),

    @SerializedName("NORMAL")
    NORMAL(value = "NORMAL", resId = R.drawable.ic_normal_mood),

    @SerializedName("HAPPY")
    HAPPY(value = "HAPPY", resId = R.drawable.ic_fun_mood)
}

class IdolMoodView : FrameLayout, Animator.AnimatorListener {

    private val displayImageView = AppCompatImageView(context)
    private val animationImageView = AppCompatImageView(context)
    private var drawableWidth: Int
    private var drawableHeight: Int
    private val drawable: Drawable = context.getDrawable(Mood.values().first().resId)!!
    private var translationXAnimation: ObjectAnimator

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        drawableWidth = drawable.intrinsicWidth
        drawableHeight = drawable.intrinsicHeight
        translationXAnimation = ObjectAnimator.ofFloat(animationImageView, "translationX", 0f, -(drawableWidth.toFloat()))
        addView(displayImageView)
        addView(animationImageView)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) = run { super.onMeasure(widthMeasureSpec, drawableHeight) }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        displayImageView.layout(right - drawableWidth, top, right, bottom)
        animationImageView.layout(right, top, right + drawableWidth, bottom)
    }

    fun switchIdolMood(mood: Mood? = Mood.values().random()) {
        if (mood == null) {
            displayImageView.setImageResource(0)
            return
        } else if (translationXAnimation.isRunning) {
            return
        }
        animationImageView.setImageResource(mood.resId)
        translationXAnimation.addListener(MoodAnimationView@ this)
        translationXAnimation.duration = 500
        translationXAnimation.start()
    }

    override fun onAnimationStart(animation: Animator?) {}
    override fun onAnimationRepeat(animation: Animator?) {}
    override fun onAnimationCancel(animation: Animator?) = run { }
    override fun onAnimationEnd(animation: Animator?) = run { onAnimationEnd(animation = animation, isReverse = false) }
    override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) = run { displayImageView.setImageDrawable(animationImageView.drawable) }
}
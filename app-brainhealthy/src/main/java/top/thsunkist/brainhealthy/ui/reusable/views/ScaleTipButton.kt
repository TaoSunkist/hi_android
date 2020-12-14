package top.thsunkist.brainhealthy.ui.reusable.views

import android.content.Context
import android.util.AttributeSet
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.appcompat.widget.AppCompatButton
import top.thsunkist.brainhealthy.ui.reusable.viewcontroller.animation.AnimationListener

class ScaleTipButton : AppCompatButton {
    private var interruptLaunchVideoCallingButtonAnimationRunning = true

    private val scaleAnimation: ScaleAnimation by lazy {
        ScaleAnimation(
            1f, 1.1f, 1f, 1.1f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 400
            setAnimationListener(object : AnimationListener() {
                override fun onAnimationEnd(animation: Animation) =
                    if (interruptLaunchVideoCallingButtonAnimationRunning) {
                        startAnimation(reverseScaleAnimation)
                    } else {
                        cancel()
                    }
            })
        }
    }

    private val reverseScaleAnimation: ScaleAnimation by lazy {
        ScaleAnimation(
            1.1f, 1f, 1.1f, 1f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 400
            setAnimationListener(object : AnimationListener() {
                override fun onAnimationEnd(animation: Animation) =
                    if (interruptLaunchVideoCallingButtonAnimationRunning) {
                        startAnimation(scaleAnimation)
                    } else {
                        cancel()
                    }
            })
        }
    }


    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}
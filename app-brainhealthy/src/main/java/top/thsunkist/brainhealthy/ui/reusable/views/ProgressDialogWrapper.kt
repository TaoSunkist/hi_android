package top.thsunkist.brainhealthy.ui.reusable.views

import android.animation.Animator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import top.thsunkist.brainhealthy.R
import top.thsunkist.brainhealthy.ui.reusable.viewcontroller.animation.AnimationListener
import top.thsunkist.brainhealthy.utilities.view.Dimens
import top.thsunkist.brainhealthy.utilities.view.makeExactlyMeasure

private const val animationDuration = 200L

class ProgressDialogWrapper(parent: ViewGroup) {

    val view: View = LayoutInflater.from(parent.context).inflate(R.layout.view_progress, parent, false)
    private val progressContent: View = view.findViewById(R.id.view_progress_content)
    private val progressBackground: View = view.findViewById(R.id.view_progress_background)
    val progressText: AppCompatTextView = view.findViewById(R.id.view_progress_text)

    init {
        view.isClickable = true
        view.isFocusable = true
    }

    fun show(animated: Boolean, completion: () -> Unit) {
        if (view.measuredWidth == 0) {
            /* view is not yet measured, force to measure and set to hidden state */
            view.measure(view.makeExactlyMeasure(Dimens.screenWidth), view.makeExactlyMeasure(Dimens.screenHeight))
            view.layout(0, 0, Dimens.screenWidth, Dimens.screenHeight)
            hide(animated = false, completion = {})
        }

        if (animated) {
            progressBackground.animate()
                .alpha(1f).setDuration(animationDuration).start()
            progressContent.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(animationDuration).setListener(object : AnimationListener() {
                    override fun onAnimationEnd(p0: Animator) {
                        completion()
                    }
                })
        } else {
            progressBackground.alpha = 1f
            progressContent.alpha = 1f
            progressContent.translationY = 0f
            completion()
        }
    }

    fun hide(animated: Boolean, completion: () -> Unit) {
        if (animated) {
            progressBackground.animate()
                .alpha(0f).setDuration(animationDuration).start()
            progressContent.animate()
                .alpha(0f)
                .translationY(Dimens.dpToPx(50).toFloat())
                .setDuration(animationDuration).setListener(object : AnimationListener() {
                    override fun onAnimationEnd(p0: Animator) {
                        completion()
                    }
                })
        } else {
            progressBackground.alpha = 0f
            progressContent.alpha = 0f
            progressContent.translationY = Dimens.dpToPx(50).toFloat()
            completion()
        }
    }
}
package top.thsunkist.brainhealthy.ui.reusable.viewcontroller.animation

import android.animation.Animator
import android.view.animation.Animation

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
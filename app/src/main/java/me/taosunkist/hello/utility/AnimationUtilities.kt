package me.taosunkist.hello.utility

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View

const val propertyNameInScaleX = "scaleX"
const val propertyNameInScaleY = "scaleY"
const val propertyNameInTranslationX = "translationX"
const val propertyNameInTranslationY = "translationY"


fun playTogether(vararg animators: Animator, delay: Long = 0): AnimatorSet {
    val set = AnimatorSet()
    set.playTogether(animators.asList())
    set.startDelay = delay
    return set
}

fun playSequentially(vararg animators: Animator, delay: Long = 0): AnimatorSet {
    val set = AnimatorSet()
    set.startDelay = delay
    set.playSequentially(animators.asList())
    return set
}

fun createPopAnimation(view: View, scale: Float = 1.4f, duration: Long = 100): Animator {
    val scaleX1 = ObjectAnimator.ofFloat(view, propertyNameInScaleX, 1.0f, scale).setDuration(duration / 2)
    val scaleY1 = ObjectAnimator.ofFloat(view, propertyNameInScaleY, 1.0f, scale).setDuration(duration / 2)
    val scaleX2 = ObjectAnimator.ofFloat(view, propertyNameInScaleX, scale, 1.0f).setDuration(duration / 2)
    val scaleY2 = ObjectAnimator.ofFloat(view, propertyNameInScaleY, scale, 1.0f).setDuration(duration / 2)
    return playSequentially(
        playTogether(scaleX1, scaleY1),
        playTogether(scaleX2, scaleY2)
    )
}
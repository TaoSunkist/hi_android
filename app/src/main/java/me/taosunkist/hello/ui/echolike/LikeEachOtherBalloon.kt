package me.taosunkist.hello.ui.echolike

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.animation.*
import androidx.annotation.MainThread
import androidx.cardview.widget.CardView
import androidx.fragment.app.DialogFragment
import me.taosunkist.hello.data.model.User
import me.taosunkist.hello.ui.zoomoutanimation.propertyNameInScaleX
import me.taosunkist.hello.ui.zoomoutanimation.propertyNameInScaleY
import top.thsunkist.appkit.utility.Dimens
import top.thsunkist.tatame.utilities.weak


@DslMarker
internal annotation class LikeEachOtherBalloonInlineDsl

interface LikeEachOtherBalloonDelegate {
    fun likeEachOtherBalloonDelegateDidClick(remoteUser: User)
}

@MainThread
@JvmSynthetic
@LikeEachOtherBalloonInlineDsl
inline fun createLikeEachOtherBalloon(
    context: Context,
    crossinline block: LikeEachOtherBalloon.Builder.() -> Unit
): LikeEachOtherBalloon = LikeEachOtherBalloon.Builder(context).apply(block).build()

class LikeEachOtherBalloon(val context: Context, val builder: Builder) :DialogFragment(context){
    companion object {
        private const val avatarAnimationSetStartDelayInMills: Long = 250
    }

    @LikeEachOtherBalloonInlineDsl
    class Builder(private val context: Context) {


        lateinit var remoteUser: User

        lateinit var localUser: User

        var delegate: LikeEachOtherBalloonDelegate? by weak()

        @SuppressLint("ObjectAnimatorBinding")
        fun build(): LikeEachOtherBalloon {

            val binding = BalloonLikeEachOtherBinding.inflate(LayoutInflater.from(context))

            binding.leftCircleImageView.alpha = 0f
            binding.rightCircleImageView.alpha = 0f

            val likeIconScaleXAnimator =
                ObjectAnimator.ofFloat(binding.likeIconImageView, propertyNameInScaleX, 0.1f, 1.2f, 1f).apply { duration = 400 }

            val likeIconScaleYAnimator =
                ObjectAnimator.ofFloat(binding.likeIconImageView, propertyNameInScaleY, 0.1f, 1.2f, 1f).apply { duration = 400 }

            val scaleXAnimator = ObjectAnimator.ofFloat(null, propertyNameInScaleX, 0.1f, 1.2f, 1f).apply { duration = 600 }

            val scaleYAnimator = ObjectAnimator.ofFloat(null, propertyNameInScaleY, 0.1f, 1.2f, 1f).apply { duration = 600 }

            val leftTranslationX = if (freeAddTimeAnimDirection) ObjectAnimator.ofFloat(
                binding.leftCircleImageView,
                propertyNameInTranslationX,
                0f,
                -Dimens.dpToPx(35).toFloat(),
                -Dimens.dpToPx(25).toFloat(),
                -Dimens.dpToPx(35).toFloat()
            ).apply {
                duration = 800
            } else ObjectAnimator.ofFloat(
                binding.leftCircleImageView,
                propertyNameInTranslationX,
                0f,
                Dimens.dpToPx(35).toFloat(),
                Dimens.dpToPx(25).toFloat(),
                Dimens.dpToPx(35).toFloat()
            ).apply {
                duration = 800
            }

            val rightTranslationX = if (freeAddTimeAnimDirection) ObjectAnimator.ofFloat(
                binding.rightCircleImageView,
                propertyNameInTranslationX,
                0f,
                Dimens.dpToPx(35).toFloat(),
                Dimens.dpToPx(25).toFloat(),
                Dimens.dpToPx(35).toFloat()
            ).apply {
                duration = 800
            } else ObjectAnimator.ofFloat(
                binding.rightCircleImageView,
                propertyNameInTranslationX,
                0f,
                -Dimens.dpToPx(35).toFloat(),
                -Dimens.dpToPx(25).toFloat(),
                -Dimens.dpToPx(35).toFloat()
            ).apply {
                duration = 800
            }

            binding.leftCircleImageView.bind(ImageUIModel.displayImage(remoteUser,Dimens.dpToPx(32),Dimens.dpToPx(32)))
            binding.rightCircleImageView.bind(ImageUIModel.displayImage(localUser, Dimens.dpToPx(32),Dimens.dpToPx(32)))

            balloon = createBalloon(context) {
                setLayout(binding.root)
                setBalloonAnimationStyle(R.style.LikeEachOtherBalloonAnimation10)
                setArrowSize(0)
                setHeight(180)
                setElevation(0)
                marginTop = Dimens.marginXLarge
                setWidthRatio(0.88f)
                setAutoDismissDuration(4800)
                setBackgroundDrawableResource(R.drawable.bg_balloon_like_each_other)
                setDismissWhenTouchOutside(false)
                setOnBalloonInitializedListener {

                    AnimatorSet().apply {
                        play(
                            scaleXAnimator.clone().apply { target = binding.leftCircleImageView }
                        ).with(
                            scaleYAnimator.clone().apply { target = binding.leftCircleImageView }
                        ).before(
                            leftTranslationX
                        )
                        startDelay = avatarAnimationSetStartDelayInMills
                        start()
                    }

                    AnimatorSet().apply {
                        play(
                            scaleXAnimator.clone().apply { target = binding.rightCircleImageView }
                        ).with(
                            scaleYAnimator.clone().apply { target = binding.rightCircleImageView }
                        ).before(
                            rightTranslationX
                        )
                        startDelay = avatarAnimationSetStartDelayInMills
                        start()
                    }

                    binding.root.postDelayed({
                        binding.leftCircleImageView.alpha = 1f
                        binding.rightCircleImageView.alpha = 1f
                    }, avatarAnimationSetStartDelayInMills)

                    leftTranslationX.addUpdateListener { animation ->
                        if (animation.animatedFraction >= 0.50f && likeIconScaleXAnimator.isStarted.not()) {
                            likeIconScaleXAnimator.start()
                            likeIconScaleYAnimator.start()
                            binding.likeIconImageView.alpha = 1f
                            binding.svgaImageView.startAnimation()
                        }
                    }
                }
            }

            val cardView: CardView = balloon.getContentView().findViewById(R.id.balloon_card)
            cardView.cardElevation = 0f
            binding.dismissImageView.setOnClickListener { balloon.dismiss() }

            balloon.setOnBalloonClickListener {
                delegate?.likeEachOtherBalloonDelegateDidClick(remoteUser)
            }
            balloon.setOnBalloonDismissListener {
                binding.svgaImageView.stopAnimation(clear = true)
                leftTranslationX.removeAllListeners()
                leftTranslationX.removeAllUpdateListeners()
            }

            return LikeEachOtherBalloon(context = context, builder = this@Builder)
        }
    }

    fun show(activity: Activity) {
        builder.balloon.showAlignTop(activity.window.decorView)
        RongIMService.shared.likeEachOtherEvent.accept(true)
    }

    fun dismiss() {
        builder.balloon.dismiss()
    }
}
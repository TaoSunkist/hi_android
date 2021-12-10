package me.taosunkist.hello.ui.mutualheartbeat

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatDialog
import androidx.core.text.TextUtilsCompat
import androidx.core.view.ViewCompat
import me.taosunkist.hello.databinding.DialogMutualHeartbeatBinding
import me.taosunkist.hello.utility.propertyNameInScaleX
import me.taosunkist.hello.utility.propertyNameInScaleY
import me.taosunkist.hello.utility.propertyNameInTranslationX
import top.thsunkist.appkit.engine.ImageUIModel
import top.thsunkist.appkit.engine.bind
import top.thsunkist.appkit.utility.Dimens
import java.util.*

class MutualHeartbeatDialog(context: Context) : AppCompatDialog(context) {
    companion object {
        fun init(context: Context): MutualHeartbeatDialog {
            return MutualHeartbeatDialog(context)
        }

        private const val avatarAnimationSetStartDelayInMills: Long = 250
    }

    lateinit var rightTranslationX: ObjectAnimator

    private lateinit var leftTranslationX: ObjectAnimator

    private lateinit var likeIconScaleYAnimator: ObjectAnimator

    lateinit var likeIconScaleXAnimator: ObjectAnimator

    private var _binding: DialogMutualHeartbeatBinding? = null

    private val binding: DialogMutualHeartbeatBinding get() = _binding!!

    @SuppressLint("ObjectAnimatorBinding")
    private val scaleXAnimator = ObjectAnimator.ofFloat(null, propertyNameInScaleX, 0.1f, 1.2f, 1f).apply { duration = 600 }

    @SuppressLint("ObjectAnimatorBinding")
    private val scaleYAnimator = ObjectAnimator.ofFloat(null, propertyNameInScaleY, 0.1f, 1.2f, 1f).apply { duration = 600 }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(DialogMutualHeartbeatBinding.inflate(LayoutInflater.from(context)).also { _binding = it }.root)

        likeIconScaleXAnimator =
            ObjectAnimator.ofFloat(binding.likeIconImageView, propertyNameInScaleX, 0.1f, 1.2f, 1f).apply { duration = 400 }

        likeIconScaleYAnimator =
            ObjectAnimator.ofFloat(binding.likeIconImageView, propertyNameInScaleY, 0.1f, 1.2f, 1f).apply { duration = 400 }

        leftTranslationX =
            if (TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == ViewCompat.LAYOUT_DIRECTION_RTL) ObjectAnimator.ofFloat(
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

        rightTranslationX =
            if (TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == ViewCompat.LAYOUT_DIRECTION_RTL) ObjectAnimator.ofFloat(
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

        binding.leftCircleImageView.alpha = 0f
        binding.rightCircleImageView.alpha = 0f

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
            }
        }
    }

    override fun dismiss() {
        super.dismiss()
        leftTranslationX.removeAllListeners()
        leftTranslationX.removeAllUpdateListeners()
    }
}
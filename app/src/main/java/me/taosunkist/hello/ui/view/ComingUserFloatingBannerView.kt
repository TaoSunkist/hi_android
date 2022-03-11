package me.taosunkist.hello.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Interpolator
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.mooveit.library.Fakeit
import me.taosunkist.hello.R
import top.thsunkist.appkit.engine.ImageUIModel
import top.thsunkist.appkit.utility.Dimens

data class ComingUserFloatingBannerUIModel(
    val avatarImageUIModel: ImageUIModel,
    val nickname: String,
    val carName: String? = null,
    val y: Float = 0f,
    @DrawableRes val backgroundDrawableRes: Int,
    val experLevel: Int,
) {

    companion object {

        fun fake(): ComingUserFloatingBannerUIModel {
            return ComingUserFloatingBannerUIModel(
                avatarImageUIModel = ImageUIModel.fake(),
                nickname = Fakeit.pokemon().name(),
                y = Dimens.screenHeight * 0.6f,
                backgroundDrawableRes = arrayOf(
                    R.drawable.activity_item_bg1,
                    R.drawable.activity_item_bg2,
                    R.drawable.activity_item_bg3,
                    R.drawable.activity_item_bg4,
                    R.drawable.activity_item_bg5
                ).random(),
                experLevel = (0..50).random(),
            )
        }

    }
}

@SuppressLint("ViewConstructor")
class ComingUserFloatingBannerView(context: Context, viewGroup: ViewGroup) : ConstraintLayout(context, null) {

    private val backgroundImageView: ImageView

    private val nicknameTextView: TextView

    private val carNameTextView: TextView

    var isActiveOfAnimation = false

    var animation: TranslateAnimation? = null

    init {
        viewGroup.addView(
            this,
            LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        )
    }

    init {
        inflate(context, R.layout.view_coming_user_floating_banner, this)

        backgroundImageView = findViewById(R.id.background_image_view)
        nicknameTextView = findViewById(R.id.nickname_text_view)
        carNameTextView = findViewById(R.id.car_name_text_view)
        visibility = View.GONE
    }

    /**
     * 设置数据
     *
     *
     * 显示弹幕 等级标志 + XXX + 骑着“XXX” 来了
     */
    fun bind(uiModel: ComingUserFloatingBannerUIModel, enabled: Boolean = true) {

        if (enabled.not()) {
            return
        }
        y = uiModel.y

        backgroundImageView.setImageResource(uiModel.backgroundDrawableRes)
        nicknameTextView.text = uiModel.nickname
        carNameTextView.text = uiModel.carName

        //做弹幕动画
        val inInterval = 0.16f
        val inIntervalR = 0.13f
        val inSpeed = 3f
        val inValue = inInterval * inSpeed
        val inValueR = inIntervalR * inSpeed
        val middleInterval = 0.95f
        val middleIntervalR = 0.95f
        val middleSpeed = 0.06f
        val middleValue = inValue + (middleInterval - inInterval) * middleSpeed
        val middleValueR = inValueR + (middleIntervalR - inIntervalR) * middleSpeed
        val outSpeed = 2.65f
        val w = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        val h = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        measure(w, h)
        val width = measuredWidth.toFloat()
        animation = if (isRtl) {
            TranslateAnimation(-width, Dimens.screenWidth.toFloat(), 0f, 0f)
        } else {
            TranslateAnimation(Dimens.screenWidth.toFloat(), -width, 0f, 0f)
        }
        animation?.duration = 2500
        animation?.interpolator = Interpolator { v: Float ->
            if (isRtl) {
                if (v < inIntervalR) {

                    return@Interpolator inSpeed * v
                } else if (v < middleIntervalR) {
                    return@Interpolator inValueR + middleSpeed * (v - inIntervalR)
                } else {
                    return@Interpolator middleValueR + outSpeed * (v - middleIntervalR)
                }
            } else {
                if (v < inInterval) {
                    return@Interpolator inSpeed * v
                } else if (v < middleInterval) {
                    return@Interpolator inValue + middleSpeed * (v - inInterval)
                } else {
                    return@Interpolator middleValue + outSpeed * (v - middleInterval)
                }
            }
        }
        animation?.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
                isActiveOfAnimation = true
                visibility = View.VISIBLE
            }

            override fun onAnimationEnd(p0: Animation) {
                isActiveOfAnimation = false
                visibility = View.GONE
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }
        })
        startAnimation(animation)
    }

    override fun onDetachedFromWindow() {
        animation?.cancel()
        super.onDetachedFromWindow()
    }
}
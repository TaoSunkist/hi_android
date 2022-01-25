package me.taosunkist.hello.ui.reusable.dampinghorizonmovablelayout

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.Interpolator
import android.view.animation.TranslateAnimation
import android.widget.LinearLayout
import androidx.fragment.app.FragmentActivity
import com.example.materials.R
import top.thsunkist.appkit.utility.Dimens
import java.util.*

class DampingHorizonMovableView : LinearLayout {


    private var fullBannerUIModel: FullBannerUIModel? = null

    private var eggFullBannerContainer: View? = null

    private var rootContainer: View? = null

    private val uiModels: MutableList<FullBannerUIModel> = ArrayList<FullBannerUIModel>()

    private var playing = false

    var animation: TranslateAnimation? = null

    constructor(context: Context) : super(context, null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    private fun initView() {
        rootContainer = findViewById(R.id.root_container)

        eggFullBannerContainer = View(context).also {
            it.layoutParams = LayoutParams(200, 80)
        }

    }

    private fun updateView() {
        playing = true



        val inInterval = 0.1f
        val inIntervalR = 0.08f
        val inSpeed = 4.71f
        val inValue = inInterval * inSpeed
        val inValueR = inIntervalR * inSpeed
        val middleInterval = 0.8f
        val middleIntervalR = 0.8f
        val middleSpeed = 0.1f
        val middleValue = inValue + (middleInterval - inInterval) * middleSpeed
        val middleValueR = inValueR + (middleIntervalR - inIntervalR) * middleSpeed
        val outSpeed = 3.65f
        val w = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        val h = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        rootContainer!!.measure(w, h)
        val width = rootContainer!!.measuredWidth.toFloat()
        animation = if (isRtl()) {
            TranslateAnimation(-width, Dimens.screenWidth.toFloat(), 0f, 0f)
        } else {
            TranslateAnimation(Dimens.screenWidth.toFloat(), -width, 0f, 0f)
        }
        animation = TranslateAnimation(-width, Dimens.screenWidth.toFloat(), 0f, 0f)
        animation!!.duration = 8000
        animation!!.interpolator = Interpolator { v: Float ->
            if (isRtl()) {
                if (v < inIntervalR) {
                    return@Interpolator inSpeed * v
                } else if (v < middleIntervalR) {
                    return@Interpolator inValueR + middleSpeed * (v - inIntervalR)
                } else {
                    return@Interpolator middleValueR + outSpeed * (v - middleIntervalR)
                }
            } else {
                var s = 0f
                if (v < inInterval) {
                    s = inSpeed * v
                } else if (v < middleInterval) {
                    s = inValue + middleSpeed * (v - inInterval)
                } else {
                    s = middleValue + outSpeed * (v - middleInterval)
                }
                return@Interpolator s
            }
        }
        animation!!.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                rootContainer!!.visibility = VISIBLE
            }

            override fun onAnimationEnd(animation: Animation) {
                rootContainer!!.visibility = INVISIBLE
                playing = false
                startPlay()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        rootContainer!!.startAnimation(animation)
    }


    private fun clear() {
        uiModels.clear()
        if (animation != null) {
            animation!!.cancel()
            animation = null
        }
    }

    fun bind(uiModel: FullBannerUIModel) {
        if (uiModel != null) {
            uiModels.add(uiModel)
            startPlay()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        clear()
    }

    private fun startPlay() {
        if (!playing && uiModels.size > 0) {
            fullBannerUIModel = uiModels[0]
            uiModels.removeAt(0)
            val context = getContext() ?: return
            if (context is FragmentActivity) {
                if (getContext() == null || getContext() !is FragmentActivity) {
                    return
                }
            }
            updateView()
        }
    }
}

fun isRtl(): Boolean {
    return TextUtils.getLayoutDirectionFromLocale(Locale.CHINESE) == View.LAYOUT_DIRECTION_RTL
}


package top.thsunkist.brainhealthy.utilities.animation

import android.util.LayoutDirection
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.text.TextUtilsCompat
import com.facebook.rebound.SimpleSpringListener
import com.facebook.rebound.Spring
import com.facebook.rebound.SpringConfig
import com.facebook.rebound.SpringSystem
import top.thsunkist.brainhealthy.utilities.view.Dimens
import top.thsunkist.brainhealthy.utilities.view.freeAddTimeAnimDirection
import java.util.*

object GiftAnimationUtility {
    /**
     * @param view 礼物小特效
     */
    fun giftTranslation(view: View) {
        val current = 0.0
        val dp = if (TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == LayoutDirection.RTL) 200 else -200
        val end = Dimens.dpToPx(dp).toDouble()
        // 移动
        val springSystem = SpringSystem.create()
        val spring = springSystem.createSpring()
        // SpringConfig springConfig = new SpringConfig(100,200);带减速效果
        val springConfig = SpringConfig(100.0, 30.0)
        spring.springConfig = springConfig
        spring.addListener(object : SimpleSpringListener() {
            override fun onSpringUpdate(spring: Spring) {
                val scale = spring.currentValue.toFloat()
                view.translationX = scale
                if (scale == end.toFloat()) spring.destroy()
            }
        })
        spring.currentValue = current
        spring.endValue = end

        //透明度
        val springAlpha = springSystem.createSpring()
        val springConfigAlpha = SpringConfig(50.0, 30.0)
        springAlpha.springConfig = springConfigAlpha
        springAlpha.addListener(object : SimpleSpringListener() {
            override fun onSpringUpdate(spring: Spring) {
                val scale = spring.currentValue.toFloat()
                view.alpha = scale
                if (scale == 1f) spring.destroy()
            }
        })
        springAlpha.currentValue = 0.0
        springAlpha.endValue = 1.0
    }

    /**
     * @param view 礼物连击
     */
    fun giftCombo(view: View) {
        val springSystem = SpringSystem.create()
        val spring = springSystem.createSpring()
        val springConfig = SpringConfig(100.0, 12.0)
        spring.springConfig = springConfig
        spring.addListener(object : SimpleSpringListener() {
            override fun onSpringUpdate(spring: Spring) {
                val scale = spring.currentValue.toFloat()
                //float scale = 1f - (value * 0.5f);
                view.scaleX = scale
                view.scaleY = scale
                if (scale == 1f) spring.destroy()
            }
        })
        spring.currentValue = 1.5
        spring.endValue = 1.0
    }

    /**
     * 礼物选中动效
     */
    fun giftSelect(view: View?) {
        val springSystem = SpringSystem.create()
        val spring = springSystem.createSpring()
        val springConfig = SpringConfig(100.0, 12.0)
        spring.springConfig = springConfig
        spring.addListener(object : SimpleSpringListener() {
            override fun onSpringUpdate(spring: Spring) {
                val scale = spring.currentValue.toFloat()
                //float scale = 1f - (value * 0.5f);
                if (view != null) {
                    view.scaleX = scale
                    view.scaleY = scale
                }
            }
        })
        spring.currentValue = 0.5
        spring.endValue = 1.0
    }

    fun fullscreenGiftEffect(effectSvga: ImageView?) {
        effectSvga?.let {
            val width: Int
            val height: Int
            var margin = 0
            val scaleWidth: Boolean
            val scale = 0.562f //礼物宽高比
            val screenScale = Dimens.screenWidth.toFloat() / Dimens.screenHeight.toFloat()
            if (screenScale > scale) {
                scaleWidth = false
                //宽占满，调整高度
                height = (Dimens.screenWidth / scale + .5f).toInt()
                width = Dimens.screenWidth
            } else {
                width = (Dimens.screenHeight * scale + .5f).toInt()
                height = Dimens.screenHeight
                margin = (width - Dimens.screenWidth) / 2
                scaleWidth = true
            }
            val layoutParams = effectSvga.layoutParams as LinearLayout.LayoutParams
            if (scaleWidth) {
                layoutParams.width = width
                layoutParams.height = height
                if (freeAddTimeAnimDirection) {
                    layoutParams.setMargins(0, 0, -margin, 0)
                } else {
                    layoutParams.setMargins(-margin, 0, 0, 0)
                }
            } else {
                layoutParams.height = height
                layoutParams.width = width
            }
            effectSvga.layoutParams = layoutParams
        }
    }
}
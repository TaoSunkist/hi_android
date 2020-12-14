package top.thsunkist.brainhealthy.utilities

import android.R
import android.content.res.ColorStateList
import android.graphics.Color

import androidx.annotation.ColorInt

/**
 * Created by EagleDiao on 2016-02-15.
 */
object ColorUtility {

    /**
     * @param color
     * @param degree
     * @return
     */
    fun getDarkenColor(color: Int, degree: Float): Int {
        var degree = degree
        val r = Color.red(color)
        val b = Color.blue(color)
        val g = Color.green(color)
        degree = 1 - degree
        return Color.rgb((r * degree).toInt(), (g * degree).toInt(), (b * degree).toInt())
    }

    /**
     * @param color
     * @param alpha
     * @return
     */
    fun getColorWithAlpha(color: Int, alpha: Float): Int {
        val r = Color.red(color)
        val b = Color.blue(color)
        val g = Color.green(color)
        return Color.argb((255 * alpha).toInt(), r, g, b)
    }

    /**
     * @param degree
     * @return
     */
    fun getWhiteColor(degree: Float): Int {
        val comp = (255 * degree).toInt()
        return Color.rgb(comp, comp, comp)
    }

    /**
     * Produce true if given color is light.
     *
     * @param color color
     * @return true if given color is light, false the color is light
     */
    fun isLightColor(@ColorInt color: Int): Boolean {
        val darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
        return darkness < 0.5
    }

    /**
     * Produce the average of two colors.
     *
     * @param color1 color1
     * @param color2 color2
     * @return average of two colors.
     */
    @ColorInt
    fun getAverageColor(
        @ColorInt color1: Int,
        @ColorInt color2: Int
    ): Int {

        return getTransitionColor(color1, color2, 0.5f)
    }

    /**
     * Produce a color in between two colors with given step.
     *
     * @param fromColor from color
     * @param toColor   to color
     * @param step      step (0 - 1)
     * @return a transition color
     */
    @ColorInt
    fun getTransitionColor(@ColorInt fromColor: Int, @ColorInt toColor: Int, step: Float): Int {

        val fromRed = Color.red(fromColor)
        val fromGreen = Color.green(fromColor)
        val fromBlue = Color.blue(fromColor)

        val toRed = Color.red(toColor)
        val toGreen = Color.green(toColor)
        val toBlue = Color.blue(toColor)

        return Color.argb(
            255,
            (fromRed * step + toRed * (1 - step)).toInt(),
            (fromGreen * step + toGreen * (1 - step)).toInt(),
            (fromBlue * step + toBlue * (1 - step)).toInt()
        )
    }

    val primaryColor = Color.parseColor("#9036ff")
    val backgroundGray = Color.parseColor("#FFFFFF")

    private const val ENABLE_ATTR = R.attr.state_enabled
    private const val CHECKED_ATTR = R.attr.state_checked
    private const val PRESSED_ATTR = R.attr.state_pressed

    fun generateThumbColorWithTintColor(tintColor: Int): ColorStateList? {
        val states = arrayOf(
            intArrayOf(-ENABLE_ATTR, CHECKED_ATTR),
            intArrayOf(-ENABLE_ATTR),
            intArrayOf(PRESSED_ATTR, -CHECKED_ATTR),
            intArrayOf(PRESSED_ATTR, CHECKED_ATTR),
            intArrayOf(CHECKED_ATTR),
            intArrayOf(-CHECKED_ATTR)
        )
        val colors = intArrayOf(
            tintColor - -0x56000000,
            -0x454546,
            tintColor - -0x67000000,
            tintColor - -0x67000000,
            tintColor or -0x1000000,
            -0x111112
        )
        return ColorStateList(states, colors)
    }

    fun generateBackColorWithTintColor(tintColor: Int): ColorStateList? {
        val states = arrayOf(
            intArrayOf(-ENABLE_ATTR, CHECKED_ATTR),
            intArrayOf(-ENABLE_ATTR),
            intArrayOf(CHECKED_ATTR, PRESSED_ATTR),
            intArrayOf(-CHECKED_ATTR, PRESSED_ATTR),
            intArrayOf(CHECKED_ATTR),
            intArrayOf(-CHECKED_ATTR)
        )
        val colors = intArrayOf(
            tintColor - -0x1f000000,
            0x10000000,
            tintColor - -0x30000000,
            0x20000000,
            tintColor - -0x30000000,
            0x20000000
        )
        return ColorStateList(states, colors)
    }
}

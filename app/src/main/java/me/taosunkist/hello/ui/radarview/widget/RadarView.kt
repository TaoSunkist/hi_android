package me.taosunkist.hello.ui.radarview.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat.getColor
import me.taosunkist.hello.R
import me.taosunkist.hello.utility.printf

class RadarView constructor(context: Context, attrs: AttributeSet) : View(context, attrs) {

    companion object {
        @JvmStatic
        val TAG = "RadarView"
    }

    private val circle: Paint

    private val circleDis: Paint

    private val photoPain: Paint

    private var radiusTemp = 0f

    private var radius = 0f

    private var gradientAlpha = 0f

    private var radiusDisTemp = 0f

    private var radiusDis = 0f

    private var alphaDis = 0f

    private var photoRadius = 0f

    private var xc = 0f

    private var yc = 0f

    private var viewHeight = 0

    private var viewWidth = 0

    private var valueAnimator: ValueAnimator = ValueAnimator.ofFloat(0f, 600f)

    private var endListener: EndListener? = null

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        circle.alpha = gradientAlpha.toInt()
        circleDis.alpha = alphaDis.toInt()
        canvas.drawCircle(xc, yc, radiusTemp, circle)
        canvas.drawCircle(xc, yc, radiusDisTemp, circleDis)
        canvas.drawCircle(xc, yc, photoRadius, photoPain)
    }

    fun startLoadingAnimation() {
        if (valueAnimator.isStarted) {
            return
        }

        valueAnimator.addUpdateListener { animation: ValueAnimator ->
            val f = animation.animatedValue as Float
            if (f < 400) {
                radiusTemp = photoRadius + radius * (f / 400)
                gradientAlpha = 255 - 255 * (f / 400)
            }
            if (f >= 200) {
                radiusDisTemp = photoRadius + radiusDis * (f - 200) / 400
                alphaDis = 255 - 255 * (f - 200) / 400
            }
            this@RadarView.invalidate()
            requestLayout()
        }
        valueAnimator.duration = 2000
        valueAnimator.repeatCount = -1
        valueAnimator.repeatMode = ValueAnimator.RESTART
        valueAnimator.start()
    }

    fun stopAnimation() {
        if (valueAnimator.isStarted) {
            valueAnimator.cancel()
            val valueAnim01 = ValueAnimator.ofFloat(0f, 255f)
            valueAnim01.addUpdateListener { animation: ValueAnimator ->
                val value = animation.animatedValue as Float
                alphaDis *= (1 - value / 255)
                gradientAlpha *= (1 - value / 255)
                this.invalidate()
                requestLayout()
            }
            valueAnim01.duration = 2500
            valueAnim01.start()
            if (endListener != null) {
                endListener!!.end()
            }
        }
    }

    val isEnd: Boolean
        get() = !valueAnimator.isRunning

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        viewHeight = MeasureSpec.getSize(heightMeasureSpec)
        xc = (viewWidth / 2).toFloat()
        yc = (viewHeight / 2).toFloat()
        radius = yc - photoRadius
        radiusDis = yc - photoRadius
        photoRadius = 10f
    }

    fun setEndListener(endListener: EndListener?) {
        this.endListener = endListener
    }

    interface EndListener {
        fun end()
    }

    init {
        val color = getColor(resources, R.color.colorPrimary, null)
        circle = Paint()
        circle.color = color
        circleDis = Paint()
        circleDis.color = color
        photoPain = Paint()
        photoPain.color = color
        photoPain.alpha = 100
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAnimation()
        printf("$TAG onDetachedFromWindow")
    }
}
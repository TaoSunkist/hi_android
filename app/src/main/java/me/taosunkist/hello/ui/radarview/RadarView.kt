package me.taosunkist.hello.ui.radarview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.res.ResourcesCompat.getColor
import me.taosunkist.hello.R

class RadarView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val mCircle: Paint
    private val mCircleDis: Paint
    private val mPhotoPain: Paint
    private var mRadiusTemp = 0f
    private var mRadius = 0f
    private var mAlpha = 0f
    private var mRadiusDisTemp = 0f
    private var mRadiusDis = 0f
    private var mAlphaDis = 0f
    private var mPhotoRadius = 0f
    private var xc = 0f
    private var yc = 0f
    private var mHeight = 0
    private var mWidth = 0
    private var va: ValueAnimator = ValueAnimator.ofFloat(0f, 600f)
    private var endListener: EndListener? = null

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mCircle.alpha = mAlpha.toInt()
        mCircleDis.alpha = mAlphaDis.toInt()
        canvas.drawCircle(xc, yc, mRadiusTemp, mCircle)
        canvas.drawCircle(xc, yc, mRadiusDisTemp, mCircleDis)
        canvas.drawCircle(xc, yc, mPhotoRadius, mPhotoPain)
    }

    fun startLoadingAnimation() {
        if (va.isStarted) {
            return
        }

        va.addUpdateListener { animation: ValueAnimator ->
            val f = animation.animatedValue as Float
            if (f < 400) {
                mRadiusTemp = mPhotoRadius + mRadius * (f / 400)
                mAlpha = 255 - 255 * (f / 400)
            }
            if (f >= 200) {
                mRadiusDisTemp = mPhotoRadius + mRadiusDis * (f - 200) / 400
                mAlphaDis = 255 - 255 * (f - 200) / 400
            }
            this@RadarView.invalidate()
            requestLayout()
        }
        va.duration = 4000
        va.repeatCount = 1000
        va.repeatMode = ValueAnimator.RESTART
        va.start()
    }

    fun stopAnimation() {
        if (va.isStarted) {
            va.cancel()
            val valueAnim01 = ValueAnimator.ofFloat(0f, 255f)
            valueAnim01.addUpdateListener { animation: ValueAnimator ->
                val value = animation.animatedValue as Float
                mAlphaDis *= (1 - value / 255)
                mAlpha *= (1 - value / 255)
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
        get() = !va.isRunning

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = MeasureSpec.getSize(widthMeasureSpec)
        mHeight = MeasureSpec.getSize(heightMeasureSpec)
        xc = (mWidth / 2).toFloat()
        yc = (mHeight / 2).toFloat()
        mRadius = yc - mPhotoRadius
        mRadiusDis = yc - mPhotoRadius
        mPhotoRadius = 10f
    }

    fun setEndListener(endListener: EndListener?) {
        this.endListener = endListener
    }

    interface EndListener {
        fun end()
    }

    init {
        val color = getColor(resources, R.color.colorPrimary, null)
        mCircle = Paint()
        mCircle.color = color
        mCircleDis = Paint()
        mCircleDis.color = color
        mPhotoPain = Paint()
        mPhotoPain.color = color
        mPhotoPain.alpha = 100
    }
}
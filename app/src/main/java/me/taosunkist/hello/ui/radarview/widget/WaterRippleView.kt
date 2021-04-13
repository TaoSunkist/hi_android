package me.taosunkist.hello.ui.radarview.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import top.thsunkist.library.utilities.Dimens
import java.util.*

class WaterRippleView : View {
    private var mMaxWaveAreaRadius = 0f
    private var mWaveIntervalSize = 0f//Wave distance = 0f
    private var mStirStep = 0f // The stride of wave movement = 0f
    private var mWidth = 0
    private var mWaveStartWidth = 0f// px = 0f
    private var mWaveEndWidth = 0f// px Maximum radius, over wave disappears = 0f
    private var mWaveColor = 0
    private var mViewCenterX = 0f
    private var mViewCenterY = 0f
    private val rippleColor = Color.BLUE

    //Fluctuating property setting
    private val mWavePaint = Paint()

    //Center point attribute setting
    private val mWaveCenterShapePaint = Paint()
    private var mFillAllView = false
    private var mFillWaveSourceShapeRadius = 0f
    private val mWaves: MutableList<Wave?> = ArrayList()

    constructor(context: Context?, attrs: AttributeSet?) : super(
            context,
            attrs
    ) {
        init()
    }

    constructor(context: Context?) : super(context) {
        init()
    }

    private fun init() {
        setWaveInfo(2f, 1f, 2f, 15f, rippleColor)
        mWaveIntervalSize = Dimens.dpToPx(20).toFloat()
        mWidth = Dimens.dpToPx(2)
        //Initialize the maximum radius of fluctuation
        mWaveEndWidth = Dimens.dpToPx(100).toFloat()
    }

    override fun onLayout(
            changed: Boolean, left: Int, top: Int, right: Int,
            bottom: Int
    ) {
        super.onLayout(changed, left, top, right, bottom)
        mViewCenterX = width / 2.toFloat()
        mViewCenterY = height / 2.toFloat()
        var waveAreaRadius = mMaxWaveAreaRadius
        waveAreaRadius = if (mFillAllView) {
            Math.sqrt(
                    (mViewCenterX * mViewCenterX
                            + mViewCenterY * mViewCenterY).toDouble()
            ).toFloat()
        } else {
            Math.min(mViewCenterX, mViewCenterY)
        }
        if (mMaxWaveAreaRadius != waveAreaRadius) {
            mMaxWaveAreaRadius = waveAreaRadius
            resetWave()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        stir()
        for (w in mWaves) {
            mWavePaint.color = w!!.color
            mWavePaint.strokeWidth = mWidth.toFloat()
            mWavePaint.alpha = w.alpha
            canvas.drawCircle(mViewCenterX, mViewCenterY, w.radius, mWavePaint)
        }
        if (mFillWaveSourceShapeRadius > 0f) {
            canvas.drawCircle(
                    mViewCenterX, mViewCenterY,
                    mFillWaveSourceShapeRadius, mWaveCenterShapePaint
            )
        }
        postInvalidateDelayed(FPS.toLong())
    }

    /**
     * Wave
     *
     */
    internal inner class Wave {
        var radius = 0f
        var width = 0f
        var color = 0
        var alpha = 0
        fun reset() {
            radius = 0f
            width = mWaveStartWidth
            color = mWaveColor
        }

        override fun toString(): String {
            return ("Wave [radius=" + radius + ", width=" + width + ", color="
                    + color + "]")
        }

        init {
            reset()
        }
    }

    private var mLastRmoveWave: Wave? = null

    /**
     * Trigger surge propagation
     */
    private fun stir() {
        val nearestWave = if (mWaves.isEmpty()) null else mWaves[0]
        if (nearestWave == null || nearestWave.radius >= mWaveIntervalSize) {
            var w: Wave? = null
            if (mLastRmoveWave != null) {
                w = mLastRmoveWave
                mLastRmoveWave = null
                w!!.reset()
            } else {
                w = Wave()
            }
            mWaves.add(0, w)
        }
        val waveWidthIncrease = mWaveEndWidth - mWaveStartWidth
        val size = mWaves.size
        for (i in 0 until size) {
            val w = mWaves[i]
            var rP = w!!.radius / mMaxWaveAreaRadius
            if (rP > 1f) {
                rP = 1f
            }
            w.width = mWaveStartWidth + rP * waveWidthIncrease
            w.radius += mStirStep
            w.color = rippleColor
            w.alpha = 255 / (i + 1)
        }
        val farthestWave = mWaves[size - 1]
        if (farthestWave!!.radius > mWaveEndWidth) {
            mWaves.removeAt(size - 1)
        }
    }

    /**
     * If true, the largest diagonal of the view will be selected as the active radius
     *
     */
    fun setFillAllView(fillAllView: Boolean) {
        mFillAllView = fillAllView
        resetWave()
    }

    fun resetWave() {
        mWaves.clear()
        postInvalidate()
    }

    /**
     * Fill the center point of the origin of the waveform
     *
     * @param radius radius
     */
    fun setFillWaveSourceShapeRadius(radius: Float) {
        mFillWaveSourceShapeRadius = radius
    }

    /**
     * Set waveform properties
     *
     * @param intervalSize The interval between two waveforms
     * @param stireStep wave movement speed
     * @param startWidth Start waveform width
     * @param endWidth end waveform width
     * @param color Waveform color
     */
    fun setWaveInfo(
            intervalSize: Float, stireStep: Float,
            startWidth: Float, endWidth: Float, color: Int
    ) {
        mWaveIntervalSize = intervalSize
        mStirStep = stireStep
        mWaveStartWidth = startWidth
        mWaveEndWidth = endWidth
        setWaveColor(color)
        resetWave()
    }

    /**
     * Set the waveform color
     *
     * @param color
     */
    fun setWaveColor(color: Int) {
        mWaveColor = color
        mWaveCenterShapePaint.color = mWaveColor
    }

    companion object {
        private const val FPS = 1000 / 40
    }

    init {
        mWavePaint.isAntiAlias = true
        mWavePaint.style = Paint.Style.FILL
    }

    init {
        mWaveCenterShapePaint.isAntiAlias = true
        mWaveCenterShapePaint.style = Paint.Style.FILL
    }
}

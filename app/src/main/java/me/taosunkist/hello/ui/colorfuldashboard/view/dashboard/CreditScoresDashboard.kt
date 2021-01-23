package me.taosunkist.hello.ui.colorfuldashboard.view.dashboard

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import me.taosunkist.hello.R
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.cos
import kotlin.math.sin


/**
 * Created by Sunkist on 2016/4/8.
 */
class CreditScoresDashboard @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    /**
     * 渐变色扇形条的旋转区域
     */
    private var gradientScallopBarMatrix: Matrix? = null
    /**
     * 仪表盘的全宽
     */
    private var mDashWidth: Float = 0.toFloat()
    /**
     * 仪表盘的全高
     */
    private var mDashHeight: Float = 0.toFloat()
    /**
     * 外部扇形条的颜色
     */
    private var outerArcProgressBarRectF: RectF? = null
    /**
     * 外部灰色背景弧形进度条的画笔
     */
    private var mOuterArcProgressBarPen: Paint? = null
    /**
     * 渐变色的绘制笔
     */
    private var mGradientScallopBarPen: Paint? = null

    private val mColors = intArrayOf(-0x593ff, -0xa5fcf, -0x683cb3, -0xae4894, -0x613bb7, -0x5038bf)
    private var mSweepGradient: SweepGradient? = null
    private var mInnerAcrProgressBarAllSize: Float = 0.toFloat()
    private var mTickMarginBottom: Float = 0.toFloat()
    private var mTickHeight: Float = 0.toFloat()
    private var mTickWidth: Float = 0.toFloat()
    private var mTickFontPen: Paint? = null
    private var mOuterBarStrokeWidth: Float = 0.toFloat()
    private var tickFontSize: Float = 0.toFloat()
    private var mTickFontMargin: Float = 0.toFloat()
    private var mTickFont: Font? = null
    private var gradientScallopBarRectF: RectF? = null
    private var progressSweep: Float = 0.toFloat()
    private var tickFontColor: Int = 0

    private var mTickRectF: RectF? = null

    /**
     * 信用分的最大值，默认300-900
     */
    private val minScores = 0
    private var maxScores = 100
    private val scoresRange = ArrayList<String>(4)


    private var mProgress: Int = 0

    init {
        setupParams()
        if (attrs != null) {
            parseStyleable(context, attrs)
        }
    }


    private fun setupParams() {
        this.mGradientScallopBarPen = Paint(Paint.ANTI_ALIAS_FLAG)
        this.mOuterArcProgressBarPen = Paint(Paint.ANTI_ALIAS_FLAG)
        this.mTickFontPen = Paint(Paint.ANTI_ALIAS_FLAG)
        this.mTickFont = Font()
        this.outerArcProgressBarRectF = RectF()
        this.mTickRectF = RectF()
        this.gradientScallopBarRectF = RectF()
        this.gradientScallopBarMatrix = Matrix()

    }


    /**
     * 获取XML配置参数并初始化默认值
     *
     * @param context
     * @param attrs
     */
    private fun parseStyleable(context: Context, attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.credit_scores_dashboard)

        mTickHeight = typedArray.getDimension(R.styleable.credit_scores_dashboard_tick_height, 0f)
        mTickWidth = typedArray.getDimension(R.styleable.credit_scores_dashboard_tick_width, 0f)
        mTickFontMargin = typedArray.getDimension(R.styleable.credit_scores_dashboard_tick_font_margin, 0f)
        tickFontSize = typedArray.getDimension(R.styleable.credit_scores_dashboard_tick_font_size, 0f)
        //获得刻度的上下距离
        mTickMarginBottom = typedArray.getDimension(R.styleable.credit_scores_dashboard_tick_margin_bottom, 0f)
        //获得设置的内部的滚动Bar的宽度
        mInnerAcrProgressBarAllSize = typedArray.getDimension(R.styleable.credit_scores_dashboard_inner_arc_progress_bar_width, 0f)
        tickFontColor = typedArray.getColor(R.styleable.credit_scores_dashboard_tick_font_color, Color.TRANSPARENT)
        //通过内部的弧形进度控制外部刻度的大小，计算规则为：内部弧形滚动Bar的Size+tick的Size+刻度间距
        typedArray.recycle()
        //全宽=内部扇形进度条的宽度+刻度的高度+刻度的顶部间距+刻度的底部间距+30dp
        mTickFont!!.width = FontUtil.measureFontSize(tickFontSize, maxScores.toString(), true)
        mTickFont!!.height = FontUtil.measureFontSize(tickFontSize, maxScores.toString(), false)
        this.mDashWidth = (mInnerAcrProgressBarAllSize
                + mTickMarginBottom * 2
                + mTickHeight * 2
                + mTickFontMargin * 2
                + (mTickFont!!.width * 2).toFloat())

        mDashHeight = mDashWidth
        this.mOuterBarStrokeWidth = mTickHeight + mTickMarginBottom + mTickMarginBottom / 2
        //刻度的上下边距和高宽
        mTickRectF!!.left = mDashWidth / 2 - mTickWidth
        mTickRectF!!.right = mDashWidth / 2 + mTickWidth
        mTickRectF!!.top = mTickFont!!.width + mTickFontMargin
        mTickRectF!!.bottom = mTickRectF!!.top + mTickHeight

        //计算隐藏的Bar的区域
        this.outerArcProgressBarRectF!!.set(
                mTickFont!!.width.toFloat() + mTickFontMargin + mTickMarginBottom, mTickFont!!.width.toFloat() + mTickFontMargin + mTickMarginBottom, mDashWidth - mTickFont!!.width.toFloat() - mTickFontMargin - mTickMarginBottom, mDashHeight - mTickFont!!.width.toFloat() - mTickFontMargin - mTickMarginBottom
        )

        this.gradientScallopBarRectF!!.set(
                mTickFont!!.width.toFloat(), mTickFont!!.width.toFloat(), mDashWidth - mTickFont!!.width, mDashWidth - mTickFont!!.width)

        this.mSweepGradient = SweepGradient(
                this.mDashWidth / 2, this.mDashWidth / 2, mColors, null)

        this.gradientScallopBarMatrix!!.setRotate(127f, this.mDashWidth / 2, this.mDashWidth / 2)

        this.mSweepGradient!!.setLocalMatrix(this.gradientScallopBarMatrix)

        this.mOuterArcProgressBarPen!!.style = Paint.Style.STROKE
        this.mOuterArcProgressBarPen!!.color = Color.rgb(33, 99, 0xff)
        mOuterArcProgressBarPen!!.strokeWidth = mOuterBarStrokeWidth
        this.mTickFontPen!!.textSize = tickFontSize
        this.mTickFontPen!!.color = tickFontColor
        this.mGradientScallopBarPen!!.shader = this.mSweepGradient
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(
                this.mDashWidth.toInt(), this.mDashHeight.toInt())
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //是否已经设置渐变色背景色
        canvas.drawArc(this.gradientScallopBarRectF!!, ANGLE_START.toFloat(), ANGLE_END.toFloat(), false, this.mGradientScallopBarPen!!)
        canvas.drawArc(outerArcProgressBarRectF!!, ANGLE_START - 2 + progressSweep, ANGLE_END + 4 - progressSweep, false, mOuterArcProgressBarPen!!)
        val atomicBoolean = AtomicBoolean(true)
        val atomicInteger = AtomicInteger(9)
        canvas.save()
        for (i in 0..30) {
            if (i < 15) {
                atomicInteger.addAndGet(9)
                canvas.rotate(-9f, mDashWidth / 2, mDashHeight / 2)
            } else {
                if (atomicBoolean.getAndSet(false)) {
                    canvas.rotate((atomicInteger.get() - 9).toFloat(), mDashWidth / 2, mDashHeight / 2)
                } else {
                    canvas.rotate(9f, mDashWidth / 2, mDashHeight / 2)
                }
            }
            canvas.clipRect(0f, 0f, mDashWidth, mDashHeight)
            canvas.clipRect(mTickRectF!!, Region.Op.DIFFERENCE)
        }
        canvas.drawColor(Color.WHITE)
        canvas.restore()

        var x = 0f
        var y = 0f
        /** 初中数学知识 */
        x = (mDashWidth / 2 + mDashWidth / 2 * cos(135 * Math.PI / 180)).toFloat()
        y = (mDashWidth / 2 + mDashWidth / 2 * Math.sin(135 * Math.PI / 180)).toFloat()
        canvas.drawText(minScores.toString(), x - mTickFont!!.width + mTickFont!!.width / 2, y - mTickFont!!.height, mTickFontPen!!)

        x = (mDashWidth / 2 + mDashWidth / 2 * cos(189 * Math.PI / 180)).toFloat()
        y = (mDashWidth / 2 + mDashWidth / 2 * sin(189 * Math.PI / 180)).toFloat()
        canvas.drawText(if (scoresRange.size != 0) scoresRange[0] else "", x, y, mTickFontPen!!)

        x = (mDashWidth / 2 + mDashWidth / 2 * cos(234 * Math.PI / 180)).toFloat()
        y = (mDashWidth / 2 + mDashWidth / 2 * sin(234 * Math.PI / 180)).toFloat()
        canvas.drawText(if (scoresRange.size != 0) scoresRange[1] else "", x, y, mTickFontPen!!)

        x = (mDashWidth / 2 + mDashWidth / 2 * cos(306 * Math.PI / 180)).toFloat()
        y = (mDashWidth / 2 + mDashWidth / 2 * sin(306 * Math.PI / 180)).toFloat()
        canvas.drawText(if (scoresRange.size != 0) scoresRange[2] else "", x - mTickFont!!.width, y, mTickFontPen!!)

        x = (mDashWidth / 2 + mDashWidth / 2 * cos(351 * Math.PI / 180)).toFloat()
        y = (mDashWidth / 2 + mDashWidth / 2 * sin(351 * Math.PI / 180)).toFloat()
        canvas.drawText(if (scoresRange.size != 0) scoresRange[3] else "", x - mTickFont!!.width, y, mTickFontPen!!)

        x = (mDashWidth / 2 + mDashWidth / 2 * cos(405 * Math.PI / 180)).toFloat()
        y = (mDashWidth / 2 + mDashWidth / 2 * sin(405 * Math.PI / 180)).toFloat()
        canvas.drawText(maxScores.toString(), x - mTickFont!!.width + mTickFont!!.width / 2, y - mTickFont!!.height, mTickFontPen!!)
    }

    fun setProgressValue(progressValue: Float, progressMaxValue: Float) {
        this.progressSweep = progressValue / progressMaxValue * (ANGLE_END + 4)
        invalidate()
    }

    fun setProgress(progress: Int) {
        this.mProgress = progress
        invalidate()
    }

    fun setMaxProgress(maxProgrexx: Int) {
        this.maxScores = maxProgrexx
    }

    companion object {
        /* 起点角度 */
        private val ANGLE_START = 135
        /* 终点角度 */
        private val ANGLE_END = 270
    }
}













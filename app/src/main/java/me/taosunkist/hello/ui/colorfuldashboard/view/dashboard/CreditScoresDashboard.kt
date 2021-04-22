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
    private var dashboardWidth: Float = 0.toFloat()

    /**
     * 仪表盘的全高
     */
    private var dashboardHeight: Float = 0.toFloat()

    /**
     * 外部扇形条的颜色
     */
    private var outerArcProgressBarRectF: RectF? = null

    /**
     * 外部灰色背景弧形进度条的画笔
     */
    private var outerArcProgressBarPen: Paint? = null

    /**
     * 渐变色的绘制笔
     */
    private var gradientScallopBarPen: Paint? = null

    private val mColors = intArrayOf(-0x593ff, -0xa5fcf, -0x683cb3, -0xae4894, -0x613bb7, -0x5038bf)
    private var mSweepGradient: SweepGradient? = null
    private var mInnerAcrProgressBarAllSize: Float = 0.toFloat()
    private var mTickMarginBottom: Float = 0.toFloat()
    private var tickHeight: Float = 0.toFloat()
    private var tickWidth: Float = 0.toFloat()
    private var tickFontPen: Paint? = null
    private var mOuterBarStrokeWidth: Float = 0.toFloat()
    private var tickFontSize: Float = 0.toFloat()
    private var tickFontMargin: Float = 0.toFloat()
    private var tickFont: Font? = null
    private var gradientScallopBarRectF: RectF? = null
    private var progressSweep: Float = 0.toFloat()
    private var tickFontColor: Int = 0

    private var tickRectF: RectF? = null

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
        this.gradientScallopBarPen = Paint(Paint.ANTI_ALIAS_FLAG)
        this.outerArcProgressBarPen = Paint(Paint.ANTI_ALIAS_FLAG)
        this.tickFontPen = Paint(Paint.ANTI_ALIAS_FLAG)
        this.tickFont = Font()
        this.outerArcProgressBarRectF = RectF()
        this.tickRectF = RectF()
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

        tickHeight = typedArray.getDimension(R.styleable.credit_scores_dashboard_tick_height, 0f)
        tickWidth = typedArray.getDimension(R.styleable.credit_scores_dashboard_tick_width, 0f)
        tickFontMargin = typedArray.getDimension(R.styleable.credit_scores_dashboard_tick_font_margin, 0f)
        tickFontSize = typedArray.getDimension(R.styleable.credit_scores_dashboard_tick_font_size, 0f)
        //获得刻度的上下距离
        mTickMarginBottom = typedArray.getDimension(R.styleable.credit_scores_dashboard_tick_margin_bottom, 0f)
        //获得设置的内部的滚动Bar的宽度
        mInnerAcrProgressBarAllSize = typedArray.getDimension(R.styleable.credit_scores_dashboard_inner_arc_progress_bar_width, 0f)
        tickFontColor = typedArray.getColor(R.styleable.credit_scores_dashboard_tick_font_color, Color.TRANSPARENT)
        //通过内部的弧形进度控制外部刻度的大小，计算规则为：内部弧形滚动Bar的Size+tick的Size+刻度间距
        typedArray.recycle()
        //全宽=内部扇形进度条的宽度+刻度的高度+刻度的顶部间距+刻度的底部间距+30dp
        tickFont!!.width = FontUtil.measureFontSize(tickFontSize, maxScores.toString()).first
        tickFont!!.height = FontUtil.measureFontSize(tickFontSize, maxScores.toString()).second
        this.dashboardWidth = (mInnerAcrProgressBarAllSize
                + mTickMarginBottom * 2
                + tickHeight * 2
                + tickFontMargin * 2
                + (tickFont!!.width * 2).toFloat())

        dashboardHeight = dashboardWidth
        this.mOuterBarStrokeWidth = tickHeight + mTickMarginBottom + mTickMarginBottom / 2
        //刻度的上下边距和高宽
        tickRectF!!.left = dashboardWidth / 2 - tickWidth
        tickRectF!!.right = dashboardWidth / 2 + tickWidth
        tickRectF!!.top = tickFont!!.width + tickFontMargin
        tickRectF!!.bottom = tickRectF!!.top + tickHeight

        //计算隐藏的Bar的区域
        this.outerArcProgressBarRectF!!.set(
                tickFont!!.width.toFloat() + tickFontMargin + mTickMarginBottom, tickFont!!.width.toFloat() + tickFontMargin + mTickMarginBottom, dashboardWidth - tickFont!!.width.toFloat() - tickFontMargin - mTickMarginBottom, dashboardHeight - tickFont!!.width.toFloat() - tickFontMargin - mTickMarginBottom
        )

        this.gradientScallopBarRectF!!.set(
                tickFont!!.width.toFloat(), tickFont!!.width.toFloat(), dashboardWidth - tickFont!!.width, dashboardWidth - tickFont!!.width)

        this.mSweepGradient = SweepGradient(
                this.dashboardWidth / 2, this.dashboardWidth / 2, mColors, null)

        this.gradientScallopBarMatrix!!.setRotate(127f, this.dashboardWidth / 2, this.dashboardWidth / 2)

        this.mSweepGradient!!.setLocalMatrix(this.gradientScallopBarMatrix)

        this.outerArcProgressBarPen!!.style = Paint.Style.STROKE
        this.outerArcProgressBarPen!!.color = Color.GRAY
        this.outerArcProgressBarPen!!.strokeWidth = mOuterBarStrokeWidth

        this.tickFontPen!!.textSize = tickFontSize
        this.tickFontPen!!.color = tickFontColor
        this.gradientScallopBarPen!!.shader = this.mSweepGradient
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(
                this.dashboardWidth.toInt(), this.dashboardHeight.toInt())
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        /* 设置渐变色背景色 */
        outerArcProgressBarPen?.color = gradientScallopBarPen?.color ?: Color.BLACK
        canvas.drawArc(this.gradientScallopBarRectF!!, ANGLE_START.toFloat(), ANGLE_END.toFloat(), false, this.gradientScallopBarPen!!)
        canvas.drawArc(outerArcProgressBarRectF!!, ANGLE_START - 2 + progressSweep, ANGLE_END + 4 - progressSweep, false, outerArcProgressBarPen!!)

        val atomicBoolean = AtomicBoolean(true)
        val atomicInteger = AtomicInteger(9)

        canvas.save()
        for (i in 0..30) {
            if (i < 15) {
                atomicInteger.addAndGet(9)
                canvas.rotate(-9f, dashboardWidth / 2, dashboardHeight / 2)
            } else {
                if (atomicBoolean.getAndSet(false)) {
                    canvas.rotate((atomicInteger.get() - 9).toFloat(), dashboardWidth / 2, dashboardHeight / 2)
                } else {
                    canvas.rotate(9f, dashboardWidth / 2, dashboardHeight / 2)
                }
            }
            canvas.clipRect(0f, 0f, dashboardWidth, dashboardHeight)
            canvas.clipRect(tickRectF!!, Region.Op.DIFFERENCE)
        }
        canvas.drawColor(Color.WHITE)
        canvas.restore()

        var x = 0f
        var y = 0f

        x = (dashboardWidth / 2 + dashboardWidth / 2 * cos(135 * Math.PI / 180)).toFloat()
        y = (dashboardWidth / 2 + dashboardWidth / 2 * Math.sin(135 * Math.PI / 180)).toFloat()
        canvas.drawText(minScores.toString(), x - tickFont!!.width + tickFont!!.width / 2, y - tickFont!!.height, tickFontPen!!)

        x = (dashboardWidth / 2 + dashboardWidth / 2 * cos(189 * Math.PI / 180)).toFloat()
        y = (dashboardWidth / 2 + dashboardWidth / 2 * sin(189 * Math.PI / 180)).toFloat()
        canvas.drawText(if (scoresRange.size != 0) scoresRange[0] else "", x, y, tickFontPen!!)

        x = (dashboardWidth / 2 + dashboardWidth / 2 * cos(234 * Math.PI / 180)).toFloat()
        y = (dashboardWidth / 2 + dashboardWidth / 2 * sin(234 * Math.PI / 180)).toFloat()
        canvas.drawText(if (scoresRange.size != 0) scoresRange[1] else "", x, y, tickFontPen!!)

        x = (dashboardWidth / 2 + dashboardWidth / 2 * cos(306 * Math.PI / 180)).toFloat()
        y = (dashboardWidth / 2 + dashboardWidth / 2 * sin(306 * Math.PI / 180)).toFloat()
        canvas.drawText(if (scoresRange.size != 0) scoresRange[2] else "", x - tickFont!!.width, y, tickFontPen!!)

        x = (dashboardWidth / 2 + dashboardWidth / 2 * cos(351 * Math.PI / 180)).toFloat()
        y = (dashboardWidth / 2 + dashboardWidth / 2 * sin(351 * Math.PI / 180)).toFloat()
        canvas.drawText(if (scoresRange.size != 0) scoresRange[3] else "", x - tickFont!!.width, y, tickFontPen!!)

        x = (dashboardWidth / 2 + dashboardWidth / 2 * cos(405 * Math.PI / 180)).toFloat()
        y = (dashboardWidth / 2 + dashboardWidth / 2 * sin(405 * Math.PI / 180)).toFloat()
        canvas.drawText(maxScores.toString(), x - tickFont!!.width + tickFont!!.width / 2, y - tickFont!!.height, tickFontPen!!)
    }

    fun setProgressValue(progressValue: Float, progressMaxValue: Float) {
        this.progressSweep = progressValue / progressMaxValue * (ANGLE_END + 4)
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
package me.taosunkist.hello.ui.colorfuldashboard.view.dashboard

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View

import me.taosunkist.hello.R

class ArcProgressBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    /* 弧形进度的区域 */
    private var innerArcBarRectF: RectF? = null
    /* 起点角度 */
    private var startAngel: Int = 0
    /* 终点角度 */
    private var endAngel: Int = 0
    /* 弧形条的傻笔 */
    private var arcBarPen: Paint? = null
    /* 弧形进度的宽度 */
    private var strokeWidth: Int = 0
    /* 最大进度值 */
    private var allSize: Float = 0.toFloat()
    private var centerFontPen: Paint? = null
    private var bottomFontPen: Paint? = null
    private var belowFontPen: Paint? = null
    private var mPts: FloatArray? = null

    /* 正中心字体的尺寸 */
    private var centerFont: Font? = null
    private var belowFont: Font? = null
    private var bottomFont: Font? = null
    private val mArcProgressBarColor: Int = 0


    /* 进度值 */
    private var progressValue = 0f
    /* 进度角度 */
    private var progressSweep: Int = 0
    private var progressMaxValue = 100

    init {
        setupParams()
        if (attrs != null) {
            parseAttrs(context, attrs)
        }
    }

    private fun parseAttrs(context: Context, attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.arc_progress_bar)
        this.startAngel = typedArray.getInteger(R.styleable.arc_progress_bar_angel_start, 0)
        this.endAngel = typedArray.getInteger(R.styleable.arc_progress_bar_angel_end, 360)
        this.strokeWidth = typedArray.getDimension(R.styleable.arc_progress_bar_stroke_width, 0f).toInt()

        val centerFontSize = typedArray.getDimension(R.styleable.arc_progress_bar_center_font_size, 0f)
        val belowFontSize = typedArray.getDimension(R.styleable.arc_progress_bar_below_font_size, 0f)
        val bottomFontSize = typedArray.getDimension(R.styleable.arc_progress_bar_bottom_font_size, 0f)

        val centerFontColor = typedArray.getColor(R.styleable.arc_progress_bar_center_font_color, Color.TRANSPARENT)
        val belowFontColor = typedArray.getColor(R.styleable.arc_progress_bar_below_font_color, Color.TRANSPARENT)
        val bottomFontColor = typedArray.getColor(R.styleable.arc_progress_bar_bottom_font_color, Color.TRANSPARENT)
        allSize = typedArray.getDimension(R.styleable.arc_progress_bar_all_size, 0f)
        typedArray.recycle()

        innerArcBarRectF!!.set((0 + strokeWidth / 2).toFloat(), (0 + strokeWidth / 2).toFloat(), allSize - strokeWidth / 2, allSize - strokeWidth / 2)

        centerFont!!.fontSize = centerFontSize
        centerFont!!.fontColor = centerFontColor

        belowFont!!.fontSize = belowFontSize
        belowFont!!.fontColor = belowFontColor

        bottomFont!!.fontSize = bottomFontSize
        bottomFont!!.fontColor = bottomFontColor

        centerFontPen!!.textSize = centerFont!!.fontSize
        bottomFontPen!!.textSize = bottomFont!!.fontSize
        belowFontPen!!.textSize = belowFont!!.fontSize

        mPts = floatArrayOf(allSize / 2 - getDpValue(16), allSize / 2 - getDpValue(14), allSize / 2 - getDpValue(51), allSize / 2 - getDpValue(14), allSize / 2 + getDpValue(16), allSize / 2 - getDpValue(14), allSize / 2 + getDpValue(51), allSize / 2 - getDpValue(14))

    }

    /**
     * 装载参数
     */
    fun setupParams() {
        this.innerArcBarRectF = RectF()
        this.arcBarPen = Paint(Paint.ANTI_ALIAS_FLAG)
        centerFontPen = Paint(Paint.ANTI_ALIAS_FLAG)
        bottomFontPen = Paint(Paint.ANTI_ALIAS_FLAG)
        belowFontPen = Paint(Paint.ANTI_ALIAS_FLAG)

        centerFontPen!!.textAlign = Paint.Align.CENTER
        bottomFontPen!!.textAlign = Paint.Align.CENTER
        belowFontPen!!.textAlign = Paint.Align.CENTER

        centerFont = Font()
        belowFont = Font()
        bottomFont = Font()
        //测试数据
        centerFont!!.setContent(0.toString())
        belowFont!!.setContent("获取中")
        bottomFont!!.setContent("获取中")
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        this.arcBarPen!!.style = Paint.Style.STROKE
        this.arcBarPen!!.color = Color.BLUE
        this.arcBarPen!!.strokeCap = Paint.Cap.ROUND
        this.arcBarPen!!.strokeWidth = this.strokeWidth.toFloat()
        canvas.drawArc(innerArcBarRectF!!, startAngel.toFloat(), progressSweep.toFloat(), false, arcBarPen!!)
        if (!TextUtils.isEmpty(centerFont!!.getContent())) {
            canvas.drawText(centerFont!!.getContent(), 0, centerFont!!.getContent().length, this.allSize / 2, this.allSize / 2 + centerFont!!.height / 2 - belowFont!!.height, centerFontPen!!)
        } else {
            centerFontPen!!.strokeWidth = centerFont!!.fontSize / 13f
            canvas.drawLines(mPts!!, centerFontPen!!)
        }

        canvas.drawText(belowFont!!.getContent(), 0, belowFont!!.getContent().length, this.allSize / 2, this.allSize / 2 + centerFont!!.height, belowFontPen!!)

        canvas.drawText(bottomFont!!.getContent(), 0, bottomFont!!.getContent().length, this.allSize / 2, this.allSize - bottomFont!!.height * 2.3f, bottomFontPen!!)

        canvas.drawColor(Color.TRANSPARENT)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(allSize.toInt(), allSize.toInt())
    }

    private fun getDpValue(w: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, w.toFloat(), context.resources.displayMetrics).toInt()
    }

    fun setProgress(progress: Int) {
        this.progressValue = progress.toFloat()
        val ratio = progressValue / progressMaxValue
        progressSweep = (ratio * endAngel).toInt()
        Log.d("ProgressBarValue", "" + progressSweep)
        this.invalidate()
    }

    fun setMaxProgress(maxProgress: Int) {
        this.progressMaxValue = maxProgress
    }
}

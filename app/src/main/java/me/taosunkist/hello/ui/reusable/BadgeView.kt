package me.taosunkist.hello.ui.reusable

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import me.taosunkist.hello.ui.colorfuldashboard.view.dashboard.FontUtil
import top.thsunkist.appkit.utility.printf

class BadgeView : View {

    /**
    Paint.ANTI_ALIAS_FLAG ：抗锯齿标志
    Paint.FILTER_BITMAP_FLAG : 使位图过滤的位掩码标志
    Paint.DITHER_FLAG : 使位图进行有利的抖动的位掩码标志
    Paint.UNDERLINE_TEXT_FLAG : 下划线
    Paint.STRIKE_THRU_TEXT_FLAG : 中划线
    Paint.FAKE_BOLD_TEXT_FLAG : 加粗
    Paint.LINEAR_TEXT_FLAG : 使文本平滑线性扩展的油漆标志
    Paint.SUBPIXEL_TEXT_FLAG : 使文本的亚像素定位的绘图标志
    Paint.EMBEDDED_BITMAP_TEXT_FLAG : 绘制文本时允许使用位图字体的绘图标志
     */
    private var contentPaint: Paint = Paint().apply {
        color = Color.BLACK
        textSize = contentTextSize
    }
    private val contentTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12f, resources.displayMetrics);

    val contentRect = Rect(0, 0, 0, 0)

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        setOnClickListener { invalidate() }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val content = (0..Int.MAX_VALUE).random()
        val size = FontUtil.measureFontSize(contentTextSize, "$content")
        contentRect.left = 0
        contentRect.right = size.first
        contentRect.top = 0
        contentRect.bottom = size.second
        canvas.drawRect(contentRect, Paint().apply {
            color = Color.RED
        })
        canvas.drawText("$content", 0f, 0f, contentPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
        setMeasuredDimension(200,200)
        setBackgroundColor(Color.BLUE)
        printf("taohui onMeasure ${measuredWidth}, $measuredHeight")
    }
}
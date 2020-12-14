package top.thsunkist.brainhealthy.ui.reusable.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import top.thsunkist.brainhealthy.utilities.view.Dimens

class BorderedImageView : AppCompatImageView {

    private val borderPaint = Paint().apply {
    }
    private val imageRect = RectF()

    /* TODO gift selected box round */
    open var borderColor: Int = Color.parseColor("#F1F1F1")
        set(value) {
            field = value
            borderPaint.color = value
            invalidate()
        }

    private var borderWidth: Float = Dimens.dpToPx(1).toFloat()
        set(value) {
            field = value
            borderPaint.strokeWidth = value
            invalidate()
        }

    private var drawBorder: Boolean = true
        set(value) {
            field = value
            invalidate()
        }

    private var cornerRadius: Int = Dimens.marginMedium
        set(value) {
            field = value
            invalidate()
        }

    constructor(context: Context) : super(context) {
        commonInit()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        commonInit()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        commonInit()
    }

    private fun commonInit() {
        setWillNotDraw(false)
        setPadding(
            Dimens.marginSmall,
            Dimens.marginSmall,
            Dimens.marginSmall,
            Dimens.marginSmall
        )
        borderPaint.color = borderColor
        borderPaint.strokeWidth = borderWidth
        borderPaint.style = Paint.Style.STROKE
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        imageRect.set(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        if (drawBorder.not()) {
            return
        }
        canvas?.let {
            canvas.drawRoundRect(
                imageRect,
                cornerRadius.toFloat(),
                cornerRadius.toFloat(),
                borderPaint
            )
        }
    }
}
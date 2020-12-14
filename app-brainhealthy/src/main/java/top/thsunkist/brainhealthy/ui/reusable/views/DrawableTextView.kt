package top.thsunkist.brainhealthy.ui.reusable.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.annotation.IntDef
import androidx.appcompat.widget.AppCompatTextView
import top.thsunkist.brainhealthy.R
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy


/**
 * @author deadline
 *
 *
 * 解决drawable不与文字居中的问题
 */
class DrawableTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
) :
    AppCompatTextView(context, attrs, defStyleAttr) {
    private var drawables: Array<Drawable?>
    private var widths: IntArray
    private var heights: IntArray

    @IntDef(
        LEFT,
        TOP,
        RIGHT,
        BOTTOM
    )
    @Retention(RetentionPolicy.SOURCE)
    annotation class DrawGravity

    fun setDrawable(
        @DrawGravity gravity: Int,
        drawable: Drawable?,
        width: Int,
        height: Int
    ) {
        drawables[gravity] = drawable
        widths[gravity] = width
        heights[gravity] = height
        postInvalidate()
    }

    fun setDrawables(drawables: Array<Drawable?>?, widths: IntArray?, heights: IntArray?) {
        if (drawables != null && drawables.size >= 4 && widths != null && widths.size >= 4 && heights != null && heights.size >= 4
        ) {
            this.drawables = drawables
            this.widths = widths
            this.heights = heights
            postInvalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        val drawablePadding = compoundDrawablePadding
        translateText(canvas, drawablePadding)
        super.onDraw(canvas)
        val centerX = (width + paddingLeft - paddingRight) / 2.toFloat()
        val centerY = (height + paddingTop - paddingBottom) / 2.toFloat()
        val halfTextWidth =
            paint.measureText(if (text.toString().isEmpty()) hint.toString() else text.toString()) / 2
        val fontMetrics = paint.fontMetrics
        val halfTextHeight = (fontMetrics.descent - fontMetrics.ascent) / 2
        if (drawables[0] != null) {
            val left = (centerX - drawablePadding - halfTextWidth - widths[0]).toInt()
            val top = (centerY - heights[0] / 2).toInt()
            drawables[0]!!.setBounds(
                left,
                top,
                left + widths[0],
                top + heights[0]
            )
            canvas.save()
            drawables[0]!!.draw(canvas)
            canvas.restore()
        }
        if (drawables[2] != null) {
            val left = (centerX + halfTextWidth + drawablePadding).toInt()
            val top = (centerY - heights[2] / 2).toInt()
            drawables[2]!!.setBounds(
                left,
                top,
                left + widths[2],
                top + heights[2]
            )
            canvas.save()
            drawables[2]!!.draw(canvas)
            canvas.restore()
        }
        if (drawables[1] != null) {
            val left = (centerX - widths[1] / 2).toInt()
            val bottom = (centerY - halfTextHeight - drawablePadding).toInt()
            drawables[1]!!.setBounds(
                left,
                bottom - heights[1],
                left + widths[1],
                bottom
            )
            canvas.save()
            drawables[1]!!.draw(canvas)
            canvas.restore()
        }
        if (drawables[3] != null) {
            val left = (centerX - widths[3] / 2).toInt()
            val top = (centerY + halfTextHeight + drawablePadding).toInt()
            drawables[3]!!.setBounds(
                left,
                top,
                left + widths[3],
                top + heights[3]
            )
            canvas.save()
            drawables[3]!!.draw(canvas)
            canvas.restore()
        }
    }

    private fun translateText(canvas: Canvas, drawablePadding: Int) {
        var translateWidth = 0
        if (drawables[0] != null && drawables[2] != null) {
            translateWidth = (widths[0] - widths[2]) / 2
        } else if (drawables[0] != null) {
            translateWidth = (widths[0] + drawablePadding) / 2
        } else if (drawables[2] != null) {
            translateWidth = -(widths[2] + drawablePadding) / 2
        }
        var translateHeight = 0
        if (drawables[1] != null && drawables[3] != null) {
            translateHeight = (heights[1] - heights[3]) / 2
        } else if (drawables[1] != null) {
            translateHeight = (heights[1] + drawablePadding) / 2
        } else if (drawables[3] != null) {
            translateHeight = -(heights[3] - drawablePadding) / 2
        }
        canvas.translate(translateWidth.toFloat(), translateHeight.toFloat())
    }

    companion object {
        const val LEFT = 0
        const val TOP = 1
        const val RIGHT = 2
        const val BOTTOM = 3
    }

    init {
        drawables = arrayOfNulls(4)
        widths = IntArray(4)
        heights = IntArray(4)
        gravity = Gravity.CENTER
        val array = context.obtainStyledAttributes(attrs, R.styleable.DrawableTextView)
        drawables[0] = array.getDrawable(R.styleable.DrawableTextView_leftDrawable)
        drawables[1] = array.getDrawable(R.styleable.DrawableTextView_topDrawable)
        drawables[2] = array.getDrawable(R.styleable.DrawableTextView_rightDrawable)
        drawables[3] = array.getDrawable(R.styleable.DrawableTextView_bottomDrawable)
        widths[0] = array.getDimensionPixelSize(R.styleable.DrawableTextView_leftDrawableWidth, 0)
        widths[1] = array.getDimensionPixelSize(R.styleable.DrawableTextView_topDrawableWidth, 0)
        widths[2] = array.getDimensionPixelSize(R.styleable.DrawableTextView_rightDrawableWidth, 0)
        widths[3] = array.getDimensionPixelSize(R.styleable.DrawableTextView_bottomDrawableWidth, 0)
        heights[0] = array.getDimensionPixelSize(R.styleable.DrawableTextView_leftDrawableHeight, 0)
        heights[1] = array.getDimensionPixelSize(R.styleable.DrawableTextView_topDrawableHeight, 0)
        heights[2] = array.getDimensionPixelSize(R.styleable.DrawableTextView_rightDrawableHeight, 0)
        heights[3] = array.getDimensionPixelSize(R.styleable.DrawableTextView_bottomDrawableHeight, 0)
        array.recycle()
    }
}
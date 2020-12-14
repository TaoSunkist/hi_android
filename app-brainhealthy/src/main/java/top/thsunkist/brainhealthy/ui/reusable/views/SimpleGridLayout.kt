package top.thsunkist.brainhealthy.ui.reusable.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.annotation.AttrRes
import top.thsunkist.brainhealthy.R
import top.thsunkist.brainhealthy.utilities.view.Dimens
import top.thsunkist.brainhealthy.utilities.view.makeAtMostMeasure
import top.thsunkist.brainhealthy.utilities.view.makeExactlyMeasure
import kotlin.math.min

class SimpleGridLayout : ViewGroup {

    var horizontalSpacing: Int = 0
    var verticalSpacing: Int = 0
    var itemHeight: Int = -1
    var columnCount: Int = 2
    var showDivider: Boolean = false
        set(newValue) {
            field = newValue
            setWillNotDraw(newValue.not())
        }
    private var dividerWidth: Float = Dimens.dpToPx(1).toFloat()
        set(width) {
            field = width
            dividerPaint.strokeWidth = width
        }
    private var dividerColor: Int = Color.parseColor("#E4E4E4")
        set(color) {
            field = color
            dividerPaint.color = color
        }
    private var dividerPaint = Paint()

    constructor(context: Context) : super(context) {
        commonInit(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        commonInit(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        commonInit(context, attrs)
    }

    /**
     * Common init with nullable attributes.
     *
     * @param context context
     * @param attrs   attributes
     */
    private fun commonInit(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.SimpleGridLayout)
            horizontalSpacing = a.getDimension(R.styleable.SimpleGridLayout_horizontalSpacing, 0f).toInt()
            verticalSpacing = a.getDimension(R.styleable.SimpleGridLayout_verticalSpacing, 0f).toInt()
            itemHeight = a.getDimension(R.styleable.SimpleGridLayout_itemHeight, -1f).toInt()
            columnCount = a.getInt(R.styleable.SimpleGridLayout_columnCount, 2)
            showDivider = a.getBoolean(R.styleable.SimpleGridLayout_showDivider, showDivider)
            dividerWidth = a.getDimension(R.styleable.SimpleGridLayout_dividerWidth, dividerWidth)
            dividerColor = a.getColor(R.styleable.SimpleGridLayout_dividerColor, dividerColor)

            a.recycle()
        }
        setWillNotDraw(showDivider.not())
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)

        val itemWidth = (width - paddingStart - paddingEnd - horizontalSpacing * (columnCount - 1)) / columnCount
        var totalHeight = -verticalSpacing
        var lineHeight = 0

        for (index in 0 until childCount) {
            if (index % columnCount == 0) {
                totalHeight += lineHeight + verticalSpacing
                lineHeight = 0
            }
            val child = getChildAt(index)
            val heightSpec = if (itemHeight > 0) makeExactlyMeasure(itemHeight) else makeAtMostMeasure(height)
            child.measure(makeExactlyMeasure(itemWidth), heightSpec)
            lineHeight = Math.max(lineHeight, child.measuredHeight)
        }

        totalHeight = Math.max(0, totalHeight)
        totalHeight += lineHeight + paddingTop + paddingBottom
        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
            resolveMeasureSpec(totalHeight, heightMeasureSpec)
        )
    }

    private fun resolveMeasureSpec(wanted: Int, measureSpec: Int): Int {
        val measure = MeasureSpec.getSize(measureSpec)
        return when (MeasureSpec.getMode(measureSpec)) {
            MeasureSpec.AT_MOST -> {
                Math.min(measure, wanted)
            }
            else -> {
                measure
            }
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val itemWidth = (right - left - paddingStart - paddingEnd - horizontalSpacing * (columnCount - 1)) / columnCount
        var currX = paddingStart
        var currY = paddingTop - verticalSpacing
        var maxLineHeight = 0
        for (index in 0 until childCount) {
            if (index % columnCount == 0) {
                /* New Row */
                currX = paddingStart
                currY += maxLineHeight + verticalSpacing
                maxLineHeight = 0
            }

            val child = getChildAt(index)
            child.layout(currX, currY, currX + itemWidth, currY + child.measuredHeight)
            maxLineHeight = child.measuredHeight.coerceAtLeast(maxLineHeight)
            currX += itemWidth + horizontalSpacing
        }
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        if (showDivider.not()) {
            return
        }
        canvas?.let {
            val rowCount = childCount / columnCount + min(1, childCount % columnCount)
            val xStart = paddingLeft.toFloat()
            val xEnd = (canvas.width - paddingRight).toFloat()
            val yStart = paddingTop.toFloat()
            val yBottom = (canvas.height - paddingBottom).toFloat()
            val rowHeight = (yBottom - yStart) / rowCount
            val columnWidth = (xEnd - xStart) / columnCount
            for (row in 0 until rowCount) {
                val currY = yStart + rowHeight * row
                for (col in 1 until columnCount) {

                    it.drawLine(
                        xStart + col * columnWidth - dividerWidth / 2,
                        currY,
                        xStart + col * columnWidth - dividerWidth / 2,
                        currY + rowHeight,
                        dividerPaint
                    )
                }
                /* Horizontal */
                it.drawLine(
                    xStart,
                    currY,
                    xEnd,
                    currY,
                    dividerPaint
                )
            }
            it.drawLine(
                xStart,
                yBottom - dividerWidth,
                xEnd,
                yBottom - dividerWidth,
                dividerPaint
            )
        }
    }
}
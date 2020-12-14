package top.thsunkist.brainhealthy.ui.reusable

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import top.thsunkist.brainhealthy.R
import kotlin.math.roundToInt

class DrawableSizeTextView : AppCompatTextView {
    private var mDrawableWidth = 0
    private var mDrawableHeight = 0

    constructor(context: Context) : super(context) {
        init(context, null, 0, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs, 0, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr, 0)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.TextViewDrawableSize, defStyleAttr, defStyleRes)
        try {
            mDrawableWidth = array.getDimensionPixelSize(R.styleable.TextViewDrawableSize_compoundDrawableWidth, -1)
            mDrawableHeight = array.getDimensionPixelSize(R.styleable.TextViewDrawableSize_compoundDrawableHeight, -1)
        } finally {
            array.recycle()
        }
        if (mDrawableWidth > 0 || mDrawableHeight > 0) {
            initCompoundDrawableSize()
        }
    }

    private fun initCompoundDrawableSize() {
        val drawables = compoundDrawablesRelative
        for (drawable in drawables) {
            if (drawable == null) {
                continue
            }
            val realBounds = drawable.bounds
            val scaleFactor = realBounds.height() / realBounds.width().toFloat()
            var drawableWidth = realBounds.width().toFloat()
            var drawableHeight = realBounds.height().toFloat()
            if (mDrawableWidth > 0) {
                if (drawableWidth > mDrawableWidth) {
                    drawableWidth = mDrawableWidth.toFloat()
                    drawableHeight = drawableWidth * scaleFactor
                }
            }
            if (mDrawableHeight > 0) {
                if (drawableHeight > mDrawableHeight) {
                    drawableHeight = mDrawableHeight.toFloat()
                    drawableWidth = drawableHeight / scaleFactor
                }
            }
            realBounds.right = realBounds.left + drawableWidth.roundToInt()
            realBounds.bottom = realBounds.top + drawableHeight.roundToInt()
            drawable.bounds = realBounds
        }
        setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], drawables[2], drawables[3])
    }
}
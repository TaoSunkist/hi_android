package top.thsunkist.brainhealthy.ui.reusable.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.Shape
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import top.thsunkist.brainhealthy.R
import top.thsunkist.brainhealthy.utilities.ColorUtility.getTransitionColor
import top.thsunkist.brainhealthy.utilities.view.Dimens

class PageIndicator : LinearLayout {
    constructor(context: Context) : super(context) {
        commonInit(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        commonInit(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        commonInit(context, attrs)
    }

    private lateinit var mDots: Array<View?>
    private lateinit var mDotDrawables: Array<DotDrawable?>
    private var mDefaultDotColor = 0
    private var mSelectedDotColor = 0
    private var mRealItemCount = 0
    private var mDotSize = 0
    private var mDotMargin = 0
    private fun commonInit(context: Context, attributeSet: AttributeSet?) {
        mDefaultDotColor = Color.WHITE
        mSelectedDotColor = Color.RED
        mDotSize = Dimens.dpToPx(4)
        mDotMargin = 8
        if (attributeSet != null) {
            val a =
                context.obtainStyledAttributes(attributeSet, R.styleable.PageIndicator)
            mDefaultDotColor =
                a.getColor(R.styleable.PageIndicator_default_color, mDefaultDotColor)
            mSelectedDotColor =
                a.getColor(R.styleable.PageIndicator_selected_color, mSelectedDotColor)
            mDotSize = a.getDimensionPixelSize(R.styleable.PageIndicator_dot_size, mDotSize)
            mDotMargin = a.getDimensionPixelSize(R.styleable.PageIndicator_dot_margin, mDotMargin)
            a.recycle()
        }
    }

    fun setDefaultDotColor(color: Int) {
        mDefaultDotColor = color
    }

    fun setDotSize(size: Int) {
        mDotSize = size
    }

    fun setDotMargin(size: Int) {
        mDotMargin = size
    }

    fun setSelectedDotColor(color: Int) {
        mSelectedDotColor = color
    }

    private fun obtainLayoutParams(): LayoutParams {
        val size = mDotSize
        val margin = mDotMargin
        val lp = LayoutParams(size, size)
        if (orientation == HORIZONTAL) {
            lp.gravity = Gravity.CENTER_VERTICAL
            lp.leftMargin = margin
            lp.rightMargin = margin
        } else {
            lp.gravity = Gravity.CENTER_HORIZONTAL
            lp.topMargin = margin
            lp.bottomMargin = margin
        }
        return lp
    }

    fun setPageCount(itemCount: Int) {
        mRealItemCount = itemCount
        mDotDrawables = arrayOfNulls(itemCount)
        mDots = arrayOfNulls(itemCount)
        for (i in 0 until itemCount) {
            val view = View(context)
            mDotDrawables[i] = DotDrawable(if (i == 0) mSelectedDotColor else mDefaultDotColor)
            mDots[i] = view
            view.background = mDotDrawables[i]
            view.layoutParams = obtainLayoutParams()
            addView(view)
        }
    }

    private var mCurrentPage = 0
    private var mCurrentOffset = 0f
    fun onPageScrolled(position: Int, positionOffset: Float) {
        var position = position
        val adapterPosition = position
        position = position % mRealItemCount
        mCurrentPage = position
        mCurrentOffset = positionOffset
        for (i in mDotDrawables.indices) {
            val colorChanged: Boolean
            colorChanged = if (i == mCurrentPage) {
                mDotDrawables[i]!!.setColor(
                    getTransitionColor(
                        mSelectedDotColor,
                        mDefaultDotColor,
                        1 - mCurrentOffset
                    )
                )
            } else if (i == mCurrentPage + 1 || adapterPosition > mRealItemCount && i == (mCurrentPage + 1) % mRealItemCount) {
                mDotDrawables[i]!!.setColor(
                    getTransitionColor(
                        mSelectedDotColor,
                        mDefaultDotColor,
                        mCurrentOffset
                    )
                )
            } else {
                mDotDrawables[i]!!.setColor(mDefaultDotColor)
            }
            if (colorChanged) {
                mDots[i]!!.invalidate()
            }
        }
    }

    inner class DotDrawable(color: Int) :
        ShapeDrawable(OvalShape()) {
        private var mColor = 0
        override fun onDraw(
            shape: Shape,
            canvas: Canvas,
            paint: Paint
        ) {
            paint.color = mColor
            super.onDraw(shape, canvas, paint)
        }

        fun setColor(color: Int): Boolean {
            if (mColor == color) {
                return false
            }
            mColor = color
            return true
        }
    }
}
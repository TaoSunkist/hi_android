package me.taosunkist.hello.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import me.taosunkist.hello.R
import top.thsunkist.appkit.utility.Dimens

class RoomTicketEnabledMinimumView : View {


    private var viewWidth: Int = 0

    private var viewHeight: Int = 0

    val paint = Paint().apply {
        color = Color.BLUE
        textSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, 10f, resources.displayMetrics
        )
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        setBackgroundResource(R.drawable.icon_default_inviter_avatar)
        val drawable: Drawable = ContextCompat.getDrawable(context, R.drawable.icon_default_inviter_avatar)!!
        viewWidth = Dimens.dpToPx(drawable.intrinsicWidth / 2)
        viewHeight = Dimens.dpToPx(drawable.intrinsicHeight / 2)
        setMeasuredDimension(viewWidth, viewHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val textSize = measureFontSize(paint.textSize, "$number")
        canvas.drawText(
            "$number",
            (viewWidth / 2 - textSize.first / 2).toFloat(), (viewHeight - textSize.second).toFloat(), paint
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(viewWidth, viewHeight)
    }

    var number: Int = 111

    fun bind(number: Int) {
        this.number = number
        invalidate()
    }

    private fun measureFontSize(fontSize: Float, str: String): Pair<Int, Int> {
        val pen = Paint()
        if (TextUtils.isEmpty(str)) {
            return Pair(0, 0)
        }
        pen.textSize = fontSize
        val rect = Rect()
        pen.getTextBounds(str, 0, str.length, rect)
        return Pair(rect.width(), rect.height())
    }

    protected var mDownTimeMillis: Long = 0

    protected val mbottomViewHeight: Int by lazy {
        val resourceId = this.resources.getIdentifier("status_bar_height", "dimen", "android")
        Dimens.dpToPx(50) + (if (resourceId > 0) {
            this.resources.getDimensionPixelSize(resourceId)
        } else {
            Dimens.dpToPx(20)
        })
    }

    protected var xDelta = 0

    protected var yDelta = 0

    open var completion: (() -> Unit)? = null

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.rawX.toInt()
        val y = event.rawY.toInt()
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                mDownTimeMillis = System.currentTimeMillis()
                val params = this.layoutParams as ConstraintLayout.LayoutParams
                xDelta = x - params.leftMargin
                yDelta = y - params.topMargin
            }
            MotionEvent.ACTION_MOVE -> {
                val layoutParams = layoutParams as ConstraintLayout.LayoutParams
                val width = viewWidth
                val height = layoutParams.height
                var xDistance = x - xDelta
                var yDistance = y - yDelta
                val outX = Dimens.screenWidth - width - 10
                if (xDistance > outX) {
                    xDistance = outX
                }
                val outY = Dimens.screenHeight - height - mbottomViewHeight
                if (yDistance > outY) {
                    yDistance = outY
                }
                if (xDistance < 10) {
                    xDistance = 10
                }
                if (yDistance < 100) {
                    yDistance = 100
                }
                layoutParams.marginEnd = xDistance
                layoutParams.topMargin = yDistance
                this.layoutParams = layoutParams
            }
            MotionEvent.ACTION_UP -> parseMotionEvent(this)
        }
        return true
    }

    protected fun parseMotionEvent(view: View) {
        if (System.currentTimeMillis() - mDownTimeMillis > 150) {
            return
        }
        if (view.visibility != View.VISIBLE) {
            return
        }

        completion?.invoke()
    }

    fun setFloatViewClickListener(completion: (() -> Unit)? = null) {
        this.completion = completion
    }

    open fun hide() {
        if (visibility != View.GONE) {
            visibility = View.GONE
        }
    }

    open fun show() {
        if (visibility != View.VISIBLE) {
            visibility = View.VISIBLE
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        val parent = parent as ConstraintLayout
        val layoutParams = layoutParams as ConstraintLayout.LayoutParams
        val width = layoutParams.width
        val mw: Int = resources.displayMetrics.widthPixels - viewWidth

        layoutParams.marginStart = mw
        layoutParams.topMargin = 0
        setLayoutParams(layoutParams)
        parent.invalidate()
    }

}
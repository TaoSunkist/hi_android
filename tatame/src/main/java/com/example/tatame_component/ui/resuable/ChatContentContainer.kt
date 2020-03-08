package com.example.tatame_component.ui.resuable

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.FrameLayout
import com.example.tatame_component.utility.Dimens

class ChatContentContainer : FrameLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val dotLength = Dimens.dpToPx(8).toFloat()
    private val paint = Paint().apply {
        color = Color.RED
    }
    var showDot: Boolean = false
        set(value) {
            if (field != value) {
                field = value
                invalidate()
            }
        }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        if (childCount > 0 && showDot) {
            val lastChild = getChildAt(childCount - 1)
            canvas?.drawCircle(lastChild.right + Dimens.marginXSmall + dotLength / 2,
                    dotLength / 2,
                    dotLength / 2,
                    paint)
        }
    }
}
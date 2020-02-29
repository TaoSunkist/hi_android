package com.example.tatame_component.ui.resuable

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.example.tatame_component.R

class ChatView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0,
        defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyle, defStyleRes) {

    private val toolbar: View by lazy {
        findViewById<View>(R.id.view_controller_chat_toolbar)
    }
    private val contentView: View by lazy {
        findViewById<View>(R.id.common_id_swipe_refresh_layout)
    }
    private val keyboardView: View by lazy {
        findViewById<View>(R.id.view_controller_chat_input_view)
    }
    private val keyboardContainer: View by lazy {
        findViewById<View>(R.id.view_controller_chat_keyboard_container)
    }

    init {
        clipChildren = false
        clipToPadding = false
    }

    var showKeyboardContainer: Boolean = false
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    var keyboardContainerHeight: Int = 0
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

//    private fun isKeyboardVisible(height: Int): Boolean {
//        return height < Dimens.screenHeight - Dimens.dpToPx(150)
//    }
//
//
//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        val width = MeasureSpec.getSize(widthMeasureSpec)
//        val height = MeasureSpec.getSize(heightMeasureSpec)
//
//        measureChild(toolbar, widthMeasureSpec, heightMeasureSpec)
//        measureChild(keyboardView, widthMeasureSpec, heightMeasureSpec)
//        measureChild(keyboardContainer,
//                makeExactlyMeasure(width),
//                makeExactlyMeasure(keyboardContainerHeight))
//        if (showKeyboardContainer && isKeyboardVisible(height).not()) {
//            contentView.measure(
//                    makeExactlyMeasure(width),
//                    makeExactlyMeasure(height - toolbar.measuredHeight -
//                            keyboardView.measuredHeight - keyboardContainerHeight))
//        } else {
//            contentView.measure(
//                    makeExactlyMeasure(width),
//                    makeExactlyMeasure(height - toolbar.measuredHeight - keyboardView.measuredHeight))
//
//        }
//        setMeasuredDimension(width, height)
//    }
//
//    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
//        super.onLayout(changed, l, t, r, b)
//
//        val width = r - l
//        val height = b - t
//
//        toolbar.setFrame(x = 0, y = 0, width = width, height = toolbar.measuredHeight)
//
//        val offset =
//                if (isKeyboardVisible(height) || showKeyboardContainer.not())
//                    0
//                else
//                    this.keyboardContainerHeight
//
//        contentView.setFrame(
//                x = 0,
//                y = toolbar.measuredHeight,
//                width = width,
//                height = height - toolbar.measuredHeight - keyboardView.measuredHeight - offset)
//        keyboardView.setFrame(
//                x = 0,
//                y = height - keyboardView.measuredHeight - offset,
//                width = width,
//                height = keyboardView.measuredHeight
//        )
//
//        if (isKeyboardVisible(height) || showKeyboardContainer.not()) {
//            keyboardContainer.setFrame(
//                    x = 0,
//                    y = height,
//                    width = width,
//                    height = keyboardContainerHeight
//            )
//        } else {
//            keyboardContainer.setFrame(
//                    x = 0,
//                    y = height - keyboardContainerHeight,
//                    width = width,
//                    height = keyboardContainerHeight
//            )
//        }
//    }
}

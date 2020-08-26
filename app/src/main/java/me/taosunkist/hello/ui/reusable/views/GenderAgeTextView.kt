package me.taosunkist.hello.ui.reusable.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import me.taosunkist.hello.Dimens
import me.taosunkist.hello.utility.printf

class GenderAgeTextView : AppCompatTextView {
	constructor(context: Context?) : super(context)
	constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
	constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

	init {
		/* 没有text的时候drawpadding也必须清空 */

	}

	override fun onDraw(canvas: Canvas?) {
		super.onDraw(canvas)
		printf("taohui, ${text} ${compoundDrawables}")
		if (text.isNotEmpty()) {
		} else if (compoundDrawablePadding != 0) compoundDrawablePadding = 0
	}

	override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
		super.onLayout(changed, left, top, right, bottom)
	}

	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec)
	}

	override fun setCompoundDrawablesWithIntrinsicBounds(left: Drawable?, top: Drawable?, right: Drawable?, bottom: Drawable?) {
		super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom)
	}

	override fun setCompoundDrawablesWithIntrinsicBounds(left: Int, top: Int, right: Int, bottom: Int) {
		super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom)
	}

	override fun setText(text: CharSequence, type: BufferType) {
		super.setText(text, type)
	}
}
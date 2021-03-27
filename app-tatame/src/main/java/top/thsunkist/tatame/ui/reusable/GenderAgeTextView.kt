package top.thsunkist.tatame.ui.reusable

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import top.thsunkist.tatame.utilities.Dimens

class GenderAgeTextView : AppCompatTextView {
	constructor(context: Context) : super(context)
	constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
	constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

	init {
		if (text.isEmpty()) {
			compoundDrawablePadding = 0
			setPadding(Dimens.dpToPx(6), 0, Dimens.dpToPx(5), 0)
		} else {
			setPadding(Dimens.dpToPx(6), 0, Dimens.dpToPx(5), 0)
			compoundDrawablePadding = Dimens.dpToPx(2)
		}
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
		if (text.isEmpty()) {
			compoundDrawablePadding = 0
			setPadding(Dimens.dpToPx(6), 0, Dimens.dpToPx(5), 0)
		} else {
			setPadding(Dimens.dpToPx(6), 0, Dimens.dpToPx(5), 0)
			compoundDrawablePadding = Dimens.dpToPx(2)
		}
	}

	override fun setText(text: CharSequence, type: BufferType) {
		super.setText(text, type)
		if (text.isEmpty()) {
			compoundDrawablePadding = 0
			setPadding(Dimens.dpToPx(6), 0, Dimens.dpToPx(5), 0)
		} else {
			setPadding(Dimens.dpToPx(6), 0, Dimens.dpToPx(5), 0)
			compoundDrawablePadding = Dimens.dpToPx(2)
		}
	}
}
package top.thsunkist.brainhealthy.ui.reusable.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import top.thsunkist.brainhealthy.utilities.view.Dimens

class GenderAgeTextView : AppCompatTextView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        if (text.isEmpty()) {
            compoundDrawablePadding = 0
            setPadding(Dimens.dpToPx(6), 0, Dimens.dpToPx(6), 0)
        } else {
            setPadding(Dimens.dpToPx(6), 0, Dimens.dpToPx(6), 0)
            compoundDrawablePadding = Dimens.dpToPx(2)
        }
    }

    override fun setCompoundDrawablesWithIntrinsicBounds(left: Int, top: Int, right: Int, bottom: Int) {
        super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom)
        if (text.isNullOrEmpty()) {
            compoundDrawablePadding = 0
            setPadding(Dimens.dpToPx(6), 0, Dimens.dpToPx(6), 0)
        } else {
            setPadding(Dimens.dpToPx(6), 0, Dimens.dpToPx(6), 0)
            compoundDrawablePadding = Dimens.dpToPx(2)
        }
    }

    override fun setText(text: CharSequence?, type: BufferType) {
        super.setText(text, type)
        if (text.isNullOrEmpty()) {
            compoundDrawablePadding = 0
            setPadding(Dimens.dpToPx(6), 0, Dimens.dpToPx(6), 0)
        } else {
            setPadding(Dimens.dpToPx(6), 0, Dimens.dpToPx(6), 0)
            compoundDrawablePadding = Dimens.dpToPx(2)
        }
    }
}
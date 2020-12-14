package top.thsunkist.brainhealthy.utilities.view

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.RadioGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.TextUtilsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.get
import top.thsunkist.brainhealthy.ui.reusable.uimodels.ImageUIModel
import com.squareup.picasso.Picasso
import java.util.*

fun View.setFrame(x: Int, y: Int, width: Int, height: Int) {
    layout(x, y, x + width, y + height)
}

fun View.makeExactlyMeasure(dimension: Int): Int {
    return View.MeasureSpec.makeMeasureSpec(dimension, View.MeasureSpec.EXACTLY)
}

fun View.makeAtMostMeasure(dimension: Int): Int {
    return View.MeasureSpec.makeMeasureSpec(dimension, View.MeasureSpec.AT_MOST)
}

fun AppCompatTextView.setDrawableLeft(drawable: Drawable?) {
    setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
}

fun AppCompatTextView.setDrawableRight(drawable: Drawable?) {
    setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
}

fun AppCompatImageView.bind(model: ImageUIModel) {
    when {
        model.imageRes != null -> {
            setImageResource(model.imageRes!!)
            if (model.targetWidth > 0) {
                layoutParams.width = model.targetWidth
            }
        }
        model.imageUrl?.isNotEmpty() ?: false -> {
            val requestCreator = Picasso.get().load(model.imageUrl);
            model.placeholder?.let {
                requestCreator.placeholder(it)
            }
            requestCreator.apply {
                if (model.targetWidth > 0 || model.targetHeight > 0) {
                    resize(model.targetWidth, model.targetHeight)
                    onlyScaleDown()
                }
                into(this@bind)
            }
        }
        model.placeholder != null -> {
            setImageResource(model.placeholder!!)
        }
    }
}

fun RadioGroup.eachViewGroup(
    rechargeRadioGroup: RadioGroup = this,
    each: ((childIndex: Int, childView: AppCompatRadioButton) -> Unit)
) {
    var radioButtonIndex = 0
    for (childIndex in 0 until rechargeRadioGroup.childCount) {
        val childView = rechargeRadioGroup[childIndex]
        if (childView is AppCompatRadioButton) {
            each(radioButtonIndex++, childView)
        } else {
            continue
        }
    }
}

val freeAddTimeAnimDirection: Boolean
    get() {
        return TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == ViewCompat.LAYOUT_DIRECTION_RTL
    }

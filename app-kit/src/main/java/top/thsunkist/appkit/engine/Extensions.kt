package top.thsunkist.appkit.engine

import androidx.appcompat.widget.AppCompatImageView
import com.squareup.picasso.Picasso
import top.thsunkist.appkit.R

fun AppCompatImageView.bind(model: ImageUIModel) {
    when {
        model.imageRes != null -> setImageResource(model.imageRes!!)
        model.imageUrl?.isNotEmpty() ?: false -> {
            Picasso.get().load(model.imageUrl)
                .placeholder(model.placeholder ?: R.drawable.placeholder_image).apply {
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
package me.taosunkist.hello.utility

import android.content.Context
import android.graphics.Color
import android.widget.Toast
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getDrawable
import es.dmoral.toasty.Toasty
import me.taosunkist.hello.R

object ToastyExt {


    @SuppressWarnings("unused")
    fun normal(context: Context?, content: String, showIcon: Boolean = false): Toast? {
        return context?.let {
            Toasty.custom(
                it,
                content,
                null,
                getColor(it, R.color.colorPrimary),
                Color.WHITE,
                Toast.LENGTH_SHORT,
                showIcon,
                false
            )
        }
    }

    @SuppressWarnings("unused")
    fun success(context: Context?, content: String, showIcon: Boolean = false): Toast? {
        return context?.let {
            Toasty.custom(
                it,
                content,
                getDrawable(it, R.drawable.ic_check_white_24dp),
                getColor(it, R.color.colorPrimary),
                Color.WHITE,
                Toast.LENGTH_SHORT,
                showIcon,
                false
            )
        }
    }

    @SuppressWarnings("unused")
    fun error(context: Context?, content: String, showIcon: Boolean = false): Toast? {
        return context?.let {
            Toasty.custom(
                it,
                content,
                getDrawable(it, R.drawable.ic_clear_white_24dp),
                getColor(it, R.color.colorPrimary),
                Color.WHITE,
                Toast.LENGTH_SHORT,
                showIcon,
                false
            )
        }
    }

    init {
        Toasty.Config.getInstance()
            .tintIcon(true)
            .setTextSize(14)
            .allowQueue(true)
            .apply()
    }
}


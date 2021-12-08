package me.taosunkist.hello.ui.textspan

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.*
import android.util.AttributeSet
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import top.thsunkist.appkit.utility.StringUtils

@DslMarker
internal annotation class InlineDsl

@JvmSynthetic
@InlineDsl
inline fun createSpanStyle(
    textView: TextView,
    crossinline block: SpanStyleBuilder.() -> Unit,
): Any = SpanStyleBuilder().apply(block).build(textView)

/**
 * keywords[0] -> XXXSpan
 */
@InlineDsl
class SpanStyleBuilder {

    lateinit var matchResultSequence: Sequence<MatchResult>

    lateinit var spannableStringBuilder: SpannableStringBuilder

    fun build(textView: TextView): Any {
        spannableStringBuilder = SpannableStringBuilder(textView.text)
        matchResultSequence.forEach {
            it.let {
                spannableStringBuilder.setSpan(ForegroundColorSpan(Color.parseColor("#454343")),
                    it.range.first,
                    it.range.last + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

                spannableStringBuilder.setSpan(BackgroundColorSpan(Color.parseColor("#A12323")),
                    it.range.first,
                    it.range.last + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

                spannableStringBuilder.setSpan(RelativeSizeSpan(2f),
                    it.range.first,
                    it.range.last + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        textView.text = spannableStringBuilder

        return Any()
    }
}


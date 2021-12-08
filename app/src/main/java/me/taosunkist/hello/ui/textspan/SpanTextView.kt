package me.taosunkist.hello.ui.textspan

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.text.style.StyleSpan
import android.text.style.TypefaceSpan
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.graphics.TypefaceCompat
import top.thsunkist.appkit.utility.StringUtils

data class SpanTextUIModel(val keywords: Array<out String>, val textContent: String) {
    companion object {
        fun init(vararg keywords: String, textContent: String): SpanTextUIModel {
            return SpanTextUIModel(keywords = keywords, textContent = textContent)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SpanTextUIModel

        if (!keywords.contentEquals(other.keywords)) return false
        if (textContent != other.textContent) return false

        return true
    }

    override fun hashCode(): Int {
        var result = keywords.contentHashCode()
        result = 31 * result + textContent.hashCode()
        return result
    }
}

@DslMarker
internal annotation class InlineDsl

@JvmSynthetic
@InlineDsl
inline fun createSpanTextStyle(
    crossinline block: SpanTextView.Builder.() -> Unit,
): Any = SpanTextView.Builder().apply(block).build()


class SpanTextView : AppCompatTextView {

    /**
     * keywords[0] -> XXXSpan
     */
    @InlineDsl
    class Builder {

        lateinit var keywords: Array<out String>

        var textContent: String = ""

        val spannableStringBuilder: SpannableStringBuilder = SpannableStringBuilder(textContent)

        var foregroundColorSpan: ForegroundColorSpan? = null

        fun build(): Any {
            keywords.forEach {
                val keywordRangePair = StringUtils.findKeyWordPositionInTextPart(textContent, it)
                foregroundColorSpan?.let {
                    spannableStringBuilder.setSpan(it,
                        keywordRangePair.first,
                        keywordRangePair.last,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }
            return Any()
        }

    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun bind(uiModel: SpanTextUIModel) {
        val spannableStringBuilder = SpannableStringBuilder(uiModel.textContent)

        uiModel.keywords.forEach {
            val keywordRangePair = StringUtils.findKeyWordPositionInTextPart(uiModel.textContent, it)

            spannableStringBuilder.setSpan(ForegroundColorSpan(Color.parseColor("#000000")),
                keywordRangePair.first,
                keywordRangePair.last,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            spannableStringBuilder.setSpan(StyleSpan(Typeface.BOLD_ITALIC),
                keywordRangePair.first,
                keywordRangePair.last,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        text = spannableStringBuilder
    }
}
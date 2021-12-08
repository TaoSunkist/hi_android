package me.taosunkist.hello.ui.textspan

import android.content.Context
import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
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

class SpanTextView : AppCompatTextView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun bind(uiModel: SpanTextUIModel) {
        val spannableStringBuilder = SpannableStringBuilder(uiModel.textContent)

        uiModel.keywords.forEach {
            val keywordRangePair = StringUtils.findKeyWordPositionInTextPart(uiModel.textContent, it)

            spannableStringBuilder.setSpan(ForegroundColorSpan(Color.parseColor("#000000")),
                keywordRangePair.first,
                keywordRangePair.second,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        text = spannableStringBuilder
    }
}
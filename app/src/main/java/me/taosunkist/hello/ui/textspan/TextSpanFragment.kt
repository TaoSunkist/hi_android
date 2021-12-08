package me.taosunkist.hello.ui.textspan

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.LineBackgroundSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.buildSpannedString
import androidx.core.text.set
import androidx.core.text.toSpannable
import androidx.core.text.toSpanned
import com.mooveit.library.Fakeit
import me.taosunkist.hello.R
import me.taosunkist.hello.databinding.FragmentTextSpanBinding
import top.thsunkist.appkit.utility.StringUtils

class TextSpanFragment : Fragment() {

    private var _binding: FragmentTextSpanBinding? = null

    private val binding: FragmentTextSpanBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = FragmentTextSpanBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val matchResult = "a".toRegex().find("Biden warns Putin of sanctions, aid for Ukraine military if Russia invades")

        val buildSpannedString = buildSpannedString {

            append("Biden warns Putin of sanctions, aid for Ukraine military if Russia invades")

            setSpan(ForegroundColorSpan(Color.parseColor("#454343")),
                matchResult?.range?.start ?: 0,
                (matchResult?.range?.endInclusive ?: 0) + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            setSpan(BackgroundColorSpan(Color.parseColor("#A12323")),
                matchResult?.range?.first ?: 0,
                (matchResult?.range?.endInclusive ?: 0) + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        binding.normalSpanTextView.text = buildSpannedString


    }
}
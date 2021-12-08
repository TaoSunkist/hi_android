package me.taosunkist.hello.ui.textspan

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import me.taosunkist.hello.R
import me.taosunkist.hello.databinding.FragmentTextSpanBinding
import me.taosunkist.hello.ui.BaseFragment
import top.thsunkist.appkit.utility.StringUtil

class TextSpanFragment : Fragment() {

    private var _binding: FragmentTextSpanBinding? = null

    private val binding: FragmentTextSpanBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = FragmentTextSpanBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nick = "taohui"
        val targetNick = "wangbo"
        val roomID = System.currentTimeMillis()
        val result = getString(R.string.s_s_send_to_, nick, roomID.toString(), targetNick)

        val spannableStringBuilder = SpannableStringBuilder(result)
        spannableStringBuilder.setSpan(ForegroundColorSpan(Color.parseColor("#000000")), 0, nick.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        val roomIDPositionPair = StringUtil.findKeyWordPositionInTextPart(result, roomID.toString())
        spannableStringBuilder.setSpan(ForegroundColorSpan(Color.parseColor("#000000")),
            roomIDPositionPair.first, roomIDPositionPair.second, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        val targetNickPositionPair = StringUtil.findKeyWordPositionInTextPart(result, targetNick)
        spannableStringBuilder.setSpan(ForegroundColorSpan(Color.parseColor("#000000")),
            targetNickPositionPair.first, targetNickPositionPair.second, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.normalSpanTextView.text = spannableStringBuilder


    }

    companion object {

        @JvmStatic
        fun newInstance() =
            TextSpanFragment().apply {}
    }
}
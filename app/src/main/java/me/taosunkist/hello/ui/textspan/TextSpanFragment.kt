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

        binding.normalSpanTextView.bind(SpanTextUIModel.init(
            keywords = arrayOf("Biden", "Putin", "Ukraine", "Russia"),
            textContent = "Biden warns Putin of sanctions, aid for Ukraine military if Russia invades")
        )
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            TextSpanFragment().apply {}
    }
}
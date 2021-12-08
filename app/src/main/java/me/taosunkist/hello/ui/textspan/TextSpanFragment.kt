package me.taosunkist.hello.ui.textspan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.taosunkist.hello.databinding.FragmentTextSpanBinding

class TextSpanFragment : Fragment() {

    private var _binding: FragmentTextSpanBinding? = null

    private val binding: FragmentTextSpanBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = FragmentTextSpanBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textContent =
            "Biden warns Putin of sanctions, aid for Ukraine military if Russia invades http://google.com is google website and http://youtube.com is youtube site"

        binding.normalSpanTextView.text = textContent

        createSpanStyle(binding.normalSpanTextView) {
            this.matchResultSequence = "(if)|(for)|(is)".toRegex().findAll(textContent)
        }
    }
}
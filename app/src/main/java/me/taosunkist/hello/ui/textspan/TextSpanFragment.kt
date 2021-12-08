package me.taosunkist.hello.ui.textspan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import me.taosunkist.hello.R
import me.taosunkist.hello.databinding.FragmentTextSpanBinding
import me.taosunkist.hello.ui.BaseFragment

class TextSpanFragment : Fragment() {

    private var _binding: FragmentTextSpanBinding? = null

    private val binding: FragmentTextSpanBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = FragmentTextSpanBinding.inflate(inflater, container, false).also { _binding = it }.root

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TextSpanFragment().apply {}
    }
}
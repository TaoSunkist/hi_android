package me.taosunkist.hello.ui.debug

import me.taosunkist.hello.R
import me.taosunkist.hello.databinding.FragmentDebugBinding
import me.taosunkist.hello.ui.BaseFragment
import kotlin.reflect.KClass

class DebugFragment : BaseFragment<FragmentDebugBinding>(R.layout.fragment_debug) {

    companion object {

        @JvmStatic
        fun newInstance() = DebugFragment().apply {}
    }

    override fun FragmentDebugBinding.onCreateView(binding: FragmentDebugBinding) {
    }
}

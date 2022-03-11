package me.taosunkist.hello.ui.debug

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.taosunkist.hello.R
import me.taosunkist.hello.databinding.FragmentDebugBinding
import me.taosunkist.hello.ui.BaseFragment

class DebugFragment : BaseFragment() {

    companion object {

        @JvmStatic
        fun newInstance() = DebugFragment().apply {}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentDebugBinding.inflate(inflater, container, false).root
    }
}

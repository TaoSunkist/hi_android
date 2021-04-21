package me.taosunkist.hello.ui.service

import android.content.Context
import android.net.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import me.taosunkist.hello.databinding.SystemServiceFragmentBinding
import me.taosunkist.hello.ui.BaseFragment
import me.taosunkist.hello.utility.initConnectivityManager
import me.taosunkist.hello.utility.registerNetworkCallback
import top.thsunkist.tatame.utilities.weak

class SystemServiceFragment : BaseFragment() {

    companion object {
        fun newInstance() = SystemServiceFragment()
    }

    private lateinit var viewModel: ViewModel

    private lateinit var networkCallback: ConnectivityManager.NetworkCallback

    private var connectivityManager: ConnectivityManager? by weak()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            SystemServiceFragmentBinding.inflate(inflater, container, false).root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(ViewModel::class.java)

        connectivityManager = initConnectivityManager(requireContext())

        networkCallback = registerNetworkCallback()
    }

    override fun onDestroy() {
        super.onDestroy()

        connectivityManager?.unregisterNetworkCallback(networkCallback)
    }
}
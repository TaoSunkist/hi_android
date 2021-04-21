package me.taosunkist.hello.ui.service

import android.content.Context
import android.content.IntentFilter
import android.net.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import me.taosunkist.hello.databinding.SystemServiceFragmentBinding
import me.taosunkist.hello.ui.BaseFragment
import top.thsunkist.tatame.utilities.printf

class SystemServiceFragment : BaseFragment() {

    companion object {
        fun newInstance() = SystemServiceFragment()
    }

    private lateinit var viewModel: ViewModel

    private lateinit var connectivityManager: ConnectivityManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            SystemServiceFragmentBinding.inflate(inflater, container, false).root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(ViewModel::class.java)

        printf("taohui", tag)

        connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } else {
            connectivityManager.registerNetworkCallback(with(NetworkRequest.Builder()) {
                addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                build()
            }, networkCallback)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}
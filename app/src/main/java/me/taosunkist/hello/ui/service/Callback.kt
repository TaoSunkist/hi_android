package me.taosunkist.hello.ui.service

import android.net.*
import me.taosunkist.hello.utility.printf

val networkCallback: ConnectivityManager.NetworkCallback by lazy {

    object : ConnectivityManager.NetworkCallback() {

        val TAG = "NetworkCallback"

        init {
            printf(TAG, "initialized")
        }

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            printf(TAG, "onAvailable($network: Network)")
        }

        override fun onLosing(network: Network, maxMsToLive: Int) {
            super.onLosing(network, maxMsToLive)
            printf(TAG, "onLosing($network: Network, $maxMsToLive: Int)")
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            printf(TAG, "onLost($network: Network)")
        }

        override fun onUnavailable() {
            super.onUnavailable()
            printf(TAG, "onUnavailable()")
        }

        override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            printf(TAG, "onCapabilitiesChanged($network: Network, $networkCapabilities: NetworkCapabilities) ")
        }

        override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
            super.onLinkPropertiesChanged(network, linkProperties)
            printf(TAG, "onLinkPropertiesChanged($network: Network, $linkProperties: LinkProperties)")
        }

        override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
            super.onBlockedStatusChanged(network, blocked)
            printf(TAG, "onBlockedStatusChanged($network: Network, $blocked: Boolean)")
        }
    }
}


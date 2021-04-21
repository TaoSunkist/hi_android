package me.taosunkist.hello.utility

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.content.ContextCompat
import me.taosunkist.hello.ui.service.networkCallback

var vibrator: Vibrator? = null

fun vibrator(context: Context, rate: Long = 500L) {
    if (vibrator == null) {
        vibrator = (context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator)
    }
    vibrator?.let {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(rate, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(rate)
            }
        }
    }
}

private var connectivityManager: ConnectivityManager? = null

fun initConnectivityManager(context: Context): ConnectivityManager {
    if (connectivityManager == null) {
        connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
    return connectivityManager!!
}

fun registerNetworkCallback(): ConnectivityManager.NetworkCallback {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        connectivityManager?.registerDefaultNetworkCallback(networkCallback)
    } else {
        connectivityManager?.registerNetworkCallback(with(NetworkRequest.Builder()) {
            addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            build()
        }, networkCallback)
    }
    return networkCallback
}
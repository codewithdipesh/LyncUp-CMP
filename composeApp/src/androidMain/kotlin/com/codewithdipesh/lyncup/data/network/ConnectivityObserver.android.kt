package com.codewithdipesh.lyncup.data.network

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.codewithdipesh.lyncup.AppContextHolder
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

actual class ConnectivityObserver{
    val context = AppContextHolder.context
    val connectivityManager = context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager

    private var networkCallback: ConnectivityManager.NetworkCallback? = null

    private val _isConnected = MutableStateFlow(false)
    actual val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    actual fun startObserving() {
        //initial status
        updateInitialWiFiState()

        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)

                //check wifi with internet
                val capabilities = connectivityManager.getNetworkCapabilities(network)
                _isConnected.value = isWiFiConnectedWithInternet(capabilities)
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                //check wifi with internet
                _isConnected.value = isWiFiConnectedWithInternet(networkCapabilities)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                _isConnected.value = false
            }
        }
        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback!!)
    }

    actual fun stopObserving() {
        networkCallback?.let {
            connectivityManager.unregisterNetworkCallback(it)
        }
        networkCallback = null
    }

    private fun updateInitialWiFiState() {
        val activeNetwork = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)

        _isConnected.value = isWiFiConnectedWithInternet(capabilities)
    }

    private fun isWiFiConnectedWithInternet(capabilities: NetworkCapabilities?): Boolean {
        return capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
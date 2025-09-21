package com.codewithdipesh.lyncup.data.network

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.NetworkInterface

actual class ConnectivityObserver actual constructor() {

    private val _isConnected = MutableStateFlow(true)
    actual val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    private var monitoringJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.IO)

    actual fun startObserving() {
        //initial
        _isConnected.value = checkNetworkAvailability()

        monitoringJob?.cancel()
        monitoringJob = scope.launch {
            while (isActive) {
                val wasConnected = _isConnected.value
                val isCurrentlyConnected = checkNetworkAvailability()

                if (wasConnected != isCurrentlyConnected) {
                    _isConnected.value = isCurrentlyConnected
                }

                delay(30000) // Check every 15 seconds
            }
        }
    }

    actual fun stopObserving() {
        monitoringJob?.cancel()
        monitoringJob = null
    }

    private fun checkNetworkAvailability(): Boolean {
        return try {
            val interfaces = NetworkInterface.getNetworkInterfaces()
            interfaces.asSequence().any { networkInterface ->
                networkInterface.isUp &&
                        !networkInterface.isLoopback &&
                        networkInterface.inetAddresses.asSequence().any { address ->
                            !address.isLoopbackAddress &&
                                    !address.isLinkLocalAddress &&
                                    (address is Inet4Address || address is Inet6Address)
                        }
            }
        } catch (e: Exception) {
            println("Error checking network interfaces: ${e.message}")
            false
        }
    }
}
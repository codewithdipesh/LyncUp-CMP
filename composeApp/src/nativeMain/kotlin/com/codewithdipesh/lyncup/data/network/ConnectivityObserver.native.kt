package com.codewithdipesh.lyncup.data.network

import kotlinx.coroutines.flow.StateFlow

actual class ConnectivityObserver actual constructor(context: Any? = null) {
    actual fun startObserving() {
    }

    actual fun stopObserving() {
    }

    actual val isConnected: StateFlow<Boolean>
        get() = TODO("Not yet implemented")
}
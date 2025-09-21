package com.codewithdipesh.lyncup.data.network

import kotlinx.coroutines.flow.StateFlow

expect class ConnectivityObserver(){
    fun startObserving()
    fun stopObserving()
    val isConnected: StateFlow<Boolean>
}
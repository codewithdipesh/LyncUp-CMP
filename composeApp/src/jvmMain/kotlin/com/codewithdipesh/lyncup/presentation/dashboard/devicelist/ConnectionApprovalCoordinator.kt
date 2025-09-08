package com.codewithdipesh.lyncup.presentation.dashboard.devicelist

import com.codewithdipesh.lyncup.domain.model.HandShake
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class ConnectionApprovalCoordinator {
    private val _requests = MutableStateFlow<HandShake?>(null)
    val requests = _requests.asStateFlow()

    @Volatile
    private var pending: CompletableDeferred<Boolean>? = null

    suspend fun onIncomingRequest(request: HandShake): Boolean {
        val deferred = CompletableDeferred<Boolean>()
        pending = deferred
        println("Svc: approval coordinator onRequest from ${request}")
        _requests.value = request
        return deferred.await()
    }

    fun approve() {
        pending?.complete(true)
        pending = null
        _requests.value = null
    }

    fun reject() {
        pending?.complete(false)
        pending = null
        _requests.value = null
    }
}
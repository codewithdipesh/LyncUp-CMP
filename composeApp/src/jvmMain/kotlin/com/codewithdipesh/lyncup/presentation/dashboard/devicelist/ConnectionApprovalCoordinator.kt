package com.codewithdipesh.lyncup.presentation.dashboard.devicelist

import com.codewithdipesh.lyncup.domain.model.HandShake
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class ConnectionApprovalCoordinator {
    private val _requests = MutableSharedFlow<HandShake>(extraBufferCapacity = 1)
    val requests = _requests.asSharedFlow()

    @Volatile
    private var pending: CompletableDeferred<Boolean>? = null

    suspend fun onIncomingRequest(request: HandShake): Boolean {
        val deferred = CompletableDeferred<Boolean>()
        pending = deferred
        _requests.tryEmit(request)
        return deferred.await()
    }

    fun approve() {
        pending?.complete(true)
        pending = null
    }

    fun reject() {
        pending?.complete(false)
        pending = null
    }
}
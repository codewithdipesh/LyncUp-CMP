package com.codewithdipesh.lyncup.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithdipesh.lyncup.data.dataStore.SharedPreference
import com.codewithdipesh.lyncup.data.network.SocketManager
import com.codewithdipesh.lyncup.domain.repository.DeviceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SessionCheckViewModel(
    private val repo : DeviceRepository
): ViewModel() {

    private val _isSessionActive = MutableStateFlow(false)
    val isSessionActive = _isSessionActive.asStateFlow()

    init {
        println("SessionCheckViewModel initialized")
        viewModelScope.launch {
            checkPrevSession()
        }
    }

    suspend fun checkPrevSession() {
        val hasActiveSession = repo.checkPrevSession()
        println("Session check result: $hasActiveSession")
        _isSessionActive.value = hasActiveSession

    }

}
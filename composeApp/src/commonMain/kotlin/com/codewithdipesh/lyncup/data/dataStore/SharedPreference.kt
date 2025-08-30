package com.codewithdipesh.lyncup.data.dataStore

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

object SharedPreference {
    private const val KEY = "device_id"

    @OptIn(ExperimentalUuidApi::class)
    fun getOrCreateDeviceId() : String {
        val settings = SettingsProvider.settings()
        val existing = settings.getStringOrNull(KEY)
        if(existing != null) return existing
        val newId = Uuid.random().toString()
        settings.putString(KEY,newId)
        return newId
    }
}
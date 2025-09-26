package com.codewithdipesh.lyncup.data.dataStore

import com.codewithdipesh.lyncup.domain.model.Device
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

object SharedPreference {
    private const val ID_KEY = "device_id"
    private const val IS_CONNECTED_KEY = "isConnected"
    private const val CONNECTED_DEVICE_KEY = "device_id"

    @OptIn(ExperimentalUuidApi::class)
    fun getOrCreateDeviceId() : String {
        val settings = SettingsProvider.settings()
        val existing = settings.getStringOrNull(ID_KEY)
        if(existing != null) return existing
        val newId = Uuid.random().toString()
        settings.putString(ID_KEY,newId)
        return newId
    }

    fun isConnected() : Boolean {
        val settings = SettingsProvider.settings()
        val existing = settings.getBooleanOrNull(IS_CONNECTED_KEY)
        if(existing != null) return existing
        return false
    }

    fun getConnectedDevice() : Device? {
        val settings = SettingsProvider.settings()
        val codedDevice =  settings.getStringOrNull(CONNECTED_DEVICE_KEY)
        val decoded = Json.decodeFromString<Device>(codedDevice ?: return null)
        return decoded
    }
    fun setConnectedDevice(device: Device? = null) {
        val settings = SettingsProvider.settings()
        if(device == null) {
            settings.remove(CONNECTED_DEVICE_KEY)
            settings.putBoolean(IS_CONNECTED_KEY,false)
        }else{
            val codedDevice = Json.encodeToString(device)
            settings.putString(CONNECTED_DEVICE_KEY,codedDevice)
            settings.putBoolean(IS_CONNECTED_KEY,true)
        }
    }
}
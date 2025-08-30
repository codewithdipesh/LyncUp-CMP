package com.codewithdipesh.lyncup.data.dataStore

import com.russhwolf.settings.Settings

expect object SettingsProvider {
    fun settings(): Settings
}
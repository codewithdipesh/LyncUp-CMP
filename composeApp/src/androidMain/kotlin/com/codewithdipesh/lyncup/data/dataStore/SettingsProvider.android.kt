package com.codewithdipesh.lyncup.data.dataStore

import android.content.Context
import com.codewithdipesh.lyncup.AppContextHolder
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

actual object SettingsProvider {

    actual fun settings(): Settings {
        val prefs = AppContextHolder.context.getSharedPreferences(
            "lync_up_prefs",
            Context.MODE_PRIVATE
        )
        return SharedPreferencesSettings(prefs)
    }
}

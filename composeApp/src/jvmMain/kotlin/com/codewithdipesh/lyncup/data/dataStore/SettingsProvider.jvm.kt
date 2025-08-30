package com.codewithdipesh.lyncup.data.dataStore

import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.Settings
import java.util.prefs.Preferences

actual object SettingsProvider {
    actual fun settings(): Settings {
       return PreferencesSettings(Preferences.userRoot().node("lync_up_prefs"))
    }
}
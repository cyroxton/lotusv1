package com.dn0ne.player.core.data

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

class LanguageManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    var language: String
        get() = prefs.getString("language", "en") ?: "en"
        set(value) {
            prefs.edit().putString("language", value).apply()
            val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(value)
            AppCompatDelegate.setApplicationLocales(appLocale)
        }

    val availableLanguages = mapOf(
        "en" to "English",
        "fr" to "Français",
        "ru" to "Русский",
        "uk" to "Українська"
    )
}

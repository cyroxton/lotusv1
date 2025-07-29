package com.dn0ne.player.core.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

class LanguageManager(private val context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    var language: String
        get() = prefs.getString("language", "en") ?: "en"
        set(value) {
            Log.d("LocaleDebug", "Changement de langue demandé: $value")
            Log.d("LocaleDebug", "Contexte actuel: ${prefs.javaClass.simpleName}")
            prefs.edit().putString("language", value).apply()
            val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(value)
            Log.d("LocaleDebug", "AppLocale créé: $appLocale")
            try {
            AppCompatDelegate.setApplicationLocales(appLocale)
                Log.d("LocaleDebug", "setApplicationLocales appelé avec succès")
                // Recréer l'activité pour appliquer correctement le changement de langue
                (context as? android.app.Activity)?.recreate()
            } catch (e: Exception) {
                Log.e("LocaleDebug", "Erreur lors du changement de langue", e)
                throw e
            }
        }

    val availableLanguages = mapOf(
        "en" to "English",
        "fr" to "Français",
        "ru" to "Русский",
        "uk" to "Українська"
    )
}

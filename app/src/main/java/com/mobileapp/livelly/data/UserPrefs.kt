package com.mobileapp.livelly.data

import android.content.Context

object UserPrefs {

    private const val PREF_NAME = "livelly_prefs"
    private const val KEY_NAME = "user_name"

    fun saveName(context: Context, name: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_NAME, name).apply()
    }

    fun getName(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_NAME, null)
    }
}
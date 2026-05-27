package com.mobileapp.livelly.data

import android.content.Context

object SettingsPrefs {

    private const val PREFS_NAME = "settings_prefs"

    private const val DARK_THEME =
        "dark_theme"

    private const val DAILY_REMINDER =
        "daily_reminder"

    private const val COMPACT_WIDGET =
        "compact_widget"

    private const val TRANSPARENT_WIDGET =
        "transparent_widget"

    private const val REMINDER_HOUR = "reminder_hour"
    private const val REMINDER_MINUTE = "reminder_minute"

    // DARK THEME
    fun saveDarkTheme(
        context: Context,
        enabled: Boolean
    ) {
        prefs(context)
            .edit()
            .putBoolean(DARK_THEME, enabled)
            .apply()
    }

    fun isDarkTheme(
        context: Context
    ): Boolean {
        return prefs(context)
            .getBoolean(DARK_THEME, true)
    }

    // DAILY REMINDER
    fun saveDailyReminder(
        context: Context,
        enabled: Boolean
    ) {
        prefs(context)
            .edit()
            .putBoolean(DAILY_REMINDER, enabled)
            .apply()
    }

    fun isDailyReminderEnabled(
        context: Context
    ): Boolean {
        return prefs(context)
            .getBoolean(DAILY_REMINDER, true)
    }

    // COMPACT WIDGET
    fun saveCompactWidget(
        context: Context,
        enabled: Boolean
    ) {
        prefs(context)
            .edit()
            .putBoolean(COMPACT_WIDGET, enabled)
            .apply()
    }

    fun isCompactWidgetEnabled(
        context: Context
    ): Boolean {
        return prefs(context)
            .getBoolean(COMPACT_WIDGET, false)
    }

    // TRANSPARENT WIDGET
    fun saveTransparentWidget(
        context: Context,
        enabled: Boolean
    ) {
        prefs(context)
            .edit()
            .putBoolean(TRANSPARENT_WIDGET, enabled)
            .apply()
    }

    fun isTransparentWidgetEnabled(
        context: Context
    ): Boolean {
        return prefs(context)
            .getBoolean(TRANSPARENT_WIDGET, true)
    }

    fun saveReminderTime(context: Context, hour: Int, minute: Int) {
        prefs(context).edit()
            .putInt(REMINDER_HOUR, hour)
            .putInt(REMINDER_MINUTE, minute)
            .apply()
    }

    fun getReminderHour(context: Context): Int {
        return prefs(context).getInt(REMINDER_HOUR, 9)
    }

    fun getReminderMinute(context: Context): Int {
        return prefs(context).getInt(REMINDER_MINUTE, 0)
    }

    private fun prefs(context: Context) =
        context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
}
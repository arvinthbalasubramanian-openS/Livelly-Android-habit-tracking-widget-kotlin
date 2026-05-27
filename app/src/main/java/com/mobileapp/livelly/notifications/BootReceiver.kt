package com.mobileapp.livelly.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mobileapp.livelly.data.SettingsPrefs

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            if (SettingsPrefs.isDailyReminderEnabled(context)) {
                val hour = SettingsPrefs.getReminderHour(context)
                val minute = SettingsPrefs.getReminderMinute(context)
                NotificationScheduler.scheduleDailyReminder(context, hour, minute)
            }
        }
    }
}

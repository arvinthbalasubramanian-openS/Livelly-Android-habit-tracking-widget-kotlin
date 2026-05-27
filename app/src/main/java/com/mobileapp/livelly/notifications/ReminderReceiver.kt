package com.mobileapp.livelly.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.mobileapp.livelly.MainActivity
import com.mobileapp.livelly.R
import com.mobileapp.livelly.data.Habit
import com.mobileapp.livelly.data.HabitPrefs
import com.mobileapp.livelly.logic.isCompletedToday

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        HabitPrefs.loadHabits(context)
        val habits = HabitPrefs.habitsFlow.value
        showNotification(context, habits)
    }

    private fun showNotification(context: Context, habits: List<Habit>) {
        val channelId = "daily_reminders"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val incompleteHabits = habits.filter { !isCompletedToday(it.lastCompleted) }
        
        val title: String
        val content: String

        when {
            habits.isEmpty() -> {
                title = "Start your journey!"
                content = "Add some habits to track your progress."
            }
            incompleteHabits.isEmpty() -> {
                title = "All caught up!"
                content = "You've completed all your habits for today. Great job!"
            }
            incompleteHabits.size == 1 -> {
                title = "Habit Reminder"
                content = "Don't forget to: ${incompleteHabits.first().name}"
            }
            else -> {
                title = "Daily Habit Progress"
                val completedCount = habits.size - incompleteHabits.size
                val nextHabits = incompleteHabits.take(2).joinToString { it.name }
                content = "$completedCount/${habits.size} completed. Next up: $nextHabits..."
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Daily Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Reminders to check your habits"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }
}

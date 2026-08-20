package com.example

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.frequent.habits.R
import com.example.data.AppDatabase
import com.example.data.isHabitActiveOnDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HabitReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val habitId = intent.getIntExtra("habitId", -1)
        val habitName = intent.getStringExtra("habitName") ?: ""
        val reminderHour = intent.getIntExtra("reminderHour", -1)
        val reminderMinute = intent.getIntExtra("reminderMinute", -1)

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val db = AppDatabase.getDatabase(context)
                val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

                if (habitId != -1) {
                    // Individual Reminder Flow
                    val habit = db.habitDao().getHabitByIdSuspend(habitId)
                    if (habit != null && !habit.isArchived) {
                        if (isHabitActiveOnDate(habit, todayStr)) {
                            val logs = db.habitDao().getLogsForHabitOnDate(habitId, todayStr)
                            val log = logs.firstOrNull()
                            val isCompleted = if (log != null) {
                                when (log.value) {
                                    -1f -> false // Explicitly failed
                                    -2f -> true  // Explicitly succeeded
                                    else -> {
                                        if (habit.type == "BINARY") {
                                            if (habit.isNegative) false else true
                                        } else {
                                            if (habit.isNegative) log.value < habit.targetValue else log.value >= habit.targetValue
                                        }
                                    }
                                }
                            } else {
                                habit.isNegative // negative default is completed (success)
                            }

                            if (!isCompleted) {
                                showIndividualNotification(context, habitId, habit.name)
                            }
                        }
                        // Reschedule alarm for next occurrence
                        NotificationHelper.scheduleAllHabitReminders(context, habit)
                    }
                } else {
                    // Fallback to General Reminder Flow
                    val allHabits = db.habitDao().getAllHabitsRaw()
                    val todayLogs = db.habitDao().getLogsForDateRaw(todayStr)
                    val logsMap = todayLogs.associateBy { it.habitId }

                    val pendingHabits = allHabits.filter { habit ->
                        val log = logsMap[habit.id]
                        val isCompleted = if (log != null) {
                            when (log.value) {
                                -1f -> false
                                -2f -> true
                                else -> {
                                    if (habit.type == "BINARY") {
                                        if (habit.isNegative) false else true
                                    } else {
                                        if (habit.isNegative) log.value < habit.targetValue else log.value >= habit.targetValue
                                    }
                                }
                            }
                        } else {
                            habit.isNegative
                        }
                        !isCompleted && !habit.isArchived && isHabitActiveOnDate(habit, todayStr)
                    }

                    if (pendingHabits.isNotEmpty()) {
                        showNotification(context, pendingHabits.size)
                    }

                    // Reschedule general reminder for tomorrow
                    val prefs = context.getSharedPreferences("habits_settings", Context.MODE_PRIVATE)
                    val isGeneralEnabled = prefs.getBoolean("reminder_enabled", false)
                    if (isGeneralEnabled) {
                        val h = if (reminderHour != -1) reminderHour else prefs.getInt("reminder_hour", 20)
                        val m = if (reminderMinute != -1) reminderMinute else prefs.getInt("reminder_minute", 0)
                        NotificationHelper.scheduleReminder(context, h, m)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                pendingResult.finish()
            }
        }
    }

    private fun showIndividualNotification(context: Context, habitId: Int, habitName: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "habit_reminders"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Gewohnheiten Erinnerung",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Erinnert an noch nicht erledigte Gewohnheiten"
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val openAppIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            habitId,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val title = "Zeit für deine Gewohnheit! 🚀"
        val text = "Hast du '$habitName' heute schon erledigt? Bleib dran!"

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(text)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .build()

        notificationManager.notify(habitId, notification)
    }

    private fun showNotification(context: Context, pendingCount: Int) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "habit_reminders"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Gewohnheiten Erinnerung",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Erinnert an noch nicht erledigte Gewohnheiten"
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val openAppIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            99,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val title = "Vergiss deine Habits nicht! 🚀"
        val text = "Du hast heute noch $pendingCount Gewohnheit(en) offen. Bleib dran!"

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(text)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .build()

        notificationManager.notify(101, notification)
    }
}

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
import java.util.Calendar

class ReviewNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val reviewType = intent.getStringExtra("OPEN_REVIEW_TYPE") ?: "MONTHLY"
        val prefs = context.getSharedPreferences("habits_settings", Context.MODE_PRIVATE)

        val cal = Calendar.getInstance()
        val currentYear = cal.get(Calendar.YEAR)
        val currentMonth = cal.get(Calendar.MONTH) + 1 // 1-12

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "review_notifications"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Rückblick Benachrichtigungen",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Erinnert an Monats- und Jahresrückblicke"
            }
            notificationManager.createNotificationChannel(channel)
        }

        if (reviewType == "YEARLY") {
            val yearlyEnabled = prefs.getBoolean("yearly_review_enabled", true)
            if (!yearlyEnabled) return

            val targetYear = currentYear - 1
            val openAppIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("OPEN_REVIEW_TYPE", "YEARLY")
                putExtra("REVIEW_YEAR", targetYear)
            }

            val pendingIntent = PendingIntent.getActivity(
                context,
                9901,
                openAppIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val language = prefs.getString("language", "en") ?: "en"
            val title = if (language == "de") "Dein Jahres-Review ist bereit!" else if (language == "ka") "თქვენი წლიური მიმოხილვა მზად არის!" else if (language == "zh") "你的年度回顾已准备就绪！" else "Your Year in Review is Ready!"
            val text = if (language == "de") "Schau nach, was dein vergangenes Ich dir in der Zeitkapsel hinterlassen hat." else if (language == "ka") "შეამოწმეთ რა დაგიტოვათ თქვენმა წარსულმა მე-მ დროის კაფსულაში." else if (language == "zh") "快来看看过去的你在时间胶囊中留下了什么。" else "Check out what your past self left you in the time capsule."

            val notification = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()

            notificationManager.notify(9901, notification)
        } else {
            val monthlyEnabled = prefs.getBoolean("monthly_review_enabled", true)
            if (!monthlyEnabled) return

            var targetYear = currentYear
            var targetMonth = currentMonth - 1
            if (targetMonth < 1) {
                targetMonth = 12
                targetYear -= 1
            }

            val openAppIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("OPEN_REVIEW_TYPE", "MONTHLY")
                putExtra("REVIEW_YEAR", targetYear)
                putExtra("REVIEW_MONTH", targetMonth)
            }

            val pendingIntent = PendingIntent.getActivity(
                context,
                9902,
                openAppIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val language = prefs.getString("language", "en") ?: "en"
            val title = if (language == "de") "Dein Monats-Review ist bereit!" else if (language == "ka") "თქვენი ყოველთვიური მიმოხილვა მზად არის!" else if (language == "zh") "你的月度回顾已准备就绪！" else "Your Monthly Review is Ready!"
            val text = if (language == "de") "Schau nach, was dein vergangenes Ich dir in der Zeitkapsel hinterlassen hat." else if (language == "ka") "შეამოწმეთ რა დაგიტოვათ თქვენმა წარსულმა მე-მ დროის კაფსულაში." else if (language == "zh") "快来看看过去的你在时间胶囊中留下了什么。" else "Check out what your past self left you in the time capsule."

            val notification = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()

            notificationManager.notify(9902, notification)
        }

        // Reschedule next review alarms
        NotificationHelper.scheduleReviewNotifications(context)
    }
}

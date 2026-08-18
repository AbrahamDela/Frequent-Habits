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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale

class SmartInsightNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val prefs = context.getSharedPreferences("habits_settings", Context.MODE_PRIVATE)
        val enabled = prefs.getBoolean("insight_notifications_enabled", true)
        if (!enabled) return

        val lastSent = prefs.getLong("insight_notification_last_sent", 0L)
        val now = System.currentTimeMillis()
        // Wait at least 6 days between insights (roughly once a week)
        if (now - lastSent < 6L * 24 * 60 * 60 * 1000) {
            NotificationHelper.scheduleSmartInsightNotifications(context)
            return
        }

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                evaluateAndShowInsight(context)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                pendingResult.finish()
                NotificationHelper.scheduleSmartInsightNotifications(context)
            }
        }
    }

    private suspend fun evaluateAndShowInsight(context: Context) {
        val db = AppDatabase.getDatabase(context).habitDao()
        val allHabits = db.getAllHabitsRaw()
        if (allHabits.isEmpty()) return
        val allLogs = db.getAllLogsRaw()
        
        val prefs = context.getSharedPreferences("habits_settings", Context.MODE_PRIVATE)
        val language = prefs.getString("app_language", "en") ?: "en"
        
        val sdfDate = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val todayDateString = sdfDate.format(java.util.Date())
        
        // Basic Stats calculation
        val habitsWithStrength = allHabits.map { habit ->
            val habitLogs = allLogs.filter { it.habitId == habit.id }
            var strength = 0
            if (habitLogs.isNotEmpty()) {
                val completed = habitLogs.count { it.value > 0f && !it.isPaused }
                strength = ((completed.toFloat() / habitLogs.size) * 100).toInt()
            }
            Pair(habit, strength)
        }
        
        var totalGlobalCompletions = 0
        allLogs.forEach { if (it.value > 0f && !it.isPaused) totalGlobalCompletions++ }

        val options = mutableListOf<Pair<String, String>>()
        
        // 1. Total Completions
        if (totalGlobalCompletions > 0 && totalGlobalCompletions % 50 == 0) {
            val text = if (language == "de") {
                "Wahnsinn! Du hast gerade insgesamt **$totalGlobalCompletions** Abschlüsse erreicht. Feiere diesen Meilenstein! 🎉"
            } else if (language == "ka") {
                "საოცარია! თქვენ მიაღწიეთ სულ **$totalGlobalCompletions** დასრულებას! 🎉"
            } else if (language == "zh") {
                "太棒了！你刚刚达成了累计 **$totalGlobalCompletions** 次完成。庆祝这个里程碑！🎉"
            } else {
                "Amazing! You just reached a total of **$totalGlobalCompletions** completions. Celebrate this milestone! 🎉"
            }
            options.add("TOTAL_COMPLETIONS" to text)
        }
        
        // 2. High Momentum
        val highMomentum = habitsWithStrength.maxByOrNull { it.second }
        if (highMomentum != null && highMomentum.second >= 80) {
            val text = if (language == "de") {
                "Dein Habit **'${highMomentum.first.name}'** hat eine fantastische Stärke von **${highMomentum.second}%** erreicht! 💎"
            } else if (language == "ka") {
                "შენმა ჩვევამ **'${highMomentum.first.name}'** მიაღწია სიძლიერეს **${highMomentum.second}%**! 💎"
            } else if (language == "zh") {
                "你的习惯 **“${highMomentum.first.name}”** 达到了惊人的 **${highMomentum.second}%** 稳固度！💎"
            } else {
                "Your habit **'${highMomentum.first.name}'** has reached a fantastic strength of **${highMomentum.second}%**! 💎"
            }
            options.add("HIGH_MOMENTUM" to text)
        }
        
        // 3. Top Performer
        val habitCompletionsMap = mutableMapOf<Int, Int>()
        allLogs.forEach { log ->
            if (log.value > 0f && !log.isPaused) {
                habitCompletionsMap[log.habitId] = (habitCompletionsMap[log.habitId] ?: 0) + 1
            }
        }
        val maxHabitEntry = habitCompletionsMap.maxByOrNull { it.value }
        if (maxHabitEntry != null && maxHabitEntry.value >= 10) {
            val matchingHabit = allHabits.find { it.id == maxHabitEntry.key }
            if (matchingHabit != null) {
                val text = if (language == "de") {
                    "Dein absoluter Spitzenreiter ist **'${matchingHabit.name}'** mit bereits **${maxHabitEntry.value}** Abschlüssen! Tolle Leistung. 🟢"
                } else if (language == "ka") {
                    "შენი საუკეთესო ჩვევაა **'${matchingHabit.name}'** უკვე **${maxHabitEntry.value}** დასრულებით! 🟢"
                } else if (language == "zh") {
                    "你的冠军习惯是 **“${matchingHabit.name}”**，已完成 **${maxHabitEntry.value}** 次！表现优异。🟢"
                } else {
                    "Your absolute top performer is **'${matchingHabit.name}'** with already **${maxHabitEntry.value}** completions! Great job. 🟢"
                }
                options.add("TOP_PERFORMER" to text)
            }
        }

        // 4. Most Active Day
        val weekdayCompletionsMap = IntArray(8)
        allLogs.forEach { log ->
            if (log.value > 0f && !log.isPaused) {
                try {
                    val dateVal = sdfDate.parse(log.date)
                    if (dateVal != null) {
                        val calDay = Calendar.getInstance()
                        calDay.time = dateVal
                        val dayOfWeek = calDay.get(Calendar.DAY_OF_WEEK)
                        if (dayOfWeek in 1..7) {
                            weekdayCompletionsMap[dayOfWeek]++
                        }
                    }
                } catch (e: Exception) {}
            }
        }
        var maxDayIndex = -1
        var maxCount = 0
        for (i in 1..7) {
            if (weekdayCompletionsMap[i] > maxCount) {
                maxCount = weekdayCompletionsMap[i]
                maxDayIndex = i
            }
        }
        if (maxDayIndex != -1 && maxCount > 10) {
            val dayName = when (maxDayIndex) {
                Calendar.MONDAY -> if (language == "de") "Montag" else if (language == "ka") "ორშაბათი" else if (language == "zh") "周一" else "Monday"
                Calendar.TUESDAY -> if (language == "de") "Dienstag" else if (language == "ka") "სამშაბათი" else if (language == "zh") "周二" else "Tuesday"
                Calendar.WEDNESDAY -> if (language == "de") "Mittwoch" else if (language == "ka") "ოთხშაბათი" else if (language == "zh") "周三" else "Wednesday"
                Calendar.THURSDAY -> if (language == "de") "Donnerstag" else if (language == "ka") "ხუთშაბათი" else if (language == "zh") "周四" else "Thursday"
                Calendar.FRIDAY -> if (language == "de") "Freitag" else if (language == "ka") "პარასკევი" else if (language == "zh") "周五" else "Friday"
                Calendar.SATURDAY -> if (language == "de") "Samstag" else if (language == "ka") "შაბათი" else if (language == "zh") "周六" else "Saturday"
                Calendar.SUNDAY -> if (language == "de") "Sonntag" else if (language == "ka") "კვირა" else if (language == "zh") "周日" else "Sunday"
                else -> ""
            }
            val text = if (language == "de") {
                "Dein aktivster Wochentag ist der **$dayName** mit insgesamt **$maxCount** erfolgreichen Abschlüssen! 📈"
            } else if (language == "ka") {
                "შენი ყველაზე აქტიური დღეა **$dayName** სულ **$maxCount** შესრულებით! 📈"
            } else if (language == "zh") {
                "你在一周中最活跃的一天是 **$dayName**，共计成功完成了 **$maxCount** 次！📈"
            } else {
                "Your most active day of the week is **$dayName** with a total of **$maxCount** successful completions! 📈"
            }
            options.add("MOST_ACTIVE_DAY" to text)
        }

        // 5. Week-over-Week comparison
        val nowTime = System.currentTimeMillis()
        val oneWeekMillis = 7 * 24 * 60 * 60 * 1000L
        val twoWeeksMillis = 14 * 24 * 60 * 60 * 1000L
        val thisWeekCount = allLogs.count { log -> log.value > 0f && !log.isPaused && (nowTime - log.timestamp) <= oneWeekMillis }
        val prevWeekCount = allLogs.count { log -> log.value > 0f && !log.isPaused && (nowTime - log.timestamp) in (oneWeekMillis + 1)..twoWeeksMillis }
        if (thisWeekCount > 0 || prevWeekCount > 0) {
            val text = if (language == "de") {
                "In den letzten 7 Tagen hast du **$thisWeekCount** Abschlüsse geschafft (Vorwoche: **$prevWeekCount**). Bleib weiter am Ball! ⚡"
            } else if (language == "ka") {
                "ბოლო 7 დღეში გაქვს **$thisWeekCount** შესრულება (წინა კვირას: **$prevWeekCount**). ⚡"
            } else if (language == "zh") {
                "最近 7 天内你达成了 **$thisWeekCount** 次完成（上周：**$prevWeekCount** 次）。继续保持势头！⚡"
            } else {
                "In the last 7 days you achieved **$thisWeekCount** completions (prev week: **$prevWeekCount**). Keep staying on track! ⚡"
            }
            options.add("WEEK_OVER_WEEK" to text)
        }

        // 6. Lowest performing habit (Constructive)
        val activeHabits = habitsWithStrength.filter { !it.first.isArchived }
        if (activeHabits.size >= 2) {
            val lowest = activeHabits.minByOrNull { it.second }
            if (lowest != null && lowest.second < 50) {
                val text = if (language == "de") {
                    "Dein Habit **'${lowest.first.name}'** könnte etwas zusätzliche Aufmerksamkeit gebrauchen (Stärke: **${lowest.second}%**). Heute durchstarten? 🎯"
                } else if (language == "ka") {
                    "შენს ჩვევას **'${lowest.first.name}'** ცოტა მეტი ყურადღება სჭირდება (სიძლიერე: **${lowest.second}%**). 🎯"
                } else if (language == "zh") {
                    "你的习惯 **“${lowest.first.name}”** 可能需要更多关注（稳固度：**${lowest.second}%**）。今天就行动起来？🎯"
                } else {
                    "Your habit **'${lowest.first.name}'** could use a little extra attention (strength: **${lowest.second}%**). Ready today? 🎯"
                }
                options.add("LOWEST_PERFORMER" to text)
            }
        }

        if (options.isEmpty()) return
        
        // Pick one randomly or deterministically
        val selected = options[Math.abs(todayDateString.hashCode()) % options.size]
        val cleanText = selected.second.replace("**", "")

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "insight_notifications"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Smart Insights",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Wöchentliche Smart Insights Benachrichtigungen"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val openAppIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            9903,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val title = if (language == "de") "Neuer Smart Insight 💡" else if (language == "ka") "ახალი ჭკვიანი ანალიტიკა 💡" else if (language == "zh") "新的智能洞察 💡" else "New Smart Insight 💡"
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(cleanText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(cleanText))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
            
        notificationManager.notify(9903, notification)
        
        // Record that we sent an insight
        prefs.edit().putLong("insight_notification_last_sent", System.currentTimeMillis()).apply()
    }
}

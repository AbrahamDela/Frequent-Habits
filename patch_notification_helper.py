with open("app/src/main/java/com/example/NotificationHelper.kt", "r") as f:
    content = f.read()

funcs = """    fun cancelReviewNotifications(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val monthlyIntent = Intent(context, ReviewNotificationReceiver::class.java)
        val monthlyPendingIntent = PendingIntent.getBroadcast(
            context,
            88001,
            monthlyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(monthlyPendingIntent)
        
        val yearlyIntent = Intent(context, ReviewNotificationReceiver::class.java)
        val yearlyPendingIntent = PendingIntent.getBroadcast(
            context,
            88002,
            yearlyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(yearlyPendingIntent)
    }

    fun scheduleSmartInsightNotifications(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val prefs = context.getSharedPreferences("habits_settings", Context.MODE_PRIVATE)
        val intent = Intent(context, SmartInsightNotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            88003,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
        
        if (prefs.getBoolean("insight_notifications_enabled", true)) {
            val calendar = java.util.Calendar.getInstance().apply {
                // Schedule for 10:00 AM next day
                set(java.util.Calendar.HOUR_OF_DAY, 10)
                set(java.util.Calendar.MINUTE, 0)
                set(java.util.Calendar.SECOND, 0)
                set(java.util.Calendar.MILLISECOND, 0)
                if (timeInMillis <= System.currentTimeMillis()) {
                    add(java.util.Calendar.DAY_OF_YEAR, 1)
                }
            }
            try {
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun cancelSmartInsightNotifications(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, SmartInsightNotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            88003,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }"""

# Find the end of the class (the last '}')
content = content.replace("    fun cancelReviewNotifications(context: Context) {", funcs.replace("    fun cancelReviewNotifications(context: Context) {", "    fun cancelReviewNotifications(context: Context) {\n        // DO NOT REPLACE IT ALL, JUST ADD BEFORE THE ORIGINALS"))

with open("app/src/main/java/com/example/NotificationHelper.kt", "w") as f:
    f.write(content)

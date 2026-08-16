package com.example

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.BroadcastReceiver
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.widget.RemoteViews
import androidx.compose.ui.graphics.toArgb
import com.example.data.AppDatabase
import com.frequent.habits.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.text.SimpleDateFormat
import java.util.*

class HabitWidgetProvider : AppWidgetProvider() {

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        scheduleNextMidnightAlarm(context)
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        scheduleNextMidnightAlarm(context)
        val pendingResult = goAsync()
        updateAllWidgets(context, appWidgetManager, appWidgetIds, pendingResult)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        val action = intent.action
        val widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)

        if (action == ACTION_UPDATE_HABITS ||
            action == Intent.ACTION_DATE_CHANGED ||
            action == Intent.ACTION_TIME_CHANGED ||
            action == Intent.ACTION_TIMEZONE_CHANGED ||
            action == Intent.ACTION_BOOT_COMPLETED ||
            action == "android.intent.action.MY_PACKAGE_REPLACED" ||
            action == "android.intent.action.TIME_SET") {
            
            scheduleNextMidnightAlarm(context)
            if (action == Intent.ACTION_BOOT_COMPLETED || action == "android.intent.action.MY_PACKAGE_REPLACED") {
                com.example.NotificationHelper.scheduleSmartInsightNotifications(context)
                com.example.NotificationHelper.scheduleReviewNotifications(context)
                kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
                    try {
                        val db = com.example.data.AppDatabase.getDatabase(context).habitDao()
                        val habits = db.getAllHabitsRaw()
                        habits.forEach { habit ->
                            com.example.NotificationHelper.scheduleAllHabitReminders(context, habit)
                        }
                    } catch (e: Exception) { e.printStackTrace() }
                }
            }
            val pendingResult = goAsync()
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val componentName = ComponentName(context, HabitWidgetProvider::class.java)
            val ids = appWidgetManager.getAppWidgetIds(componentName)
            updateAllWidgets(context, appWidgetManager, ids, pendingResult, isFullUpdate = true)
        } else if (action == ACTION_TOGGLE_BINARY_HABIT) {
            val habitId = intent.getIntExtra(EXTRA_HABIT_ID, -1)
            if (habitId != -1) {
                val pendingResult = goAsync()
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        performToggleHabit(context, habitId)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        pendingResult.finish()
                    }
                }
            }
        } else if (action == ACTION_WIDGET_ADD_VALUE_DIRECT) {
            val habitId = intent.getIntExtra(EXTRA_HABIT_ID, -1)
            val delta = intent.getFloatExtra(EXTRA_DELTA, 0f)
            if (habitId != -1 && delta != 0f) {
                val pendingResult = goAsync()
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        performDeltaHabit(context, habitId, delta)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        pendingResult.finish()
                    }
                }
            }
        } else if (action == ACTION_WIDGET_ITEM_CLICK) {
            val itemAction = intent.getStringExtra("WIDGET_ACTION")
            val habitId = intent.getIntExtra(EXTRA_HABIT_ID, -1)
            val delta = intent.getFloatExtra(EXTRA_DELTA, 0f)

            if (itemAction == "TOGGLE" || itemAction == "DELTA") {
                val now = System.currentTimeMillis()
                if (habitId == lastClickedHabitId && (now - lastClickTime) < 600L) {
                    return
                }
                lastClickedHabitId = habitId
                lastClickTime = now
            }

            if (itemAction == "TOGGLE" && habitId != -1) {
                val pendingResult = goAsync()
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        performToggleHabit(context, habitId)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        pendingResult.finish()
                    }
                }
            } else if (itemAction == "DELTA" && habitId != -1 && delta != 0f) {
                val pendingResult = goAsync()
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        performDeltaHabit(context, habitId, delta)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        pendingResult.finish()
                    }
                }
            } else if (itemAction == "OPEN_APP" && habitId != -1) {
                val db = AppDatabase.getDatabase(context)
                val pendingResult = goAsync()
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val habit = db.habitDao().getHabitByIdSuspend(habitId)
                        if (habit != null) {
                            val openAppIntent = Intent(context, MainActivity::class.java).apply {
                                val unitLower = habit.unit.lowercase().trim()
                                val isMins = unitLower in listOf("minuten", "minutes", "min", "minute", "m")
                                val isNumerical = habit.type == "NUMBER" || habit.type == "NUMERICAL"
                                if (habit.type != "BINARY" && (isNumerical || isMins)) {
                                    setAction(ACTION_WIDGET_ADD_VALUE)
                                    putExtra(EXTRA_HABIT_ID, habitId)
                                }
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                            }
                            context.startActivity(openAppIntent)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        pendingResult.finish()
                    }
                }
            }
        }
    }

    private suspend fun performToggleHabit(context: Context, habitId: Int) {
        updateMutex.withLock {
            val db = AppDatabase.getDatabase(context)
            val habit = db.habitDao().getHabitByIdSuspend(habitId) ?: return@withLock
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val todayStr = sdf.format(Date())
            db.habitDao().toggleHabitTransaction(
                habitId = habitId,
                selectedDate = todayStr,
                isNegative = habit.isNegative,
                type = habit.type,
                targetValue = habit.targetValue
            )
        }

        val appWidgetManager = AppWidgetManager.getInstance(context)
        val componentName = ComponentName(context, HabitWidgetProvider::class.java)
        val ids = appWidgetManager.getAppWidgetIds(componentName)
        updateAllWidgetsSuspend(context, appWidgetManager, ids, isFullUpdate = false)
    }

    private suspend fun performDeltaHabit(context: Context, habitId: Int, delta: Float) {
        updateMutex.withLock {
            val db = AppDatabase.getDatabase(context)
            val habit = db.habitDao().getHabitByIdSuspend(habitId) ?: return@withLock
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val todayStr = sdf.format(Date())
            db.habitDao().deltaHabitTransaction(
                habitId = habitId,
                selectedDate = todayStr,
                delta = delta,
                targetValue = habit.targetValue
            )
        }

        val appWidgetManager = AppWidgetManager.getInstance(context)
        val componentName = ComponentName(context, HabitWidgetProvider::class.java)
        val ids = appWidgetManager.getAppWidgetIds(componentName)
        updateAllWidgetsSuspend(context, appWidgetManager, ids, isFullUpdate = false)
    }

    private fun updateAllWidgets(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
        pendingResult: BroadcastReceiver.PendingResult? = null,
        isFullUpdate: Boolean = true
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                updateAllWidgetsSuspend(context, appWidgetManager, appWidgetIds, isFullUpdate)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                pendingResult?.finish()
            }
        }
    }

    private suspend fun updateAllWidgetsSuspend(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
        isFullUpdate: Boolean = true
    ) {
        updateMutex.withLock {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val todayStr = sdf.format(Date())
            val db = AppDatabase.getDatabase(context)
            val allHabits = db.habitDao().getAllHabitsRaw()

            appWidgetIds.forEach { widgetId ->
                val selectedDate = todayStr
                val widgetLogs = db.habitDao().getLogsForDateRaw(selectedDate)
                val logsMap = widgetLogs.associateBy { it.habitId }
                val allLogs = db.habitDao().getAllLogsRaw()

                val activeHabits = allHabits
                    .filter { !it.isArchived && com.example.data.isHabitActiveOnDate(it, todayStr) }
                    .sortedWith(compareBy<com.example.data.Habit> { it.sortOrder }.thenByDescending { it.id })

                var completed = 0
                var nonPausedCount = 0

                val curDate = try { java.time.LocalDate.parse(todayStr) } catch (e: Exception) { java.time.LocalDate.now() }
                val startOf7Days = curDate.minusDays(6).toString()
                val endOf7Days = curDate.toString()

                activeHabits.forEach { habit ->
                    val log = logsMap[habit.id]
                    val isPaused = log != null && log.isPaused
                    if (!isPaused) {
                        nonPausedCount++
                        val isDone = if (habit.frequency == "TIMES_WEEKLY") {
                            val weeklyTarget = habit.specificDays.toIntOrNull() ?: 3
                            val weeklyCount = allLogs.filter { l ->
                                l.habitId == habit.id && l.date >= startOf7Days && l.date <= endOf7Days && com.example.data.isLogCompleted(habit, l)
                            }.size
                            weeklyCount >= weeklyTarget || com.example.data.isLogCompleted(habit, log)
                        } else {
                            com.example.data.isLogCompleted(habit, log)
                        }
                        if (isDone) {
                            completed++
                        }
                    }
                }

                val sharedPrefs = context.getSharedPreferences("habits_settings", Context.MODE_PRIVATE)
                val isDark = sharedPrefs.getBoolean("dark_mode_enabled", true)
                val accentColorName = sharedPrefs.getString("accent_color_name", null)
                    ?: context.getSharedPreferences("habit_prefs", Context.MODE_PRIVATE).getString("accent_color_name", "PURPLE")
                    ?: "PURPLE"
                val accentColorInt = com.example.ui.HabitIconMapping.getColor(accentColorName).toArgb()

                val progressPercent = if (nonPausedCount > 0) (completed.toFloat() / nonPausedCount * 100).toInt() else 0
                val isCompleted = progressPercent >= 100 && nonPausedCount > 0

                val views = RemoteViews(context.packageName, R.layout.habit_widget)
                views.setInt(R.id.widget_root_layout, "setBackgroundResource", if (isDark) R.drawable.widget_bg else R.drawable.widget_bg_light)
                views.setInt(R.id.widget_streak_container, "setBackgroundResource", if (isDark) R.drawable.widget_streak_bg else R.drawable.widget_streak_bg_light)

                val progressBitmap = drawProgressBarBitmap(progressPercent, isCompleted, accentColorInt, isDark)
                views.setImageViewBitmap(R.id.widget_progress_bar, progressBitmap)

                val perfectStats = com.example.data.StreakCalculator.calculate(allHabits, allLogs, targetDateStr = todayStr)
                val currentStreak = perfectStats.currentStreak

                sharedPrefs.edit().putInt("current_perfect_streak", currentStreak).apply()
                views.setTextViewText(R.id.widget_streak_text, currentStreak.toString())
                views.setTextColor(R.id.widget_streak_text, if (isDark) Color.parseColor("#FF9800") else Color.parseColor("#EA580C"))

                val openAppInt = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                val pendingInt = PendingIntent.getActivity(
                    context,
                    widgetId * 5000,
                    openAppInt,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.widget_progress_bar, pendingInt)
                views.setOnClickPendingIntent(R.id.widget_streak_container, pendingInt)

                val serviceIntent = Intent(context, HabitWidgetService::class.java).apply {
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                    data = android.net.Uri.parse("custom://widget/habits_list/$widgetId")
                }
                views.setRemoteAdapter(R.id.widget_habits_list, serviceIntent)

                val clickIntent = Intent(context, HabitWidgetProvider::class.java).apply {
                    action = ACTION_WIDGET_ITEM_CLICK
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                }
                val clickPIntent = PendingIntent.getBroadcast(
                    context,
                    widgetId * 1000 + 5,
                    clickIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                )
                views.setPendingIntentTemplate(R.id.widget_habits_list, clickPIntent)

                appWidgetManager.updateAppWidget(widgetId, views)
                appWidgetManager.notifyAppWidgetViewDataChanged(widgetId, R.id.widget_habits_list)
            }
        }
    }

    private fun scheduleNextMidnightAlarm(context: Context) {
        try {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? android.app.AlarmManager ?: return
            val intent = Intent(context, HabitWidgetProvider::class.java).apply {
                action = ACTION_UPDATE_HABITS
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                998877,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            val calendar = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 2)
                set(Calendar.MILLISECOND, 0)
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                alarmManager.setAndAllowWhileIdle(android.app.AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            } else {
                alarmManager.setExact(android.app.AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getDisplayDate(dateStr: String): String {
        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        return if (dateStr == todayStr) {
            "Heute"
        } else {
            try {
                val date = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(dateStr)
                if (date != null) {
                    SimpleDateFormat("dd.MM", Locale.GERMAN).format(date)
                } else {
                    dateStr
                }
            } catch (e: Exception) {
                dateStr
            }
        }
    }

    private fun drawProgressBarBitmap(progressPercent: Int, isCompleted: Boolean, accentColorInt: Int, isDark: Boolean): Bitmap {
        val width = 600
        val height = 24
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val cornerRadius = 12f
        val rect = android.graphics.RectF(0f, 0f, width.toFloat(), height.toFloat())

        // 1. Background Track
        val trackPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = if (isDark) Color.parseColor("#242432") else Color.parseColor("#E5E5ED")
        }
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, trackPaint)

        // 2. Progress Fill
        if (progressPercent > 0) {
            val fillColor = if (isCompleted) Color.parseColor("#10B981") else accentColorInt
            val fillPaint = Paint().apply {
                isAntiAlias = true
                style = Paint.Style.FILL
                color = fillColor
            }
            val fillWidth = width.toFloat() * (progressPercent.coerceIn(0, 100) / 100f)

            val clipPath = Path().apply {
                addRoundRect(rect, cornerRadius, cornerRadius, Path.Direction.CW)
            }
            canvas.save()
            canvas.clipPath(clipPath)
            canvas.drawRect(0f, 0f, fillWidth, height.toFloat(), fillPaint)
            canvas.restore()
        }

        return bitmap
    }

    private fun getColorInt(colorName: String): Int {
        return when (colorName.lowercase()) {
            "blue" -> 0xFF3B82F6.toInt()
            "purple" -> 0xFF9333EA.toInt()
            "cyan" -> 0xFF06B6D4.toInt()
            "green" -> 0xFF10B981.toInt()
            "yellow" -> 0xFFF59E0B.toInt()
            "orange" -> 0xFFF97316.toInt()
            "red" -> 0xFFEF4444.toInt()
            "pink" -> 0xFFEC4899.toInt()
            "slate", "grey", "gray" -> 0xFF64748B.toInt()
            else -> 0xFF7356FF.toInt() // Default to PrimaryViolet
        }
    }

    fun drawIconToBitmap(context: Context, iconName: String, colorInt: Int): Bitmap {
        val size = 48
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint().apply {
            color = colorInt
            isAntiAlias = true
            style = Paint.Style.FILL
            strokeWidth = 3f
        }
        
        val half = size / 2f
        
        when (iconName.lowercase()) {
            "sparkle" -> {
                val path = Path().apply {
                    moveTo(half, 4f)
                    quadTo(half, half, size - 4f, half)
                    quadTo(half, half, half, size - 4f)
                    quadTo(half, half, 4f, half)
                    quadTo(half, half, half, 4f)
                    close()
                }
                canvas.drawPath(path, paint)
            }
            "moon" -> {
                val path = Path().apply {
                    addCircle(half + 4f, half, half - 6f, Path.Direction.CW)
                    val subtraction = Path().apply {
                        addCircle(half, half, half - 6f, Path.Direction.CW)
                    }
                    op(subtraction, Path.Op.DIFFERENCE)
                }
                canvas.drawPath(path, paint)
            }
            "sun" -> {
                canvas.drawCircle(half, half, size / 5f, paint)
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 4f
                for (i in 0 until 8) {
                    val angle = i * Math.PI / 4
                    val startX = half + (size / 3.5f) * Math.cos(angle).toFloat()
                    val startY = half + (size / 3.5f) * Math.sin(angle).toFloat()
                    val endX = half + (size / 2.2f) * Math.cos(angle).toFloat()
                    val endY = half + (size / 2.2f) * Math.sin(angle).toFloat()
                    canvas.drawLine(startX, startY, endX, endY, paint)
                }
            }
            "water" -> {
                val path = Path().apply {
                    moveTo(half, 6f)
                    cubicTo(size - 8f, half + 4f, size - 8f, size - 6f, half, size - 6f)
                    cubicTo(8f, size - 6f, 8f, half + 4f, half, 6f)
                    close()
                }
                canvas.drawPath(path, paint)
            }
            "heart" -> {
                val heartPath = Path().apply {
                    moveTo(half, size * 0.3f)
                    cubicTo(size * 0.2f, size * 0.05f, size * 0.02f, size * 0.25f, size * 0.05f, size * 0.5f)
                    cubicTo(size * 0.08f, size * 0.75f, size * 0.35f, size * 0.9f, half, size * 0.95f)
                    cubicTo(size * 0.65f, size * 0.9f, size * 0.92f, size * 0.75f, size * 0.95f, size * 0.5f)
                    cubicTo(size * 0.98f, size * 0.25f, size * 0.8f, size * 0.05f, half, size * 0.3f)
                    close()
                }
                canvas.drawPath(heartPath, paint)
            }
            "dumbbell" -> {
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 6f
                canvas.drawLine(8f, half, size - 8f, half, paint)
                paint.style = Paint.Style.FILL
                canvas.drawCircle(10f, half, 8f, paint)
                canvas.drawCircle(size - 10f, half, 8f, paint)
            }
            "book" -> {
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 4f
                val path = Path().apply {
                    moveTo(half, size - 10f)
                    lineTo(half, 10f)
                }
                canvas.drawPath(path, paint)
                val leftPage = Path().apply {
                    moveTo(half, 12f)
                    cubicTo(half - 8f, 8f, 8f, 8f, 8f, 12f)
                    lineTo(8f, size - 12f)
                    cubicTo(8f, size - 16f, half - 8f, size - 16f, half, size - 12f)
                }
                canvas.drawPath(leftPage, paint)
                val rightPage = Path().apply {
                    moveTo(half, 12f)
                    cubicTo(half + 8f, 8f, size - 8f, 8f, size - 8f, 12f)
                    lineTo(size - 8f, size - 12f)
                    cubicTo(size - 8f, size - 16f, half + 8f, size - 16f, half, size - 12f)
                }
                canvas.drawPath(rightPage, paint)
            }
            "coffee" -> {
                val cup = Path().apply {
                    moveTo(10f, 16f)
                    lineTo(size - 14f, 16f)
                    cubicTo(size - 14f, size - 10f, 14f, size - 10f, 10f, 16f)
                    close()
                }
                canvas.drawPath(cup, paint)
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 3f
                val handle = Path().apply {
                    moveTo(size - 14f, 20f)
                    cubicTo(size - 6f, 20f, size - 6f, size - 16f, size - 14f, size - 16f)
                }
                canvas.drawPath(handle, paint)
                val steam1 = Path().apply {
                    moveTo(16f, 12f)
                    quadTo(18f, 9f, 16f, 6f)
                }
                val steam2 = Path().apply {
                    moveTo(half - 2f, 12f)
                    quadTo(half, 9f, half - 2f, 6f)
                }
                canvas.drawPath(steam1, paint)
                canvas.drawPath(steam2, paint)
            }
            "run" -> {
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 4f
                paint.style = Paint.Style.FILL
                canvas.drawCircle(half + 4f, 10f, 4f, paint)
                paint.style = Paint.Style.STROKE
                val torso = Path().apply {
                    moveTo(half + 2f, 14f)
                    lineTo(half - 2f, size * 0.6f)
                    moveTo(half - 2f, size * 0.6f)
                    lineTo(half - 8f, size * 0.75f)
                    lineTo(half - 4f, size * 0.9f)
                    moveTo(half - 2f, size * 0.6f)
                    lineTo(half + 6f, size * 0.75f)
                    lineTo(half + 2f, size * 0.9f)
                    moveTo(half + 2f, 16f)
                    lineTo(half + 8f, 20f)
                    lineTo(half + 12f, 16f)
                    moveTo(half + 2f, 16f)
                    lineTo(half - 6f, 20f)
                    lineTo(half - 10f, 24f)
                }
                canvas.drawPath(torso, paint)
            }
            "code" -> {
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 4f
                val left = Path().apply {
                    moveTo(12f, 10f)
                    lineTo(6f, half)
                    lineTo(12f, size - 10f)
                }
                val right = Path().apply {
                    moveTo(size - 12f, 10f)
                    lineTo(size - 6f, half)
                    lineTo(size - 12f, size - 10f)
                }
                val slash = Path().apply {
                    moveTo(size - 10f, 6f)
                    lineTo(10f, size - 6f)
                }
                canvas.drawPath(left, paint)
                canvas.drawPath(right, paint)
                canvas.drawPath(slash, paint)
            }
            "music" -> {
                canvas.drawCircle(12f, size - 12f, 6f, paint)
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 4f
                val stem = Path().apply {
                    moveTo(16f, size - 12f)
                    lineTo(16f, 8f)
                    lineTo(size - 10f, 12f)
                }
                canvas.drawPath(stem, paint)
            }
            "phone" -> {
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 5f
                val phone = Path().apply {
                    moveTo(10f, 10f)
                    cubicTo(6f, 14f, 14f, size - 6f, size - 10f, size - 10f)
                }
                canvas.drawPath(phone, paint)
                paint.style = Paint.Style.FILL
                canvas.drawCircle(10f, 10f, 4f, paint)
                canvas.drawCircle(size - 10f, size - 10f, 4f, paint)
            }
            "meditation" -> {
                paint.style = Paint.Style.FILL
                canvas.drawCircle(half, 10f, 4f, paint)
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 4f
                val body = Path().apply {
                    moveTo(half, 18f)
                    lineTo(half, size * 0.65f)
                    moveTo(half, size * 0.65f)
                    cubicTo(8f, size * 0.65f, 10f, size * 0.9f, half, size * 0.85f)
                    moveTo(half, size * 0.65f)
                    cubicTo(size - 8f, size * 0.65f, size - 10f, size * 0.9f, half, size * 0.85f)
                    moveTo(half, 23f)
                    cubicTo(8f, 23f, 8f, size * 0.6f, 12f, size * 0.65f)
                    moveTo(half, 23f)
                    cubicTo(size - 8f, 23f, size - 8f, size * 0.6f, size - 12f, size * 0.65f)
                }
                canvas.drawPath(body, paint)
            }
            "clock" -> {
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 4f
                canvas.drawCircle(half, half, size / 2f - 6f, paint)
                canvas.drawLine(half, half, half, 12f, paint)
                canvas.drawLine(half, half, half + 8f, half, paint)
            }
            "food" -> {
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 3f
                canvas.drawCircle(half, half, 12f, paint)
                canvas.drawLine(10f, 16f, 10f, size - 16f, paint)
                canvas.drawLine(size - 10f, 16f, size - 10f, size - 16f, paint)
            }
            "money" -> {
                paint.style = Paint.Style.FILL
                paint.textSize = 34f
                paint.textAlign = Paint.Align.CENTER
                paint.isFakeBoldText = true
                canvas.drawText("$", half, half + 12f, paint)
            }
            "work" -> {
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 3f
                val rect = Path().apply {
                    addRoundRect(10f, 16f, size - 10f, size - 10f, 4f, 4f, Path.Direction.CW)
                }
                canvas.drawPath(rect, paint)
                val handle = Path().apply {
                    moveTo(half - 6f, 16f)
                    lineTo(half - 6f, 10f)
                    lineTo(half + 6f, 10f)
                    lineTo(half + 6f, 16f)
                }
                canvas.drawPath(handle, paint)
            }
            "clean" -> {
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 4f
                canvas.drawLine(10f, size - 10f, size - 16f, 16f, paint)
                canvas.drawLine(10f, size - 10f, 6f, size - 6f, paint)
                canvas.drawLine(10f, size - 10f, 14f, size - 6f, paint)
                canvas.drawLine(10f, size - 10f, 6f, size - 14f, paint)
            }
            else -> {
                val path = Path().apply {
                    moveTo(half, 4f)
                    quadTo(half, half, size - 4f, half)
                    quadTo(half, half, half, size - 4f)
                    quadTo(half, half, 4f, half)
                    quadTo(half, half, half, 4f)
                    close()
                }
                canvas.drawPath(path, paint)
            }
        }
        
        return bitmap
    }

    companion object {
        const val ACTION_UPDATE_HABITS = "com.example.widget.ACTION_UPDATE_HABITS"
        const val ACTION_TOGGLE_BINARY_HABIT = "com.example.widget.ACTION_TOGGLE_BINARY_HABIT"
        const val ACTION_WIDGET_ADD_VALUE = "com.example.widget.ACTION_WIDGET_ADD_VALUE"
        const val ACTION_WIDGET_ADD_VALUE_DIRECT = "com.example.widget.ACTION_WIDGET_ADD_VALUE_DIRECT"
        const val ACTION_WIDGET_ITEM_CLICK = "com.example.widget.ACTION_WIDGET_ITEM_CLICK"
        const val EXTRA_HABIT_ID = "com.example.widget.EXTRA_HABIT_ID"
        const val EXTRA_DELTA = "com.example.widget.EXTRA_DELTA"

        private val updateMutex = Mutex()

        @Volatile
        private var lastClickTime = 0L
        @Volatile
        private var lastUpdateAllTime = 0L

        @Volatile
        private var lastClickedHabitId = -1
        @Volatile
        private var lastUpdateTriggerTime = 0L

        fun triggerUpdate(context: Context) {
            val intent = Intent(context, HabitWidgetProvider::class.java).apply {
                action = ACTION_UPDATE_HABITS
            }
            context.sendBroadcast(intent)
        }
    }
}

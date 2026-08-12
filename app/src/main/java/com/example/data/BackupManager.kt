package com.example.data

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*

object BackupManager {

    suspend fun exportDatabaseToJson(context: Context): String = withContext(Dispatchers.IO) {
        val db = AppDatabase.getDatabase(context)
        val habits = db.habitDao().getAllHabitsRaw()
        val logs = db.habitDao().getAllLogsRaw()
        val notes = db.habitDao().getAllDailyNotesRaw()
        val timeCapsuleNotes = db.habitDao().getAllTimeCapsuleNotesRaw()
        val milestoneRewards = db.habitDao().getAllMilestoneRewardsRaw()

        val rootJson = JSONObject()
        rootJson.put("version", 3)

        val habitsArray = JSONArray()
        for (habit in habits) {
            val hJson = JSONObject().apply {
                put("id", habit.id)
                put("name", habit.name)
                put("category", habit.category)
                put("icon", habit.icon)
                put("color", habit.color)
                put("isNegative", habit.isNegative)
                put("type", habit.type)
                put("unit", habit.unit)
                put("targetValue", habit.targetValue.toDouble())
                put("frequency", habit.frequency)
                put("specificDays", habit.specificDays)
                put("startDate", habit.startDate)
                put("createdAt", habit.createdAt)
                put("sortOrder", habit.sortOrder)
                put("reminderEnabled", habit.reminderEnabled)
                put("reminderHour", habit.reminderHour)
                put("reminderMinute", habit.reminderMinute)
                put("customReminders", habit.customReminders)
                put("isArchived", habit.isArchived)
                put("description", habit.description)
            }
            habitsArray.put(hJson)
        }
        rootJson.put("habits", habitsArray)

        val logsArray = JSONArray()
        for (log in logs) {
            val lJson = JSONObject().apply {
                put("id", log.id)
                put("habitId", log.habitId)
                put("date", log.date)
                put("value", log.value.toDouble())
                put("timestamp", log.timestamp)
                put("isPaused", log.isPaused)
            }
            logsArray.put(lJson)
        }
        rootJson.put("logs", logsArray)

        val notesArray = JSONArray()
        for (note in notes) {
            val nJson = JSONObject().apply {
                put("date", note.date)
                put("content", note.content)
            }
            notesArray.put(nJson)
        }
        rootJson.put("dailyNotes", notesArray)

        val timeCapsuleArray = JSONArray()
        for (tcNote in timeCapsuleNotes) {
            val tcJson = JSONObject().apply {
                put("id", tcNote.id)
                put("type", tcNote.type)
                put("targetPeriod", tcNote.targetPeriod)
                put("content", tcNote.content)
                put("createdAt", tcNote.createdAt)
            }
            timeCapsuleArray.put(tcJson)
        }
        rootJson.put("timeCapsuleNotes", timeCapsuleArray)

        val milestoneArray = JSONArray()
        for (mReward in milestoneRewards) {
            val mJson = JSONObject().apply {
                put("id", mReward.id)
                put("habitId", mReward.habitId)
                put("rewardText", mReward.rewardText)
                put("isRedeemed", mReward.isRedeemed)
                put("unlockedAt", mReward.unlockedAt)
                put("conditionType", mReward.conditionType)
                put("conditionValue", mReward.conditionValue)
                put("trophyId", mReward.trophyId)
            }
            milestoneArray.put(mJson)
        }
        rootJson.put("milestoneRewards", milestoneArray)

        // Settings and Preferences Export
        val settingsJson = JSONObject().apply {
            val prefs = context.getSharedPreferences("habits_settings", Context.MODE_PRIVATE)
            put("user_name", prefs.getString("user_name", ""))
            put("profile_image_uri", prefs.getString("profile_image_uri", ""))
            put("language", prefs.getString("language", "en"))
            put("accent_color_name", prefs.getString("accent_color_name", "PURPLE"))
            put("dark_mode_enabled", prefs.getBoolean("dark_mode_enabled", true))
            put("vibration_enabled", prefs.getBoolean("vibration_enabled", true))
            put("info_cards_enabled", prefs.getBoolean("info_cards_enabled", true))
            put("notifications_enabled", prefs.getBoolean("notifications_enabled", true))
            put("insight_notifications_enabled", prefs.getBoolean("insight_notifications_enabled", true))
            put("monthly_review_enabled", prefs.getBoolean("monthly_review_enabled", true))
            put("yearly_review_enabled", prefs.getBoolean("yearly_review_enabled", true))
            put("reminder_enabled", prefs.getBoolean("reminder_enabled", false))
            put("reminder_hour", prefs.getInt("reminder_hour", 18))
            put("reminder_minute", prefs.getInt("reminder_minute", 0))
            put("is_saskia_unlocked", prefs.getBoolean("is_saskia_unlocked", false))
            put("smart_insight_dismissed_date", prefs.getString("smart_insight_dismissed_date", ""))
            put("has_onboarded", prefs.getBoolean("has_onboarded", false))
            put("current_perfect_streak", prefs.getInt("current_perfect_streak", 0))

            val dismissedSet = prefs.getStringSet("dismissed_reviews", emptySet()) ?: emptySet()
            val dismissedArr = JSONArray()
            dismissedSet.forEach { dismissedArr.put(it) }
            put("dismissed_reviews", dismissedArr)

            val achievementSet = prefs.getStringSet("known_unlocked_achievement_ids", emptySet()) ?: emptySet()
            val achievementArr = JSONArray()
            achievementSet.forEach { achievementArr.put(it) }
            put("known_unlocked_achievement_ids", achievementArr)

            val colorPrefs = context.getSharedPreferences("habit_prefs", Context.MODE_PRIVATE)
            put("user_saved_color_slots", colorPrefs.getString("user_saved_color_slots", ""))

            val audioPrefs = context.getSharedPreferences("audio_soundscape_prefs", Context.MODE_PRIVATE)
            put("last_selected_audio_filename", audioPrefs.getString("last_selected_audio_filename", ""))
            put("recent_audio_filenames", audioPrefs.getString("recent_audio_filenames", ""))
        }
        rootJson.put("settings", settingsJson)

        rootJson.toString(2)
    }

    suspend fun restoreDatabaseFromJson(context: Context, jsonString: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val rootJson = JSONObject(jsonString)
            val habitsArray = rootJson.getJSONArray("habits")
            val logsArray = rootJson.getJSONArray("logs")
            val notesArray = rootJson.optJSONArray("dailyNotes")
            val tcNotesArray = rootJson.optJSONArray("timeCapsuleNotes")
            val milestoneArray = rootJson.optJSONArray("milestoneRewards")
            val settingsJson = rootJson.optJSONObject("settings")

            val db = AppDatabase.getDatabase(context)
            
            // Perform restore sequentially
            db.habitDao().clearAllLogs()
            db.habitDao().clearAllHabits()
            db.habitDao().clearAllDailyNotes()
            db.habitDao().clearAllTimeCapsuleNotes()
            db.habitDao().clearAllMilestoneRewards()

            for (i in 0 until habitsArray.length()) {
                val hJson = habitsArray.getJSONObject(i)
                val habit = Habit(
                    id = hJson.getInt("id"),
                    name = hJson.getString("name"),
                    category = hJson.optString("category", "Allgemein"),
                    icon = hJson.optString("icon", "sparkle"),
                    color = hJson.optString("color", "purple"),
                    isNegative = hJson.optBoolean("isNegative", false),
                    type = hJson.optString("type", "BINARY"),
                    unit = hJson.optString("unit", ""),
                    targetValue = hJson.optDouble("targetValue", 1.0).toFloat(),
                    frequency = hJson.optString("frequency", "DAILY"),
                    specificDays = hJson.optString("specificDays", ""),
                    startDate = hJson.optLong("startDate", System.currentTimeMillis()),
                    createdAt = hJson.optLong("createdAt", System.currentTimeMillis()),
                    sortOrder = hJson.optInt("sortOrder", 0),
                    reminderEnabled = hJson.optBoolean("reminderEnabled", false),
                    reminderHour = hJson.optInt("reminderHour", 18),
                    reminderMinute = hJson.optInt("reminderMinute", 0),
                    customReminders = hJson.optString("customReminders", ""),
                    isArchived = hJson.optBoolean("isArchived", false),
                    description = hJson.optString("description", "")
                )
                db.habitDao().insertHabit(habit)
            }

            for (i in 0 until logsArray.length()) {
                val lJson = logsArray.getJSONObject(i)
                val log = HabitLog(
                    id = lJson.getInt("id"),
                    habitId = lJson.getInt("habitId"),
                    date = lJson.getString("date"),
                    value = lJson.optDouble("value", 1.0).toFloat(),
                    timestamp = lJson.optLong("timestamp", System.currentTimeMillis()),
                    isPaused = lJson.optBoolean("isPaused", false)
                )
                db.habitDao().insertLog(log)
            }

            if (notesArray != null) {
                for (i in 0 until notesArray.length()) {
                    val nJson = notesArray.getJSONObject(i)
                    val note = DailyNote(
                        date = nJson.getString("date"),
                        content = nJson.getString("content")
                    )
                    db.habitDao().insertDailyNote(note)
                }
            }

            if (tcNotesArray != null) {
                for (i in 0 until tcNotesArray.length()) {
                    val tcJson = tcNotesArray.getJSONObject(i)
                    val tcNote = TimeCapsuleNote(
                        id = tcJson.optInt("id", 0),
                        type = tcJson.getString("type"),
                        targetPeriod = tcJson.getString("targetPeriod"),
                        content = tcJson.getString("content"),
                        createdAt = tcJson.optLong("createdAt", System.currentTimeMillis())
                    )
                    db.habitDao().insertTimeCapsuleNote(tcNote)
                }
            }

            if (milestoneArray != null) {
                for (i in 0 until milestoneArray.length()) {
                    val mJson = milestoneArray.getJSONObject(i)
                    val mReward = MilestoneReward(
                        id = mJson.optInt("id", 0),
                        habitId = mJson.getInt("habitId"),
                        rewardText = mJson.getString("rewardText"),
                        isRedeemed = mJson.optBoolean("isRedeemed", false),
                        unlockedAt = mJson.optLong("unlockedAt", 0L),
                        conditionType = mJson.optString("conditionType", ""),
                        conditionValue = mJson.optInt("conditionValue", 0),
                        trophyId = mJson.optString("trophyId", "")
                    )
                    db.habitDao().insertMilestoneReward(mReward)
                }
            }

            if (settingsJson != null) {
                val prefs = context.getSharedPreferences("habits_settings", Context.MODE_PRIVATE)
                val editor = prefs.edit()

                if (settingsJson.has("user_name")) editor.putString("user_name", settingsJson.optString("user_name", ""))
                if (settingsJson.has("profile_image_uri")) editor.putString("profile_image_uri", settingsJson.optString("profile_image_uri", ""))
                if (settingsJson.has("language")) editor.putString("language", settingsJson.optString("language", "en"))
                if (settingsJson.has("accent_color_name")) editor.putString("accent_color_name", settingsJson.optString("accent_color_name", "PURPLE"))
                if (settingsJson.has("dark_mode_enabled")) editor.putBoolean("dark_mode_enabled", settingsJson.optBoolean("dark_mode_enabled", true))
                if (settingsJson.has("vibration_enabled")) editor.putBoolean("vibration_enabled", settingsJson.optBoolean("vibration_enabled", true))
                if (settingsJson.has("info_cards_enabled")) editor.putBoolean("info_cards_enabled", settingsJson.optBoolean("info_cards_enabled", true))
                if (settingsJson.has("notifications_enabled")) editor.putBoolean("notifications_enabled", settingsJson.optBoolean("notifications_enabled", true))
                if (settingsJson.has("insight_notifications_enabled")) editor.putBoolean("insight_notifications_enabled", settingsJson.optBoolean("insight_notifications_enabled", true))
                if (settingsJson.has("monthly_review_enabled")) editor.putBoolean("monthly_review_enabled", settingsJson.optBoolean("monthly_review_enabled", true))
                if (settingsJson.has("yearly_review_enabled")) editor.putBoolean("yearly_review_enabled", settingsJson.optBoolean("yearly_review_enabled", true))
                if (settingsJson.has("reminder_enabled")) editor.putBoolean("reminder_enabled", settingsJson.optBoolean("reminder_enabled", false))
                if (settingsJson.has("reminder_hour")) editor.putInt("reminder_hour", settingsJson.optInt("reminder_hour", 18))
                if (settingsJson.has("reminder_minute")) editor.putInt("reminder_minute", settingsJson.optInt("reminder_minute", 0))
                if (settingsJson.has("is_saskia_unlocked")) editor.putBoolean("is_saskia_unlocked", settingsJson.optBoolean("is_saskia_unlocked", false))
                if (settingsJson.has("smart_insight_dismissed_date")) editor.putString("smart_insight_dismissed_date", settingsJson.optString("smart_insight_dismissed_date", ""))
                if (settingsJson.has("has_onboarded")) editor.putBoolean("has_onboarded", settingsJson.optBoolean("has_onboarded", false))
                if (settingsJson.has("current_perfect_streak")) editor.putInt("current_perfect_streak", settingsJson.optInt("current_perfect_streak", 0))

                val dismissedArr = settingsJson.optJSONArray("dismissed_reviews")
                if (dismissedArr != null) {
                    val set = mutableSetOf<String>()
                    for (i in 0 until dismissedArr.length()) {
                        set.add(dismissedArr.getString(i))
                    }
                    editor.putStringSet("dismissed_reviews", set)
                }

                val achievementArr = settingsJson.optJSONArray("known_unlocked_achievement_ids")
                if (achievementArr != null) {
                    val set = mutableSetOf<String>()
                    for (i in 0 until achievementArr.length()) {
                        set.add(achievementArr.getString(i))
                    }
                    editor.putStringSet("known_unlocked_achievement_ids", set)
                }

                editor.apply()

                if (settingsJson.has("user_saved_color_slots")) {
                    val colorPrefs = context.getSharedPreferences("habit_prefs", Context.MODE_PRIVATE)
                    colorPrefs.edit().putString("user_saved_color_slots", settingsJson.optString("user_saved_color_slots", "")).apply()
                }

                if (settingsJson.has("last_selected_audio_filename") || settingsJson.has("recent_audio_filenames")) {
                    val audioPrefs = context.getSharedPreferences("audio_soundscape_prefs", Context.MODE_PRIVATE)
                    audioPrefs.edit()
                        .putString("last_selected_audio_filename", settingsJson.optString("last_selected_audio_filename", ""))
                        .putString("recent_audio_filenames", settingsJson.optString("recent_audio_filenames", ""))
                        .apply()
                }
            }

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun performBackup(context: Context, treeUriStr: String): Boolean = withContext(Dispatchers.IO) {
        if (treeUriStr.isEmpty()) return@withContext false
        try {
            val treeUri = Uri.parse(treeUriStr)
            val rootFolder = DocumentFile.fromTreeUri(context, treeUri) ?: return@withContext false
            if (!rootFolder.exists() || !rootFolder.canWrite()) return@withContext false

            val sdf = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US)
            val dateStr = sdf.format(Date())
            val filename = "backup_frequent_habits_$dateStr.json"

            val jsonContent = exportDatabaseToJson(context)

            val backupFile = rootFolder.createFile("application/json", filename) ?: return@withContext false
            context.contentResolver.openOutputStream(backupFile.uri)?.use { os ->
                os.write(jsonContent.toByteArray())
            }

            // Cleanup oldest backups if count > 3
            val files = rootFolder.listFiles()
            val backupFiles = mutableListOf<DocumentFile>()
            if (files != null) {
                for (file in files) {
                    if (file.isFile) {
                        val name = file.name
                        if (name != null && name.startsWith("backup_frequent_habits_") && name.endsWith(".json")) {
                            backupFiles.add(file)
                        }
                    }
                }
            }
            backupFiles.sortBy { it.name }

            if (backupFiles.size > 3) {
                val toDeleteCount = backupFiles.size - 3
                for (i in 0 until toDeleteCount) {
                    backupFiles[i].delete()
                }
            }

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}


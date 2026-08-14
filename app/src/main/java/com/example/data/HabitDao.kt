package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Query("SELECT * FROM habits ORDER BY sortOrder ASC, id DESC")
    fun getAllHabits(): Flow<List<Habit>>

    @Query("SELECT * FROM habits")
    suspend fun getAllHabitsRaw(): List<Habit>

    @Query("SELECT * FROM habits WHERE id = :id")
    fun getHabitById(id: Int): Flow<Habit?>

    @Query("SELECT * FROM habits WHERE id = :id")
    suspend fun getHabitByIdSuspend(id: Int): Habit?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit): Long

    @Update
    suspend fun updateHabit(habit: Habit)

    @Delete
    suspend fun deleteHabit(habit: Habit)

    @Query("DELETE FROM habits WHERE id = :id")
    suspend fun deleteHabitById(id: Int)

    @Query("SELECT * FROM habit_logs")
    fun getAllLogs(): Flow<List<HabitLog>>

    @Query("SELECT * FROM habit_logs")
    suspend fun getAllLogsRaw(): List<HabitLog>

    @Query("SELECT * FROM habit_logs WHERE date = :date")
    fun getLogsForDate(date: String): Flow<List<HabitLog>>

    @Query("SELECT * FROM habit_logs WHERE date = :date")
    suspend fun getLogsForDateRaw(date: String): List<HabitLog>

    @Query("SELECT * FROM habit_logs WHERE habitId = :habitId")
    fun getLogsForHabit(habitId: Int): Flow<List<HabitLog>>

    @Query("SELECT * FROM habit_logs WHERE habitId = :habitId AND date = :date")
    suspend fun getLogsForHabitOnDate(habitId: Int, date: String): List<HabitLog>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: HabitLog): Long

    @Delete
    suspend fun deleteLog(log: HabitLog)

    @Query("DELETE FROM habit_logs WHERE habitId = :habitId AND date = :date")
    suspend fun deleteLogsForHabitOnDate(habitId: Int, date: String)

    @Query("DELETE FROM habit_logs")
    suspend fun clearAllLogs()

    @Query("DELETE FROM habits")
    suspend fun clearAllHabits()

    @Query("SELECT * FROM daily_notes WHERE date = :date LIMIT 1")
    fun getDailyNote(date: String): Flow<DailyNote?>

    @Query("SELECT * FROM daily_notes WHERE date = :date LIMIT 1")
    suspend fun getDailyNoteRaw(date: String): DailyNote?

    @Query("SELECT * FROM daily_notes")
    fun getAllDailyNotes(): Flow<List<DailyNote>>

    @Query("SELECT * FROM daily_notes")
    suspend fun getAllDailyNotesRaw(): List<DailyNote>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyNote(note: DailyNote)

    @Delete
    suspend fun deleteDailyNote(note: DailyNote)

    @Query("DELETE FROM daily_notes")
    suspend fun clearAllDailyNotes()

    @Query("SELECT * FROM time_capsule_notes WHERE type = :type AND targetPeriod = :targetPeriod LIMIT 1")
    fun getTimeCapsuleNote(type: String, targetPeriod: String): Flow<TimeCapsuleNote?>

    @Query("SELECT * FROM time_capsule_notes WHERE type = :type AND targetPeriod = :targetPeriod LIMIT 1")
    suspend fun getTimeCapsuleNoteRaw(type: String, targetPeriod: String): TimeCapsuleNote?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimeCapsuleNote(note: TimeCapsuleNote): Long

    @Delete
    suspend fun deleteTimeCapsuleNote(note: TimeCapsuleNote)

    @Query("SELECT * FROM time_capsule_notes")
    suspend fun getAllTimeCapsuleNotesRaw(): List<TimeCapsuleNote>

    @Query("DELETE FROM time_capsule_notes")
    suspend fun clearAllTimeCapsuleNotes()

    @Query("SELECT * FROM milestone_rewards")
    fun getAllMilestoneRewards(): Flow<List<MilestoneReward>>

    @Query("SELECT * FROM milestone_rewards")
    suspend fun getAllMilestoneRewardsRaw(): List<MilestoneReward>

    @Query("SELECT * FROM milestone_rewards WHERE habitId = :habitId")
    fun getMilestoneRewardsForHabit(habitId: Int): Flow<List<MilestoneReward>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMilestoneReward(reward: MilestoneReward)

    @Update
    suspend fun updateMilestoneReward(reward: MilestoneReward)

    @Delete
    suspend fun deleteMilestoneReward(reward: MilestoneReward)

    @Query("DELETE FROM milestone_rewards WHERE habitId = :habitId")
    suspend fun deleteMilestoneRewardsForHabit(habitId: Int)

    @Query("DELETE FROM milestone_rewards")
    suspend fun clearAllMilestoneRewards()

    @Transaction
    suspend fun logHabitTransaction(habitId: Int, date: String, value: Float) {
        deleteLogsForHabitOnDate(habitId, date)
        if (value != 0f) {
            val log = HabitLog(habitId = habitId, date = date, value = value, isPaused = false, timestamp = System.currentTimeMillis())
            insertLog(log)
        }
    }

    @Transaction
    suspend fun toggleHabitTransaction(habitId: Int, selectedDate: String, isNegative: Boolean, type: String, targetValue: Float) {
        val logs = getLogsForHabitOnDate(habitId, selectedDate)
        val currentLog = logs.firstOrNull()

        val currentStatus = when {
            currentLog == null -> if (isNegative) "SUCCESS" else "PENDING"
            currentLog.value == -1f -> "FAILED"
            currentLog.value == -2f -> "SUCCESS"
            else -> {
                if (type == "BINARY") {
                    if (isNegative) "FAILED" else "SUCCESS"
                } else {
                    if (isNegative) {
                        if (currentLog.value >= targetValue) "FAILED" else "PENDING"
                    } else {
                        if (currentLog.value >= targetValue) "SUCCESS" else "PENDING"
                    }
                }
            }
        }

        val nextStatus = if (isNegative) {
            if (currentStatus == "SUCCESS") "FAILED" else "SUCCESS"
        } else {
            when (currentStatus) {
                "PENDING" -> "SUCCESS"
                "SUCCESS" -> "FAILED"
                else -> "PENDING"
            }
        }

        deleteLogsForHabitOnDate(habitId, selectedDate)
        if (nextStatus != "PENDING") {
            val nextValue = if (nextStatus == "SUCCESS") {
                if (type == "BINARY") -2f else targetValue
            } else {
                -1f
            }
            val newLog = HabitLog(
                id = 0,
                habitId = habitId,
                date = selectedDate,
                value = nextValue
            )
            insertLog(newLog)
        }
    }

    @Transaction
    suspend fun deltaHabitTransaction(habitId: Int, selectedDate: String, delta: Float, targetValue: Float) {
        val logs = getLogsForHabitOnDate(habitId, selectedDate)
        val currentLog = logs.firstOrNull()
        val currentValue = when (currentLog?.value) {
            null -> 0f
            -1f -> 0f
            -2f -> targetValue
            else -> currentLog.value
        }
        val newValue = (currentValue + delta).coerceAtLeast(0f)

        deleteLogsForHabitOnDate(habitId, selectedDate)
        if (newValue > 0f) {
            val newLog = HabitLog(
                id = 0,
                habitId = habitId,
                date = selectedDate,
                value = newValue
            )
            insertLog(newLog)
        }
    }

    @Transaction
    suspend fun togglePauseHabitTransaction(habitId: Int, date: String) {
        val existing = getLogsForHabitOnDate(habitId, date)
        if (existing.isNotEmpty()) {
            val first = existing.first()
            if (first.isPaused) {
                if (first.value == 0f) {
                    deleteLogsForHabitOnDate(habitId, date)
                } else {
                    val updated = first.copy(isPaused = false, timestamp = System.currentTimeMillis())
                    insertLog(updated)
                }
            } else {
                val updated = first.copy(isPaused = true, timestamp = System.currentTimeMillis())
                insertLog(updated)
            }
        } else {
            val log = HabitLog(habitId = habitId, date = date, value = 0f, isPaused = true)
            insertLog(log)
        }
    }
}

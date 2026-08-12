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
}

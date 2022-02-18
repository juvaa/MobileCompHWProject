package com.homework.project.data.room

import androidx.room.*
import com.homework.project.data.entity.Reminder
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ReminderDao {
    @Query("""SELECT * FROM reminders 
        WHERE (creator_id = :creatorId AND creation_time = :creationTime)""")
    abstract suspend fun reminder(creatorId: Long, creationTime: Long): Reminder

    @Transaction
    @Query("""SELECT * FROM reminders WHERE creator_id = :userId""")
    abstract fun getUserReminders(userId: Long): Flow<List<Reminder>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(entity: Reminder): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun update(entity: Reminder)

    @Delete
    abstract suspend fun delete(entity: Reminder): Int
}
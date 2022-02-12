package com.homework.project.data.room

import androidx.room.*
import com.homework.project.data.entity.Reminder
import kotlinx.coroutines.flow.Flow

abstract class ReminderDao {
    @Query("""SELECT * FROM reminders WHERE (creator_id = :creatorId AND creation_time = :creationTime)""")
    abstract fun reminder(creatorId: Long, creationTime: Long): Reminder?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(entity: Reminder): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun update(entity: Reminder)

    @Delete
    abstract suspend fun delete(entity: Reminder): Int
}
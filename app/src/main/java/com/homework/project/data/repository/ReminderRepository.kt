package com.homework.project.data.repository

import com.homework.project.data.entity.Reminder
import com.homework.project.data.room.ReminderDao

class ReminderRepository(
    private val reminderDao: ReminderDao
) {
    suspend fun addReminder(reminder: Reminder) = reminderDao.insert(reminder)

    fun getReminders(userId: Long) = reminderDao.getUserReminders(userId)

    suspend fun getReminder(creatorId: Long, creationTime: Long) : Reminder {
        return reminderDao.reminder(creatorId = creatorId, creationTime = creationTime)
    }

    suspend fun updateReminder(reminder: Reminder) = reminderDao.update(reminder)

    suspend fun deleteReminder(reminder: Reminder) = reminderDao.delete(reminder)
}
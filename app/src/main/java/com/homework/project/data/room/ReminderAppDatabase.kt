package com.homework.project.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.homework.project.data.entity.Reminder
import com.homework.project.data.entity.User

@Database(
    entities = [User::class, Reminder::class],
    version = 1,
    exportSchema = false
)
abstract class ReminderAppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun reminderDao(): ReminderDao
}
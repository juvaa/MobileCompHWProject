package com.homework.project

import android.content.Context
import androidx.room.Room
import com.homework.project.data.repository.ReminderRepository
import com.homework.project.data.repository.UserRepository
import com.homework.project.data.room.ReminderAppDatabase

/**
 * A simple singleton dependency graph
 */
object Graph {
    lateinit var database: ReminderAppDatabase

    val userRepository by lazy {
        UserRepository(
            userDao = database.userDao()
        )
    }

    val reminderRepository by lazy {
        ReminderRepository(
            reminderDao = database.reminderDao()
        )
    }

    fun provide(context: Context) {
        database = Room.databaseBuilder(context, ReminderAppDatabase::class.java, "mcData.db")
            .fallbackToDestructiveMigration() // don't use this in production app
            .build()
    }
}
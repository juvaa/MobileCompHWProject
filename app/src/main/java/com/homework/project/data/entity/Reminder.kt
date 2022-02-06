package com.homework.project.data.entity

import java.util.*

data class Reminder(
    val reminderId: Long,
    val reminderTitle: String,
    val reminderMessage: String,
    val reminderDate: Date?,
)

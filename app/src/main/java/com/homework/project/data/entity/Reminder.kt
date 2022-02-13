package com.homework.project.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "reminders",
    primaryKeys = ["creation_time", "creator_id"],
    indices = [
        Index(value = ["creation_time", "creator_id"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["creator_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)

data class Reminder(
    @ColumnInfo(name = "message") val message: String,
    @ColumnInfo(name = "location_x") val location_x: Double?,
    @ColumnInfo(name = "location_y") val location_y: Double?,
    @ColumnInfo(name = "reminder_time") val reminder_time: Long?,
    @ColumnInfo(name = "creation_time") val creation_time: Long,
    @ColumnInfo(name = "creator_id") val creator_id: Long,
    @ColumnInfo(name = "reminder_seen") val reminder_seen: Boolean,
    )

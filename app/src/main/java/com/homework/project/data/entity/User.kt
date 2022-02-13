package com.homework.project.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [
        Index("id", unique = true),
        Index("user_name", unique = true)
    ]
)

data class User(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val userId: Long = 0,
    @ColumnInfo(name = "user_name") val userName: String,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "firstname") val firstname: String,
    @ColumnInfo(name = "lastname") val lastname: String,
)

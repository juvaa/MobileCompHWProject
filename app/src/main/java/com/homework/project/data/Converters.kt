package com.homework.project.data

import androidx.room.TypeConverter
import com.homework.project.util.ReminderIcons

class Converters {

    @TypeConverter
    fun toIcon(value: Int) = enumValues<ReminderIcons>()[value]

    @TypeConverter
    fun fromIcon(value: ReminderIcons) = value.ordinal
}
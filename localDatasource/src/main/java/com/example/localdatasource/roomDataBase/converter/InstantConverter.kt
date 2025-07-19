package com.example.localdatasource.roomDataBase.converter

import androidx.room.TypeConverter
import kotlinx.datetime.Instant

class InstantConverter {
    @TypeConverter
    fun dateToTimestamp(instant: Instant): Long = instant.toEpochMilliseconds()

    @TypeConverter
    fun fromTimestamp(value: Long): Instant = Instant.fromEpochMilliseconds(value)
}
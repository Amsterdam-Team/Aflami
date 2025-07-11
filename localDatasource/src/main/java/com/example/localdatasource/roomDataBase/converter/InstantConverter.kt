package com.example.localdatasource.roomDatabase.converter

import androidx.room.TypeConverter
import kotlinx.datetime.Instant

class InstantConverter {
    @TypeConverter
    fun fromInstant(instant: Instant): Long = instant.toEpochMilliseconds()

    @TypeConverter
    fun toInstant(value: Long): Instant = Instant.fromEpochMilliseconds(value)
}
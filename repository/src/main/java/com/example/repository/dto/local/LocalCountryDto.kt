package com.example.repository.dto.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries")
data class LocalCountryDto(
    @PrimaryKey val isoCode: String,
    val name: String
)
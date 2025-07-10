package com.example.repository.dto.local

import androidx.room.Entity

@Entity(tableName = "countries")
data class LocalCountryDto(
    val name: String,
    val isoCode: String
)

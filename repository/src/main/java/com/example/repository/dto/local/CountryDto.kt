package com.example.repository.dto.local

import androidx.room.Entity

@Entity(tableName = "countries")
data class CountryDto(
    val name: String,
    val isoCode: String
)

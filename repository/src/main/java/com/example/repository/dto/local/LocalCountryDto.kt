package com.example.repository.dto.local

import androidx.room.Entity
import com.example.repository.dto.local.utils.DatabaseConstants

@Entity(
    tableName = DatabaseConstants.COUNTRY_TABLE,
    primaryKeys = ["isoCode", "storedLanguage"]
)
data class LocalCountryDto(
    val isoCode: String,
    val storedLanguage: String,
    val name: String,
)
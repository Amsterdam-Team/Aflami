package com.amsterdam.repository.dto.local

import androidx.room.Entity
import com.amsterdam.repository.dto.local.utils.DatabaseConstants

@Entity(
    tableName = DatabaseConstants.COUNTRY_TABLE,
    primaryKeys = ["isoCode", "storedLanguage"]
)
data class CountryLocalDto(
    val isoCode: String,
    val storedLanguage: String,
    val name: String,
)
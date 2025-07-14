package com.example.repository.dto.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.repository.dto.local.utils.DatabaseContract

@Entity(tableName = DatabaseContract.COUNTRY_TABLE)
data class LocalCountryDto(
    @PrimaryKey val isoCode: String,
    val name: String
)
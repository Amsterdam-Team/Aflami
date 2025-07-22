package com.example.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.repository.dto.local.LocalCountryDto
import com.example.repository.dto.local.utils.DatabaseConstants

@Dao
interface CountryDao {
    @Query("SELECT * FROM ${DatabaseConstants.COUNTRY_TABLE} WHERE storedLanguage = :storedLanguage")
    suspend fun getAllCountries(storedLanguage: String): List<LocalCountryDto>

    @Upsert
    suspend fun upsertAllCountries(countries: List<LocalCountryDto>)
}
package com.amsterdam.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.amsterdam.repository.dto.local.CountryLocalDto
import com.amsterdam.repository.dto.local.utils.DatabaseConstants

@Dao
interface CountryDao {
    @Query("SELECT * FROM ${DatabaseConstants.COUNTRY_TABLE} WHERE storedLanguage = :storedLanguage")
    suspend fun getAllCountries(storedLanguage: String): List<CountryLocalDto>

    @Upsert
    suspend fun upsertAllCountries(countries: List<CountryLocalDto>)
}
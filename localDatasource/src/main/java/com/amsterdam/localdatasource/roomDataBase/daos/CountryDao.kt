package com.amsterdam.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.amsterdam.repository.dto.local.LocalCountryDto

@Dao
interface CountryDao {

    @Query("SELECT * FROM countries")
   suspend fun getAllCountries(): List<LocalCountryDto>

    @Upsert
    suspend fun upsertAllCountries(countries: List<LocalCountryDto>)
}
package com.example.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.repository.dto.local.LocalCountryDto
import com.example.repository.dto.local.utils.DatabaseContract

@Dao
interface CountryDao {

    @Query("SELECT * FROM ${DatabaseContract.COUNTRY_TABLE}")
   suspend fun getAllCountries(): List<LocalCountryDto>

    @Upsert
    suspend fun upsertAllCountries(countries: List<LocalCountryDto>)
}
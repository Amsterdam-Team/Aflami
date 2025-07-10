package com.example.localdatasource.roomDatabase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.repository.dto.local.LocalCountryDto

@Dao
interface CountryDao {

    @Query("SELECT * FROM countries")
   suspend fun getAll(): List<LocalCountryDto>

    @Upsert
    suspend fun upsertAll(countries: List<LocalCountryDto>)
}
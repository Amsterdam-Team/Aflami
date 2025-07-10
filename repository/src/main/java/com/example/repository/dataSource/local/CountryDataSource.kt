package com.example.repository.datasource.local

import androidx.room.Query
import androidx.room.Upsert
import com.example.repository.dto.local.CountryDto

interface CountryDataSource {
    @Query("SELECT * FROM countries")
    fun getAllCountries(): List<CountryDto>
    @Upsert
    fun addAllCountries(countries: List<CountryDto>)
}
package com.example.repository.datasource.local

import androidx.room.Query
import androidx.room.Upsert
import com.example.repository.dto.local.LocalCountryDto

interface CountryDataSource {
    @Query("SELECT * FROM countries")
    fun getAllCountries(): List<LocalCountryDto>
    @Upsert
    fun addAllCountries(countries: List<LocalCountryDto>)
}
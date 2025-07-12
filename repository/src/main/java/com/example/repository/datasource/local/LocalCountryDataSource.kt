package com.example.repository.datasource.local

import com.example.repository.dto.local.LocalCountryDto

interface LocalCountryDataSource {
    suspend fun getAllCountries(): List<LocalCountryDto>
    suspend fun addAllCountries(countries: List<LocalCountryDto>)
}
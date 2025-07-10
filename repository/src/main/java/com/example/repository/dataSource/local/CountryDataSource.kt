package com.example.repository.datasource.local

import com.example.repository.dto.local.CountryDto

interface CountryDataSource {
    suspend fun getAllCountries(): List<CountryDto>
    suspend fun addAllCountries(countries: List<CountryDto>)
}
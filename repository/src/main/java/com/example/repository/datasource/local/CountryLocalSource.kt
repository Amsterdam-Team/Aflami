package com.example.repository.datasource.local

import com.example.repository.dto.local.LocalCountryDto

interface CountryLocalSource {
    suspend fun addCountries(countries: List<LocalCountryDto>)
    suspend fun getCountries(): List<LocalCountryDto>
}
package com.amsterdam.repository.datasource.local

import com.amsterdam.repository.dto.local.LocalCountryDto

interface LocalCountryDataSource {
    suspend fun getAllCountries(): List<LocalCountryDto>
    suspend fun addAllCountries(countries: List<LocalCountryDto>)
}
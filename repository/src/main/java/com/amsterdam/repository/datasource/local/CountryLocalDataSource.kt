package com.amsterdam.repository.datasource.local

import com.amsterdam.repository.dto.local.CountryLocalDto

interface CountryLocalDataSource {
    suspend fun upsertCountries(countries: List<CountryLocalDto>)

    suspend fun getCountries(storedLanguage: String): List<CountryLocalDto>
}
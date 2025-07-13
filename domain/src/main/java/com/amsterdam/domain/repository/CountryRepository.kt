package com.amsterdam.domain.repository

import com.amsterdam.entity.Country

interface CountryRepository {
    suspend fun getAllCountries(): List<Country>
}
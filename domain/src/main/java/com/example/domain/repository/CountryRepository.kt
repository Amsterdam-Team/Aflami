package com.example.domain.repository

import com.example.entity.Country

interface CountryRepository {
    suspend fun getSuggestedCountries(): List<Country>
}
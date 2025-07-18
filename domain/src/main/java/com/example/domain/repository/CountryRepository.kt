package com.example.domain.repository

import com.example.entity.Country

interface CountryRepository {
    suspend fun getCountries(): List<Country>
}
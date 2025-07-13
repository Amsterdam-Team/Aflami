package com.mohamed.repositorfake.repository

import com.example.domain.repository.CountryRepository
import com.example.entity.Country

class CountryRepositoryImpl(): CountryRepository {
    override suspend fun getAllCountries(): List<Country> {
        return listOf(
            Country(
                countryName = "Egypt",
                countryIsoCode = "EG"
            ),
            Country(
                countryName = "United States",
                countryIsoCode = "US"
            )
        )
    }
}
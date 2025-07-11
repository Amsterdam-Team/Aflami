package com.example.localdatasource.roomDatabase.datasource

import com.example.localdatasource.roomDatabase.daos.CountryDao
import com.example.repository.datasource.local.CountryDataSource
import com.example.repository.dto.local.LocalCountryDto

class CountryDataSourceImpl(
    private val dao: CountryDao
) : CountryDataSource {

    override suspend fun getAllCountries(): List<LocalCountryDto> {
        return dao.getAll()
    }

    override suspend fun addAllCountries(countries: List<LocalCountryDto>) {
        dao.upsertAll(countries)
    }
}

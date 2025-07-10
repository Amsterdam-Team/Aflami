package com.example.localdatasource.roomDatabase

import com.example.localdatasource.roomDatabase.daos.CountryDao
import com.example.repository.datasource.local.CountryDataSource
import com.example.repository.dto.local.CountryDto

class CountryDataSourceImpl(
    private val dao: CountryDao
) : CountryDataSource {

    override suspend fun getAllCountries(): List<CountryDto> {
        return dao.getAll()
    }

    override suspend fun addAllCountries(countries: List<CountryDto>) {
        dao.upsertAll(countries)
    }
}

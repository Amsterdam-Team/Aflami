package com.example.localdatasource.roomDataBase.datasource

import com.example.localdatasource.roomDataBase.daos.CountryDao
import com.example.repository.datasource.local.LocalCountryDataSource
import com.example.repository.dto.local.LocalCountryDto

class LocalCountryDataSourceImpl(
    private val dao: CountryDao
) : LocalCountryDataSource {
    override suspend fun getAllCountries(): List<LocalCountryDto> {
        return dao.getAll()
    }

    override suspend fun addAllCountries(countries: List<LocalCountryDto>) {
        dao.upsertAll(countries)
    }
}

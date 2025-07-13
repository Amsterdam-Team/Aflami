package com.amsterdam.localdatasource.roomDataBase.datasource

import com.amsterdam.localdatasource.roomDataBase.daos.CountryDao
import com.amsterdam.repository.datasource.local.LocalCountryDataSource
import com.amsterdam.repository.dto.local.LocalCountryDto

class LocalCountryDataSourceImpl(
    private val dao: CountryDao
) : LocalCountryDataSource {
    override suspend fun getAllCountries(): List<LocalCountryDto> {
        try {
        return dao.getAllCountries()

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }

    override suspend fun addAllCountries(countries: List<LocalCountryDto>) {
        dao.upsertAllCountries(countries)
    }
}

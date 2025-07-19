package com.example.localdatasource.roomDataBase.datasource

import com.example.localdatasource.roomDataBase.daos.CountryDao
import com.example.repository.datasource.local.CountryLocalSource
import com.example.repository.dto.local.LocalCountryDto

class CountryLocalDataSourceImpl(
    private val dao: CountryDao
) : CountryLocalSource {
    override suspend fun getCountries(): List<LocalCountryDto> {
        try {
            return dao.getAllCountries()

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }

    override suspend fun addCountries(countries: List<LocalCountryDto>) {
        dao.upsertAllCountries(countries)
    }
}

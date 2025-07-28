package com.amsterdam.localdatasource.roomDataBase.datasource

import com.amsterdam.localdatasource.roomDataBase.daos.CountryDao
import com.amsterdam.repository.datasource.local.CountryLocalSource
import com.amsterdam.repository.dto.local.LocalCountryDto
import javax.inject.Inject

class CountryLocalDataSourceImpl @Inject constructor(
    private val dao: CountryDao
) : CountryLocalSource {
    override suspend fun getCountries(storedLanguage: String): List<LocalCountryDto> {
        return dao.getAllCountries(storedLanguage)
    }

    override suspend fun addCountries(countries: List<LocalCountryDto>) {
        dao.upsertAllCountries(countries)
    }
}

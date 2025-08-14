package com.amsterdam.localdatasource.roomDataBase.datasource

import com.amsterdam.localdatasource.roomDataBase.daos.CountryDao
import com.amsterdam.repository.datasource.local.CountryLocalDataSource
import com.amsterdam.repository.dto.local.CountryLocalDto
import javax.inject.Inject

class CountryLocalDataDataSourceImpl @Inject constructor(
    private val dao: CountryDao
) : CountryLocalDataSource {
    override suspend fun getCountries(storedLanguage: String): List<CountryLocalDto> {
        return dao.getAllCountries(storedLanguage)
    }

    override suspend fun upsertCountries(countries: List<CountryLocalDto>) {
        dao.upsertAllCountries(countries)
    }
}

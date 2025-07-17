package com.example.repository.repository

import com.example.domain.repository.CountryRepository
import com.example.entity.Country
import com.example.repository.datasource.local.CountryLocalSource
import com.example.repository.datasource.remote.CountryRemoteSource
import com.example.repository.dto.remote.RemoteCountryDto
import com.example.repository.mapper.local.toCountries
import com.example.repository.mapper.remote.toCountries
import com.example.repository.mapper.remote.toLocalCountries
import com.example.repository.utils.tryToExecute

class CountryRepositoryImpl(
    private val localDataSource: CountryLocalSource,
    private val remoteDataSource: CountryRemoteSource,
): CountryRepository {

    override suspend fun getAllCountries(): List<Country> {
        val countriesFromLocal = getCountriesFromLocal()
        if (countriesFromLocal.isNotEmpty()) return countriesFromLocal
        return tryToExecute(
            function = { remoteDataSource.getCountries() },
            onSuccess = { remoteCountries ->
                saveCountries(remoteCountries)
                remoteCountries.toCountries()
            },
            onFailure = { aflamiException -> throw aflamiException }
        )
    }

    private suspend fun getCountriesFromLocal(): List<Country> {
        return tryToExecute(
            function = { localDataSource.getCountries() },
            onSuccess = { localCountries -> localCountries.toCountries() },
            onFailure = { emptyList() }
        )
    }

    private suspend fun saveCountries(remoteCountries: List<RemoteCountryDto>) {
        localDataSource.addCountries(remoteCountries.toLocalCountries())
    }
}
package com.example.repository.repository

import com.example.domain.repository.CountryRepository
import com.example.entity.Country
import com.example.repository.datasource.local.CountryLocalSource
import com.example.repository.datasource.remote.CountryRemoteSource
import com.example.repository.dto.remote.RemoteCountryDto
import com.example.repository.mapper.local.CountryLocalMapper
import com.example.repository.mapper.remote.CountryRemoteMapper
import com.example.repository.utils.tryToExecute

class CountryRepositoryImpl(
    private val localDataSource: CountryLocalSource,
    private val remoteDataSource: CountryRemoteSource,
    private val countryRemoteMapper: CountryRemoteMapper,
    private val countryLocalMapper: CountryLocalMapper,
): CountryRepository {

    override suspend fun getCountries(): List<Country> {
        val countriesFromLocal = getCountriesFromLocal()
        if (countriesFromLocal.isNotEmpty()) return countriesFromLocal
        return tryToExecute(
            function = { remoteDataSource.getCountries() },
            onSuccess = { remoteCountries ->
                saveCountries(remoteCountries)
                countryRemoteMapper.mapToCountries(remoteCountries)
            },
            onFailure = { aflamiException -> throw aflamiException }
        )
    }

    private suspend fun getCountriesFromLocal(): List<Country> {
        return tryToExecute(
            function = { localDataSource.getCountries() },
            onSuccess = { localCountries -> countryLocalMapper.mapToCountries(localCountries) },
            onFailure = { emptyList() }
        )
    }

    private suspend fun saveCountries(remoteCountries: List<RemoteCountryDto>) {
        localDataSource.addCountries(countryRemoteMapper.mapToLocalCountries(remoteCountries))
    }
}
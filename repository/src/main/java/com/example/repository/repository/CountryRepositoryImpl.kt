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

    override suspend fun getAllCountries(): List<Country> =
        getCountriesFromLocal()
            .takeIf { countriesFromLocal -> countriesFromLocal.isNotEmpty() }
            ?: tryToExecute(
                function = { remoteDataSource.getCountries() },
                onSuccess = { remoteCountries -> onSuccessLoadCountries(remoteCountries) },
            onFailure = { aflamiException -> throw aflamiException }
        )

    private suspend fun onSuccessLoadCountries(
        remoteCountries: List<RemoteCountryDto>
    ): List<Country> = saveCountries(remoteCountries)
        .let { countryRemoteMapper.mapToCountries(remoteCountries) }

    private suspend fun getCountriesFromLocal(): List<Country> = tryToExecute(
            function = { localDataSource.getCountries() },
            onSuccess = { localCountries -> countryLocalMapper.mapToCountries(localCountries) },
            onFailure = { emptyList() }
        )

    private suspend fun saveCountries(remoteCountries: List<RemoteCountryDto>) =
        localDataSource.addCountries(countryRemoteMapper.mapToLocalCountries(remoteCountries))
}
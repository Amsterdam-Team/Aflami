package com.example.repository.repository

import com.example.domain.repository.CountryRepository
import com.example.repository.datasource.local.CountryLocalSource
import com.example.repository.datasource.remote.CountryRemoteSource
import com.example.repository.dto.remote.RemoteCountryDto
import com.example.repository.mapper.local.CountryLocalMapper
import com.example.repository.mapper.remote.CountryRemoteMapper

class CountryRepositoryImpl(
    private val localDataSource: CountryLocalSource,
    private val remoteDataSource: CountryRemoteSource,
    private val countryRemoteMapper: CountryRemoteMapper,
    private val countryLocalMapper: CountryLocalMapper,
) : CountryRepository {
    override suspend fun getAllCountries() =
        getCountriesFromLocal().takeIf { countriesFromLocal -> countriesFromLocal.isNotEmpty() }
            ?: onSuccessLoadCountries(remoteDataSource.getCountries())

    private suspend fun onSuccessLoadCountries(remoteCountries: List<RemoteCountryDto>) =
        saveCountries(remoteCountries).let { countryRemoteMapper.mapToCountries(remoteCountries) }

    private suspend fun getCountriesFromLocal() = try {
        countryLocalMapper.mapToCountries(localDataSource.getCountries())
    } catch (_: Exception) {
        emptyList()
    }

    private suspend fun saveCountries(remoteCountries: List<RemoteCountryDto>) =
        localDataSource.addCountries(countryRemoteMapper.mapToLocalCountries(remoteCountries))
}
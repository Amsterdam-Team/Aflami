package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.CountryRepository
import com.amsterdam.entity.Country
import com.amsterdam.repository.datasource.local.LocalCountryDataSource
import com.amsterdam.repository.datasource.remote.RemoteCountryDataSource
import com.amsterdam.repository.mapper.local.CountryLocalMapper
import com.amsterdam.repository.mapper.remote.RemoteCountryMapper

class CountryRepositoryImpl(
    private val localDataSource: LocalCountryDataSource,
    private val remoteDataSource: RemoteCountryDataSource,
    private val remoteCountryMapper: RemoteCountryMapper,
    private val localCountryMapper: CountryLocalMapper,
): CountryRepository {
    override suspend fun getAllCountries(): List<Country> {
        val localCountries = localDataSource.getAllCountries()
        if (localCountries.isNotEmpty()) return localCountries.map { localCountryMapper.mapFromLocal(it) }
        val remoteCountries = remoteDataSource.getAllCountries().map { remoteCountryMapper.mapToDomain(it) }
        localDataSource.addAllCountries(remoteCountries.map { localCountryMapper.mapToLocal(it) })
        return remoteCountries
    }
}
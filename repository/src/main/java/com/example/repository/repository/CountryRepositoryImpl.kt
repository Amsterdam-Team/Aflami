package com.example.repository.repository

import com.example.domain.repository.CountryRepository
import com.example.entity.Country
import com.example.repository.datasource.local.CountryLocalSource
import com.example.repository.datasource.remote.CountryRemoteSource
import com.example.repository.mapper.local.CountryLocalMapper
import com.example.repository.mapper.remote.RemoteCountryMapper

class CountryRepositoryImpl(
    private val localDataSource: CountryLocalSource,
    private val remoteDataSource: CountryRemoteSource,
    private val remoteCountryMapper: RemoteCountryMapper,
    private val localCountryMapper: CountryLocalMapper,
): CountryRepository {
    override suspend fun getAllCountries(): List<Country> {
        val localCountries = localDataSource.getCountries()
        if (localCountries.isNotEmpty()) return localCountries.map { localCountryMapper.mapFromLocal(it) }
        val remoteCountries = remoteDataSource.getCountries().map { remoteCountryMapper.mapToDomain(it) }
        localDataSource.addCountries(remoteCountries.map { localCountryMapper.mapToLocal(it) })
        return remoteCountries
    }
}
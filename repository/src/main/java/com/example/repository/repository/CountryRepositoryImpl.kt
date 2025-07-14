package com.example.repository.repository

import com.example.domain.repository.CountryRepository
import com.example.entity.Country
import com.example.repository.datasource.local.LocalCountryDataSource
import com.example.repository.datasource.remote.RemoteCountryDataSource
import com.example.repository.mapper.local.CountryLocalMapper
import com.example.repository.mapper.remote.RemoteCountryMapper

class CountryRepositoryImpl(
    private val localDataSource: LocalCountryDataSource,
    private val remoteDataSource: RemoteCountryDataSource,
    private val remoteCountryMapper: RemoteCountryMapper,
    private val localCountryMapper: CountryLocalMapper,
) : CountryRepository {
    override suspend fun getAllCountries(): List<Country> {
        val localCountries = localDataSource.getAllCountries()
        if (localCountries.isNotEmpty()) return localCountries.map {
            localCountryMapper.mapFromLocal(
                it
            )
        }
        val remoteCountries =
            remoteDataSource.getAllCountries().map { remoteCountryMapper.mapToDomain(it) }
        localDataSource.addAllCountries(remoteCountries.map { localCountryMapper.mapToLocal(it) })
        return remoteCountries
    }
}
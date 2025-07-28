package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.CountryRepository
import com.amsterdam.entity.Country
import com.amsterdam.repository.datasource.local.CountryLocalSource
import com.amsterdam.repository.datasource.remote.CountryRemoteSource
import com.amsterdam.repository.dto.remote.RemoteCountryDto
import com.amsterdam.repository.mapper.local.CountryLocalMapper
import com.amsterdam.repository.mapper.remote.CountryRemoteMapper
import com.amsterdam.repository.mapper.remoteToLocal.CountryRemoteLocalMapper
import com.amsterdam.repository.utils.getDeviceLanguage
import javax.inject.Inject

class CountryRepositoryImpl @Inject constructor(
    private val localDataSource: CountryLocalSource,
    private val remoteDataSource: CountryRemoteSource,
    private val countryRemoteMapper: CountryRemoteMapper,
    private val countryLocalMapper: CountryLocalMapper,
    private val countryRemoteLocalMapper: CountryRemoteLocalMapper
) : CountryRepository {
    override suspend fun getCountries(): List<Country> {
        return getCountriesFromLocal()
            .takeIf { countriesFromLocal -> countriesFromLocal.isNotEmpty() }
            ?: onSuccessLoadCountries(remoteDataSource.getCountries())
    }

    private suspend fun onSuccessLoadCountries(
        remoteCountries: List<RemoteCountryDto>
    ): List<Country> {
        return saveCountries(remoteCountries).let {
            countryRemoteMapper.toEntityList(remoteCountries)
        }
    }

    private suspend fun getCountriesFromLocal(): List<Country> {
        return try {
            countryLocalMapper.toEntityList(localDataSource.getCountries(getDeviceLanguage()))
        } catch (_: Exception) {
            emptyList()
        }
    }

    private suspend fun saveCountries(remoteCountries: List<RemoteCountryDto>) {
        localDataSource.addCountries(countryRemoteLocalMapper.toLocalList(
            remoteCountries,
            listOf(getDeviceLanguage()))
        )
    }
}
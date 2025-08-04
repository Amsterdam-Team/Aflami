package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.CountryRepository
import com.amsterdam.entity.Country
import com.amsterdam.repository.datasource.local.AppPreferences
import com.amsterdam.repository.datasource.local.CountryLocalSource
import com.amsterdam.repository.datasource.remote.CountryRemoteSource
import com.amsterdam.repository.dto.remote.RemoteCountryDto
import com.amsterdam.repository.mapper.local.toEntityList
import com.amsterdam.repository.mapper.remote.toEntityList
import com.amsterdam.repository.mapper.remoteToLocal.toLocalDtoList
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CountryRepositoryImpl @Inject constructor(
    private val localDataSource: CountryLocalSource,
    private val remoteDataSource: CountryRemoteSource,
    private val preferences: AppPreferences
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
            remoteCountries.toEntityList()
        }
    }

    private suspend fun getCountriesFromLocal(): List<Country> {
        return try {
            localDataSource.getCountries(storedLanguage = preferences.getDeviceLanguage().first())
                .toEntityList()
        } catch (_: Exception) {
            emptyList()
        }
    }

    private suspend fun saveCountries(remoteCountries: List<RemoteCountryDto>) {
        localDataSource.addCountries(
            remoteCountries.toLocalDtoList(preferences.getDeviceLanguage().first())
        )
    }
}
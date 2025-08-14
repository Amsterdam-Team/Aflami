package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.CountryRepository
import com.amsterdam.entity.Country
import com.amsterdam.repository.datasource.local.AppPreferences
import com.amsterdam.repository.datasource.local.CountryLocalDataSource
import com.amsterdam.repository.datasource.remote.CountryRemoteDataSource
import com.amsterdam.repository.dto.remote.RemoteCountryDto
import com.amsterdam.repository.mapper.toEntity
import com.amsterdam.repository.mapper.toLocalDtoList
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CountryRepositoryImpl @Inject constructor(
    private val localDataSource: CountryLocalDataSource,
    private val remoteDataSource: CountryRemoteDataSource,
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
            remoteCountries.map { it.toEntity() }
        }
    }

    private suspend fun getCountriesFromLocal(): List<Country> {
        return try {
            localDataSource.getCountries(storedLanguage = preferences.getAppLanguage().first())
                .map { it.toEntity() }
        } catch (_: Exception) {
            emptyList()
        }
    }

    private suspend fun saveCountries(remoteCountries: List<RemoteCountryDto>) {
        localDataSource.upsertCountries(
            remoteCountries.toLocalDtoList(preferences.getAppLanguage().first())
        )
    }
}
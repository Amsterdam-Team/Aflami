package com.example.remotedatasource.datasource

import com.example.remotedatasource.serviceProvider.CountryServiceProvider
import com.example.repository.datasource.remote.CountryRemoteSource
import com.example.repository.dto.remote.RemoteCountryDto

class CountryRemoteDataSourceImpl(
    private val countryServiceProvider: CountryServiceProvider
) : CountryRemoteSource {
    override suspend fun getCountries(): List<RemoteCountryDto> {
        return countryServiceProvider.getCountries()
    }
}
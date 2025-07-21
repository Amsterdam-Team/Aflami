package com.example.remotedatasource.datasource

import com.example.remotedatasource.api.CountryApiService
import com.example.remotedatasource.utils.apiHandler.responseCall
import com.example.repository.datasource.remote.CountryRemoteSource
import com.example.repository.dto.remote.RemoteCountryDto

class CountryRemoteDataSourceImpl(
    private val countryApiService: CountryApiService
) : CountryRemoteSource {
    override suspend fun getCountries(): List<RemoteCountryDto> {
        return responseCall { countryApiService.getCountries() }
    }
}
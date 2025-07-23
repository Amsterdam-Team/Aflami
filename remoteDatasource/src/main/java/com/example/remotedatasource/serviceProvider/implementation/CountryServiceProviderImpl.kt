package com.example.remotedatasource.serviceProvider.implementation

import com.example.remotedatasource.api.CountryApiService
import com.example.remotedatasource.serviceProvider.CountryServiceProvider
import com.example.remotedatasource.utils.apiHandler.responseCall
import com.example.repository.dto.remote.RemoteCountryDto

class CountryServiceProviderImpl(
    private val countryApiService: CountryApiService
) : CountryServiceProvider {
    override suspend fun getCountries(): List<RemoteCountryDto> {
        return responseCall { countryApiService.getCountries() }
    }
}
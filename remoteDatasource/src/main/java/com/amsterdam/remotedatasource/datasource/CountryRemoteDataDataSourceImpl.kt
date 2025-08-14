package com.amsterdam.remotedatasource.datasource

import com.amsterdam.remotedatasource.api.CountryApiService
import com.amsterdam.remotedatasource.utils.apiHandler.responseCall
import com.amsterdam.repository.datasource.remote.CountryRemoteDataSource
import com.amsterdam.repository.dto.remote.RemoteCountryDto
import javax.inject.Inject

class CountryRemoteDataDataSourceImpl @Inject constructor(
    private val countryApiService: CountryApiService
) : CountryRemoteDataSource {
    override suspend fun getCountries(): List<RemoteCountryDto> {
        return responseCall { countryApiService.getCountries() }
    }
}
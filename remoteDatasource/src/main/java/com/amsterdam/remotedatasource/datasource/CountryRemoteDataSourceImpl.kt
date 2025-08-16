package com.amsterdam.remotedatasource.datasource

import com.amsterdam.remotedatasource.api.CountryApiService
import com.amsterdam.remotedatasource.utils.apiHandler.responseCall
import com.amsterdam.repository.datasource.remote.CountryRemoteDataSource
import com.amsterdam.repository.dto.remote.CountryRemoteDto
import javax.inject.Inject

class CountryRemoteDataSourceImpl @Inject constructor(
    private val countryApiService: CountryApiService
) : CountryRemoteDataSource {
    override suspend fun getCountries(): List<CountryRemoteDto> {
        return responseCall(execute = { countryApiService.getCountries() })
    }
}
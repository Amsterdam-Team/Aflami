package com.example.remotedatasource.datasource

import com.example.remotedatasource.client.KtorClient
import com.example.remotedatasource.utils.apiHandler.safeCall
import com.example.repository.datasource.remote.CountryRemoteSource
import com.example.repository.dto.remote.RemoteCountryDto
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

class CountryRemoteSourceImpl(
    private val ktorClient: KtorClient,
    private val json: Json,
) : CountryRemoteSource {
    override suspend fun getCountries(): List<RemoteCountryDto> {
        return safeCall<List<RemoteCountryDto>> {
            val response = ktorClient.get(GET_COUNTRIES)
            return json.decodeFromString<List<RemoteCountryDto>>(response.bodyAsText())
        }
    }

    private companion object {
        const val GET_COUNTRIES = "configuration/countries"
    }
}
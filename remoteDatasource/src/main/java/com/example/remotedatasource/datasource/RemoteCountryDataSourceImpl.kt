package com.example.remotedatasource.datasource

import com.example.remotedatasource.client.KtorClient
import com.example.remotedatasource.client.safeCall
import com.example.remotedatasource.utils.Constant.BASE_URL
import com.example.repository.datasource.remote.RemoteCountryDataSource
import com.example.repository.dto.remote.RemoteCountryDto
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

class RemoteCountryDataSourceImpl(
    private val ktorClient: KtorClient,
    private val json: Json,
): RemoteCountryDataSource {
    override suspend fun getAllCountries(): List<RemoteCountryDto> {
        return safeCall<List<RemoteCountryDto>> {
            val response = ktorClient.get("$BASE_URL/configuration/countries")
            return json.decodeFromString<List<RemoteCountryDto>>(response.bodyAsText())
        }
    }
}
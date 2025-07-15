package com.example.remotedatasource.datasource

import com.example.remotedatasource.client.KtorClient
import com.example.remotedatasource.utils.apiHandler.safeCall
import com.example.repository.datasource.remote.RemoteTvShowsDatasource
import com.example.repository.dto.remote.RemoteTvShowResponse
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

class RemoteTvDatasourceImpl(private val ktorClient: KtorClient) : RemoteTvShowsDatasource {

    override suspend fun getTvShowsByKeyword(keyword: String): RemoteTvShowResponse {
        return safeCall<RemoteTvShowResponse> {
            val response = ktorClient.get(SEARCH_TV_URL) { parameter(QUERY_KEY, keyword) }
            return Json.decodeFromString<RemoteTvShowResponse>(response.bodyAsText())
        }
    }

    private companion object {
        const val QUERY_KEY = "query"
        const val SEARCH_TV_URL = "search/tv"
    }
}
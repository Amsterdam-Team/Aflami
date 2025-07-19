package com.example.remotedatasource.datasource

import com.example.remotedatasource.client.KtorClient
import com.example.remotedatasource.client.NetworkClient
import com.example.remotedatasource.utils.apiHandler.safeCall
import com.example.repository.datasource.remote.TvShowsRemoteSource
import com.example.repository.dto.remote.RemoteTvShowResponse
import io.ktor.client.request.parameter

class TvRemoteSourceImpl(
    private val networkClient: NetworkClient
) : TvShowsRemoteSource {

    override suspend fun getTvShowsByKeyword(keyword: String): RemoteTvShowResponse {
        return safeCall {
            networkClient.get(SEARCH_TV_URL) { parameter(QUERY_KEY, keyword) }
        }
    }

    private companion object {
        const val QUERY_KEY = "query"
        const val SEARCH_TV_URL = "search/tv"
    }
}
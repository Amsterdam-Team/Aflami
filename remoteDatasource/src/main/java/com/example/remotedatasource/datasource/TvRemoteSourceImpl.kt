package com.example.remotedatasource.datasource

import com.example.remotedatasource.client.KtorClient
import com.example.repository.datasource.remote.TvShowsRemoteSource
import com.example.repository.dto.remote.RemoteTvShowResponse
import io.ktor.client.request.parameter

class TvRemoteSourceImpl(private val ktorClient: KtorClient) : TvShowsRemoteSource {

    override suspend fun getTvShowsByKeyword(keyword: String, page: Int,): RemoteTvShowResponse {
        return ktorClient.tryToExecute {
            ktorClient
                .get(SEARCH_TV_URL) {
                    parameter(QUERY_KEY, keyword)
                    parameter(PAGE, page)
                }
        }
    }

    private companion object {
        const val QUERY_KEY = "query"
        const val PAGE = "page"
        const val SEARCH_TV_URL = "search/tv"
    }
}

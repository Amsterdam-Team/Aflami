package com.example.remotedatasource.datasource

import com.example.remotedatasource.base.BaseRemoteSource
import com.example.remotedatasource.client.KtorClient
import com.example.repository.datasource.remote.TvShowsRemoteSource
import com.example.repository.dto.remote.RemoteTvShowResponse
import io.ktor.client.request.parameter

class TvRemoteSourceImpl(
    ktorClient: KtorClient
) : BaseRemoteSource(ktorClient), TvShowsRemoteSource {

    override suspend fun getTvShowsByKeyword(keyword: String): RemoteTvShowResponse {
        val rawResponse: RemoteTvShowResponse = safeExecute {
            ktorClient.get(SEARCH_TV_URL) { parameter(QUERY_KEY, keyword) }
        }
        return enrichTvShowResponseWithFullUrls(rawResponse)
    }

    private fun enrichTvShowResponseWithFullUrls(response: RemoteTvShowResponse): RemoteTvShowResponse {
        return response.copy(
            results = response.results.map { itemDto ->
                itemDto.copy(posterPath = BASE_IMAGE_URL + itemDto.posterPath.orEmpty())
            }
        )
    }


    private companion object {
        const val QUERY_KEY = "query"
        const val SEARCH_TV_URL = "search/tv"
    }
}
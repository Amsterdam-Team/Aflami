package com.example.remotedatasource.datasource

import com.example.remotedatasource.client.KtorClient
import com.example.remotedatasource.utils.apiHandler.safeCall
import com.example.remotedatasource.utils.constants.RemoteDataSourceConstants
import com.example.repository.datasource.remote.TvShowsRemoteSource
import com.example.repository.dto.remote.RemoteTvShowResponse
import io.ktor.client.request.parameter

class TvRemoteSourceImpl(
    private val ktorClient: KtorClient
) : TvShowsRemoteSource {

    override suspend fun getTvShowsByKeyword(keyword: String): RemoteTvShowResponse {
        val rawResponse: RemoteTvShowResponse = safeCall {
            ktorClient.get(SEARCH_TV_URL) { parameter(QUERY_KEY, keyword) }
        }
        return enrichTvShowResponseWithFullUrls(rawResponse)
    }

    private fun enrichTvShowResponseWithFullUrls(response: RemoteTvShowResponse): RemoteTvShowResponse {
        return response.copy(
            results = response.results.map { itemDto ->
                itemDto.copy(posterPath = RemoteDataSourceConstants.BASE_IMAGE_URL + itemDto.posterPath.orEmpty())
            }
        )
    }

    private companion object {
        const val QUERY_KEY = "query"
        const val SEARCH_TV_URL = "search/tv"
    }
}
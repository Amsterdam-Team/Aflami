package com.example.remotedatasource.datasource

import com.example.remotedatasource.client.KtorClient
import com.example.remotedatasource.utils.apiHandler.safeCall
import com.example.repository.datasource.remote.TvShowsRemoteSource
import com.example.repository.dto.remote.RemoteTvShowResponse
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

class TvRemoteSourceImpl(
    private val ktorClient: KtorClient,
) : TvShowsRemoteSource {
    override suspend fun getTvShowsByKeyword(
        keyword: String,
        page: Int,
    ): RemoteTvShowResponse {
        return safeCall<RemoteTvShowResponse> {
            val response =
                ktorClient
                    .get(SEARCH_TV_URL) {
                        parameter(QUERY_KEY, keyword)
                        parameter(PAGE, page)
                    }
            return Json.decodeFromString<RemoteTvShowResponse>(response.bodyAsText())
        }
    }

    private companion object {
        const val QUERY_KEY = "query"
        const val PAGE = "page"
        const val SEARCH_TV_URL = "search/tv"
    }
}

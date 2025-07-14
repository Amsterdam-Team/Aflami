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

    override suspend fun discoverTvShows(
        keyword: String,
        rating: Float,
        genreId: Int?
    ): RemoteTvShowResponse {
        return safeCall<RemoteTvShowResponse> {
            val response = ktorClient.get(DISCOVER_TV_URL) {
                parameter(QUERY_KEY, keyword)
                parameter(VOTE_AVERAGE_KEY, rating)
                if (genreId != null) parameter(WITH_GENRES_KEY, genreId)
            }
            return Json.decodeFromString<RemoteTvShowResponse>(response.bodyAsText())
        }
    }

    private companion object {
        const val QUERY_KEY = "query"

        const val SEARCH_TV_URL = "search/tv"

        const val DISCOVER_TV_URL = "discover/tv"

        const val VOTE_AVERAGE_KEY = "vote_average.gte"
        const val WITH_GENRES_KEY = "with_genres"
    }
}
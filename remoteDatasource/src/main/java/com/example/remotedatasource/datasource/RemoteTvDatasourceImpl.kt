package com.example.remotedatasource.datasource

import com.example.remotedatasource.client.Endpoints
import com.example.remotedatasource.client.KtorClient
import com.example.remotedatasource.client.safeCall
import com.example.repository.datasource.remote.RemoteTvShowsDatasource
import com.example.repository.dto.remote.RemoteTvShowResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

class RemoteTvDatasourceImpl(private val ktorClient: KtorClient) : RemoteTvShowsDatasource {

    override suspend fun getTvShowsByKeyword(
        keyword: String,
        rating: Float,
        genreId: Int?
    ): RemoteTvShowResponse {
        return safeCall<RemoteTvShowResponse> {
            val baseUrl = Endpoints.DISCOVER_TV_URL
            val params = buildList {
                add("$QUERY_KEY=$keyword")
                add("$VOTE_AVERAGE_KEY=$rating")
                if (genreId != null) add("with_genres=$genreId")
            }.joinToString("&")

            val response = ktorClient.get("$baseUrl?$params")
            return Json.decodeFromString<RemoteTvShowResponse>(response.bodyAsText())
        }
    }

    private companion object {
        const val QUERY_KEY = "query"
        const val VOTE_AVERAGE_KEY = "vote_average.gte"
    }
}
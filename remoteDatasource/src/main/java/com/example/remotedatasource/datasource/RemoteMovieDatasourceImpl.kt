package com.example.remotedatasource.datasource

import com.example.remotedatasource.BuildConfig
import com.example.remotedatasource.client.Endpoints
import com.example.remotedatasource.client.KtorClient
import com.example.remotedatasource.client.safeCall
import com.example.remotedatasource.utils.Constant.BASE_URL
import com.example.repository.datasource.remote.RemoteMovieDatasource
import com.example.repository.dto.remote.RemoteActorSearchResponse
import com.example.repository.dto.remote.RemoteMovieResponse
import io.ktor.client.call.body
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

class RemoteMovieDatasourceImpl(
    private val ktorClient: KtorClient,
    private val json: Json,
) : RemoteMovieDatasource {
    override suspend fun getMoviesByKeyword(
        keyword: String
    ): RemoteMovieResponse {
        return ktorClient.get("${Endpoints.SEARCH_MOVIE_URL}?$QUERY_KEY=$keyword")
            .body()
    }

    override suspend fun getMoviesByActorName(
        name: String
    ): RemoteMovieResponse {
        return safeCall<RemoteMovieResponse> {
            val actorsByName = getActorIdByName(name)
                .actors
                .joinToString(separator = "|") { it.name }

            ktorClient.get("${Endpoints.SEARCH_MOVIE_URL}?$WITH_CAST_KEY=$actorsByName")
        }
    }

    private suspend fun getActorIdByName(
        name: String
    ): RemoteActorSearchResponse {
        return safeCall<RemoteActorSearchResponse> {
            ktorClient.get("${Endpoints.GET_ACTOR_NAME_BY_ID_URL}?$QUERY_KEY=$name")
        }
    }

    override suspend fun getMoviesByCountryIsoCode(
        countryIsoCode: String
    ): RemoteMovieResponse {
        return safeCall<RemoteMovieResponse> {
            val response = ktorClient.get("${BuildConfig.BASE_URL}/discover/movie?with_origin_country=$countryIsoCode")
            return json.decodeFromString<RemoteMovieResponse>(response.bodyAsText())
        }
    }

    private companion object {
        const val WITH_CAST_KEY = "with_cast"
        const val QUERY_KEY = "query"
    }
}
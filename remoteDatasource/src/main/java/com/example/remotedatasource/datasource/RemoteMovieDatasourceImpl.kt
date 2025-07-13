package com.example.remotedatasource.datasource

import android.util.Log
import com.example.remotedatasource.BuildConfig
import com.example.remotedatasource.client.Endpoints
import com.example.remotedatasource.client.KtorClient
import com.example.remotedatasource.client.safeCall
import com.example.repository.datasource.remote.RemoteMovieDatasource
import com.example.repository.dto.remote.RemoteActorSearchResponse
import com.example.repository.dto.remote.RemoteMovieResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
class RemoteMovieDatasourceImpl(
    private val ktorClient: KtorClient,
    private val json: Json,
) : RemoteMovieDatasource {
    override suspend fun getMoviesByKeyword(
        keyword: String,
        rating: Float
    ): RemoteMovieResponse {
        return safeCall<RemoteMovieResponse> {
            val response = ktorClient.get("${Endpoints.SEARCH_MOVIE_URL}?$QUERY_KEY=$keyword&$VOTE_AVERAGE_KEY=$rating")
            Log.e("bk", "bodyAsText: ${response.bodyAsText()}")
            return json.decodeFromString<RemoteMovieResponse>(response.bodyAsText())
        }
    }

    override suspend fun getMoviesByActorName(
        name: String
    ): RemoteMovieResponse {
        return safeCall<RemoteMovieResponse> {
            val actorsByName = getActorIdByName(name)
                .actors
                .joinToString(separator = "|") { it.id.toString() }
            ktorClient.get("${Endpoints.SEARCH_MOVIE_URL}?$WITH_CAST_KEY=${actorsByName}")
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
        const val VOTE_AVERAGE_KEY = "vote_average.gte"
    }
}
package com.example.remotedatasource.datasource

import com.example.remotedatasource.BuildConfig
import com.example.remotedatasource.client.Endpoints
import com.example.remotedatasource.client.KtorClient
import com.example.remotedatasource.client.safeCall
import com.example.repository.datasource.remote.RemoteMovieDatasource
import com.example.repository.dto.remote.RemoteActorSearchResponse
import com.example.repository.dto.remote.RemoteMovieResponse
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

class RemoteMovieDatasourceImpl(
    private val ktorClient: KtorClient,
    private val json: Json,
) : RemoteMovieDatasource {
    override suspend fun getMoviesByKeyword(
        keyword: String,
        rating: Float,
        genreId: Int?
    ): RemoteMovieResponse {
        return safeCall<RemoteMovieResponse> {
            val baseUrl = Endpoints.DISCOVER_MOVIE_URL
            val params = buildList {
                add("$QUERY_KEY=$keyword")
                add("$VOTE_AVERAGE_KEY=$rating")
                if (genreId != null) add("with_genres=$genreId")
            }.joinToString("&")

            val response = ktorClient.get("$baseUrl?$params")
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

            ktorClient.get(Endpoints.SEARCH_MOVIE_URL) {
                parameter(WITH_CAST_KEY, actorsByName)
            }
        }
    }

    private suspend fun getActorIdByName(
        name: String
    ): RemoteActorSearchResponse {
        return safeCall<RemoteActorSearchResponse> {
            ktorClient.get(Endpoints.GET_ACTOR_NAME_BY_ID_URL) {
                parameter(QUERY_KEY, name)
            }
        }
    }

    override suspend fun getMoviesByCountryIsoCode(
        countryIsoCode: String
    ): RemoteMovieResponse {
        return safeCall<RemoteMovieResponse> {
            val response = ktorClient.get(Endpoints.DISCOVER_MOVIE) {
                parameter(WITH_ORIGIN_COUNTRY, countryIsoCode)
            }
            return json.decodeFromString<RemoteMovieResponse>(response.bodyAsText())
        }
    }

    private companion object {
        const val WITH_CAST_KEY = "with_cast"
        const val QUERY_KEY = "query"

        const val WITH_ORIGIN_COUNTRY = "with_origin_country"
        const val VOTE_AVERAGE_KEY = "vote_average.gte"
    }
}
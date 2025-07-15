package com.example.remotedatasource.datasource

import com.example.domain.exceptions.NoSearchByActorResultFoundException
import com.example.remotedatasource.client.KtorClient
import com.example.remotedatasource.utils.apiHandler.safeCall
import com.example.repository.datasource.remote.RemoteMovieDatasource
import com.example.repository.dto.remote.RemoteActorSearchResponse
import com.example.repository.dto.remote.RemoteCastAndCrewResponse
import com.example.repository.dto.remote.RemoteMovieResponse
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

class RemoteMovieDatasourceImpl(
    private val ktorClient: KtorClient,
    private val json: Json,
) : RemoteMovieDatasource {

    override suspend fun getMoviesByKeyword(keyword: String): RemoteMovieResponse {
        return safeCall<RemoteMovieResponse> {
            val response = ktorClient.get(SEARCH_MOVIE_URL) { parameter(QUERY_KEY, keyword) }
            return json.decodeFromString<RemoteMovieResponse>(response.bodyAsText())
        }
    }

    override suspend fun discoverMovies(
        keyword: String,
        rating: Float,
        genreId: Int?
    ): RemoteMovieResponse {
        return safeCall<RemoteMovieResponse> {
            val response = ktorClient.get(DISCOVER_MOVIE) {
                parameter(QUERY_KEY, keyword)
                parameter(VOTE_AVERAGE_KEY, rating)
                if (genreId != null) parameter(WITH_GENRES_KEY, genreId)
            }
            return Json.decodeFromString<RemoteMovieResponse>(response.bodyAsText())
        }
    }

    override suspend fun getMoviesByActorName(
        name: String
    ): RemoteMovieResponse {
        return safeCall<RemoteMovieResponse> {
            val actorsByName = getActorIdByName(name)
                .actors
                .joinToString(separator = "|") { it.id.toString() }
                .ifEmpty {
                    throw NoSearchByActorResultFoundException()
                }

            ktorClient.get(DISCOVER_MOVIE) {
                parameter(WITH_CAST_KEY, actorsByName)
            }
        }
    }

    private suspend fun getActorIdByName(
        name: String
    ): RemoteActorSearchResponse {
        return safeCall<RemoteActorSearchResponse> {
            ktorClient.get(GET_ACTOR_NAME_BY_ID_URL) {
                parameter(QUERY_KEY, name)
            }
        }
    }

    override suspend fun getMoviesByCountryIsoCode(
        countryIsoCode: String
    ): RemoteMovieResponse {
        return safeCall<RemoteMovieResponse> {
            val response = ktorClient.get(DISCOVER_MOVIE) {
                parameter(WITH_ORIGIN_COUNTRY, countryIsoCode)
            }
            return json.decodeFromString<RemoteMovieResponse>(response.bodyAsText())
        }
    }

    override suspend fun getCastByMovieId(id: Long): RemoteCastAndCrewResponse {
        return safeCall<RemoteCastAndCrewResponse> {
            val response = ktorClient.get(buildMovieCreditsEndpoint(id))
            return json.decodeFromString<RemoteCastAndCrewResponse>(response.bodyAsText())
        }
    }

    private fun buildMovieCreditsEndpoint(movieId: Long) = "movie/$movieId/credits"

    private companion object {
        const val SEARCH_MOVIE_URL = "search/movie"
        const val GET_ACTOR_NAME_BY_ID_URL = "search/person"

        const val DISCOVER_MOVIE = "discover/movie"

        const val WITH_CAST_KEY = "with_cast"
        const val QUERY_KEY = "query"

        const val WITH_ORIGIN_COUNTRY = "with_origin_country"
        const val WITH_GENRES_KEY = "with_genres"
        const val VOTE_AVERAGE_KEY = "vote_average.gte"
    }
}
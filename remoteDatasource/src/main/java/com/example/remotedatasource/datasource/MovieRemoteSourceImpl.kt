package com.example.remotedatasource.datasource

import com.example.domain.exceptions.NoSearchByActorResultFoundException
import com.example.remotedatasource.client.KtorClient
import com.example.repository.datasource.remote.MovieRemoteSource
import com.example.repository.dto.remote.RemoteActorSearchResponse
import com.example.repository.dto.remote.RemoteMovieResponse
import io.ktor.client.request.parameter

class MovieRemoteSourceImpl(
    private val ktorClient: KtorClient,
) : MovieRemoteSource {
    override suspend fun getMoviesByKeyword(
        keyword: String,
        page: Int,
    ): RemoteMovieResponse {
        return ktorClient.tryToExecute {

                ktorClient
                    .get(SEARCH_MOVIE_URL) {
                        parameter(QUERY_KEY, keyword)
                        parameter(PAGE, page)
                    }

        }
    }

    override suspend fun getMoviesByActorName(name: String, page: Int): RemoteMovieResponse {
        val actorsByName = getActorIdByName(name, page)
            .actors
            .joinToString(separator = "|") { it.id.toString() }
            .ifEmpty { throw NoSearchByActorResultFoundException() }

        return ktorClient.tryToExecute {
            ktorClient.get(DISCOVER_MOVIE) { parameter(WITH_CAST_KEY, actorsByName) }
        }
    }

    private suspend fun getActorIdByName(
        name: String,
        page: Int,
    ): RemoteActorSearchResponse {
        return ktorClient.tryToExecute {
            ktorClient.get(GET_ACTOR_NAME_BY_ID_URL) {
                parameter(QUERY_KEY, name)
                parameter(PAGE, page)
            }
        }
    }

    override suspend fun getMoviesByCountryIsoCode(countryIsoCode: String, page: Int, ): RemoteMovieResponse {
        return ktorClient.tryToExecute {
            ktorClient
                .get(DISCOVER_MOVIE) {
                    parameter(WITH_ORIGIN_COUNTRY, countryIsoCode)
                    parameter(PAGE, page)
                }
        }
    }

    private companion object {
        const val SEARCH_MOVIE_URL = "search/movie"
        const val GET_ACTOR_NAME_BY_ID_URL = "search/person"

        const val DISCOVER_MOVIE = "discover/movie"

        const val WITH_CAST_KEY = "with_cast"
        const val QUERY_KEY = "query"
        const val PAGE = "page"

        const val WITH_ORIGIN_COUNTRY = "with_origin_country"
    }
}
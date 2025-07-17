package com.example.remotedatasource.datasource

import com.example.domain.exceptions.NoSearchByActorResultFoundException
import com.example.remotedatasource.base.BaseRemoteSource
import com.example.remotedatasource.client.KtorClient
import com.example.repository.datasource.remote.MovieRemoteSource
import com.example.repository.dto.remote.RemoteActorSearchResponse
import com.example.repository.dto.remote.RemoteMovieResponse
import io.ktor.client.request.parameter

class MovieRemoteSourceImpl(
    ktorClient: KtorClient,
) : BaseRemoteSource(ktorClient), MovieRemoteSource {

    override suspend fun getMoviesByKeyword(keyword: String): RemoteMovieResponse {
        val rawResponse: RemoteMovieResponse = safeExecute {
            ktorClient.get(SEARCH_MOVIE_URL) { parameter(QUERY_KEY, keyword) }
        }
        return enrichMovieResponseWithFullUrls(rawResponse)
    }

    override suspend fun getMoviesByActorName(name: String): RemoteMovieResponse {
        val actorsByName = getActorIdByName(name)
            .actors
            .joinToString(separator = "|") { it.id.toString() }
            .ifEmpty { throw NoSearchByActorResultFoundException() }

        val rawResponse: RemoteMovieResponse = safeExecute {
            ktorClient.get(DISCOVER_MOVIE) { parameter(WITH_CAST_KEY, actorsByName) }
        }
        return enrichMovieResponseWithFullUrls(rawResponse)
    }

    private suspend fun getActorIdByName(name: String): RemoteActorSearchResponse {
        return ktorClient.tryToExecute {
            ktorClient.get(GET_ACTOR_NAME_BY_ID_URL) { parameter(QUERY_KEY, name) }
        }
    }

    override suspend fun getMoviesByCountryIsoCode(countryIsoCode: String): RemoteMovieResponse {
        val rawResponse: RemoteMovieResponse = safeExecute {
            ktorClient.get(DISCOVER_MOVIE) { parameter(WITH_ORIGIN_COUNTRY, countryIsoCode) }
        }
        return enrichMovieResponseWithFullUrls(rawResponse)
    }

    private fun enrichMovieResponseWithFullUrls(response: RemoteMovieResponse): RemoteMovieResponse {
        return response.copy(
            results = response.results.map { itemDto ->
                itemDto.copy(posterPath = BASE_IMAGE_URL + itemDto.posterPath.orEmpty())
            }
        )
    }

    private companion object {
        const val SEARCH_MOVIE_URL = "search/movie"
        const val GET_ACTOR_NAME_BY_ID_URL = "search/person"

        const val DISCOVER_MOVIE = "discover/movie"

        const val WITH_CAST_KEY = "with_cast"
        const val QUERY_KEY = "query"

        const val WITH_ORIGIN_COUNTRY = "with_origin_country"
    }
}
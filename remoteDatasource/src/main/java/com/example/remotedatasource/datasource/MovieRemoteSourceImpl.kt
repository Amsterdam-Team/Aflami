package com.example.remotedatasource.datasource

import com.example.remotedatasource.client.NetworkClient
import com.example.remotedatasource.utils.apiHandler.safeCall
import com.example.repository.datasource.remote.MovieRemoteSource
import com.example.repository.dto.remote.ProductionCompanyResponse
import com.example.repository.dto.remote.RemoteActorSearchResponse
import com.example.repository.dto.remote.RemoteCastAndCrewResponse
import com.example.repository.dto.remote.RemoteMovieItemDto
import com.example.repository.dto.remote.RemoteMovieResponse
import com.example.repository.dto.remote.movieGallery.RemoteMovieGalleryResponse
import com.example.repository.dto.remote.review.ReviewsResponse
import io.ktor.client.request.parameter

class MovieRemoteSourceImpl(
    private val networkClient: NetworkClient
) : MovieRemoteSource {

    override suspend fun getMoviesByKeyword(keyword: String): RemoteMovieResponse {
        return safeCall {
            networkClient.get(SEARCH_MOVIE_URL) { parameter(QUERY_KEY, keyword) }
        }
    }

    override suspend fun getMoviesByActorName(name: String): RemoteMovieResponse {
        val actorsByName = getActorIdByName(name)
            .actors
            .joinToString(separator = "|") { it.id.toString() }

        return safeCall {
            networkClient.get(DISCOVER_MOVIE) { parameter(WITH_CAST_KEY, actorsByName) }
        }
    }

    private suspend fun getActorIdByName(name: String): RemoteActorSearchResponse {
        return safeCall {
            networkClient.get(GET_ACTOR_NAME_BY_ID_URL) { parameter(QUERY_KEY, name) }
        }
    }

    override suspend fun getMoviesByCountryIsoCode(countryIsoCode: String): RemoteMovieResponse {
        return safeCall {
            networkClient.get(DISCOVER_MOVIE) { parameter(WITH_ORIGIN_COUNTRY, countryIsoCode) }
        }
    }

    override suspend fun getCastByMovieId(movieId: Long): RemoteCastAndCrewResponse {
        return safeCall {
            networkClient.get(buildMovieCreditsEndpoint(movieId))
        }
    }

    private fun buildMovieCreditsEndpoint(movieId: Long) = "movie/$movieId/credits"

    override suspend fun getMovieReviews(movieId: Long): ReviewsResponse {
        return safeCall {
            networkClient.get("movie/$movieId/reviews")
        }
    }

    override suspend fun getSimilarMovies(movieId: Long): RemoteMovieResponse {
        return safeCall {
            networkClient.get("movie/$movieId/similar")
        }
    }

    override suspend fun getMovieGallery(movieId: Long): RemoteMovieGalleryResponse {
        return safeCall {
            networkClient.get("movie/$movieId/images")
        }
    }

    override suspend fun getProductionCompany(movieId: Long): ProductionCompanyResponse {
        return safeCall {
            networkClient.get("movie/$movieId")
        }
    }

    override suspend fun getMovieDetailsById(movieId: Long): RemoteMovieItemDto {
        return safeCall {
            networkClient.get("movie/$movieId")
        }
    }

    override suspend fun getMoviePosters(movieId: Long): RemoteMovieGalleryResponse {
        return safeCall<RemoteMovieGalleryResponse> {
            networkClient.get("movie/$movieId/images")
        }
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
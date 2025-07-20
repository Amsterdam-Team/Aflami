package com.example.remotedatasource.datasource

import com.example.remotedatasource.client.NetworkClient
import com.example.remotedatasource.utils.apiHandler.responseCall
import com.example.repository.datasource.remote.MovieRemoteSource
import com.example.repository.dto.remote.ProductionCompanyResponse
import com.example.repository.dto.remote.RemoteActorSearchResponse
import com.example.repository.dto.remote.RemoteCastAndCrewResponse
import com.example.repository.dto.remote.RemoteMovieItemDto
import com.example.repository.dto.remote.RemoteMovieResponse
import com.example.repository.dto.remote.movieGallery.RemoteMovieGalleryResponse
import com.example.repository.dto.remote.review.ReviewsResponse
import io.ktor.client.request.parameter

class MovieRemoteDataSourceImpl(
    private val networkClient: NetworkClient
) : MovieRemoteSource {

    override suspend fun getMoviesByKeyword(keyword: String, page: Int): RemoteMovieResponse {
        return responseCall {
            networkClient.get(SEARCH_MOVIE_URL) {
                parameter(QUERY_KEY, keyword)
                parameter(PAGE, page)
            }
        }
    }

    override suspend fun getMoviesByActorName(name: String, page: Int): RemoteMovieResponse {
        val actorsByName = getActorIdByName(name, page)
            .actors
            .joinToString(separator = "|") { it.id.toString() }

        return responseCall {
            networkClient.get(DISCOVER_MOVIE) { parameter(WITH_CAST_KEY, actorsByName) }
        }
    }

    private suspend fun getActorIdByName(name: String, page: Int): RemoteActorSearchResponse {
        return responseCall {
            networkClient.get(GET_ACTOR_NAME_BY_ID_URL) {
                parameter(QUERY_KEY, name)
                parameter(PAGE, page)
            }
        }
    }

    override suspend fun getMoviesByCountryIsoCode(countryIsoCode: String, page: Int): RemoteMovieResponse {
        return responseCall {
            networkClient.get(DISCOVER_MOVIE) {
                parameter(WITH_ORIGIN_COUNTRY, countryIsoCode)
                parameter(PAGE, page)
            }
        }
    }

    override suspend fun getCastByMovieId(movieId: Long): RemoteCastAndCrewResponse {
        return responseCall {
            networkClient.get(buildMovieCreditsEndpoint(movieId))
        }
    }

    private fun buildMovieCreditsEndpoint(movieId: Long) = "movie/$movieId/credits"

    override suspend fun getMovieReviews(movieId: Long): ReviewsResponse {
        return responseCall {
            networkClient.get("movie/$movieId/reviews")
        }
    }

    override suspend fun getSimilarMovies(movieId: Long): RemoteMovieResponse {
        return responseCall {
            networkClient.get("movie/$movieId/similar")
        }
    }

    override suspend fun getMovieGallery(movieId: Long): RemoteMovieGalleryResponse {
        return responseCall {
            networkClient.get("movie/$movieId/images")
        }
    }

    override suspend fun getProductionCompany(movieId: Long): ProductionCompanyResponse {
        return responseCall {
            networkClient.get("movie/$movieId")
        }
    }

    override suspend fun getMovieDetailsById(movieId: Long): RemoteMovieItemDto {
        return responseCall {
            networkClient.get("movie/$movieId")
        }
    }

    override suspend fun getMoviePosters(movieId: Long): RemoteMovieGalleryResponse {
        return responseCall<RemoteMovieGalleryResponse> {
            networkClient.get("movie/$movieId/images")
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
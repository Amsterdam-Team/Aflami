package com.example.remotedatasource.datasource

import com.example.remotedatasource.client.KtorClient
import com.example.remotedatasource.utils.apiHandler.safeCall
import com.example.remotedatasource.utils.constants.RemoteDataSourceConstants
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
    private val ktorClient: KtorClient
) : MovieRemoteSource {

    override suspend fun getMoviesByKeyword(keyword: String): RemoteMovieResponse {
        val rawResponse: RemoteMovieResponse = safeCall {
            ktorClient.get(SEARCH_MOVIE_URL) { parameter(QUERY_KEY, keyword) }
        }
        return enrichMovieResponseWithFullUrls(rawResponse)
    }

    override suspend fun getMoviesByActorName(name: String): RemoteMovieResponse {
        val actorsByName = getActorIdByName(name)
            .actors
            .joinToString(separator = "|") { it.id.toString() }

        val rawResponse: RemoteMovieResponse = safeCall {
            ktorClient.get(DISCOVER_MOVIE) { parameter(WITH_CAST_KEY, actorsByName) }
        }
        return enrichMovieResponseWithFullUrls(rawResponse)
    }

    private suspend fun getActorIdByName(name: String): RemoteActorSearchResponse {
        return safeCall {
            ktorClient.get(GET_ACTOR_NAME_BY_ID_URL) { parameter(QUERY_KEY, name) }
        }
    }

    override suspend fun getMoviesByCountryIsoCode(countryIsoCode: String): RemoteMovieResponse {
        val rawResponse: RemoteMovieResponse = safeCall {
            ktorClient.get(DISCOVER_MOVIE) { parameter(WITH_ORIGIN_COUNTRY, countryIsoCode) }
        }
        return enrichMovieResponseWithFullUrls(rawResponse)
    }

    override suspend fun getCastByMovieId(movieId: Long): RemoteCastAndCrewResponse {
        return safeCall {
            ktorClient.get(buildMovieCreditsEndpoint(movieId))
        }
    }

    private fun buildMovieCreditsEndpoint(movieId: Long) = "movie/$movieId/credits"

    override suspend fun getMovieReviews(movieId: Long): ReviewsResponse {
        return safeCall {
            ktorClient.get("movie/$movieId/reviews")
        }
    }

    override suspend fun getSimilarMovies(movieId: Long): RemoteMovieResponse {
        val rawResponse: RemoteMovieResponse = safeCall {
            ktorClient.get("movie/$movieId/similar")
        }
        return enrichMovieResponseWithFullUrls(rawResponse)
    }

    override suspend fun getMovieGallery(movieId: Long): RemoteMovieGalleryResponse {
        return safeCall {
            ktorClient.get("movie/$movieId/images")
        }
    }

    override suspend fun getProductionCompany(movieId: Long): ProductionCompanyResponse {
        return safeCall {
            ktorClient.get("movie/$movieId")
        }
    }

    override suspend fun getMovieDetailsById(movieId: Long): RemoteMovieItemDto {
        return safeCall {
            ktorClient.get("movie/$movieId")
        }
    }

    private fun enrichMovieResponseWithFullUrls(response: RemoteMovieResponse): RemoteMovieResponse {
        return response.copy(
            results = response.results.map { itemDto ->
                itemDto.copy(posterPath = RemoteDataSourceConstants.BASE_IMAGE_URL + itemDto.posterPath.orEmpty())
            }
        )
    }

    override suspend fun getMoviePosters(movieId: Long): RemoteMovieGalleryResponse {
        return safeCall<RemoteMovieGalleryResponse> {
            val response = ktorClient.get("movie/$movieId/images")
            return json.decodeFromString<RemoteMovieGalleryResponse>(response.bodyAsText())
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
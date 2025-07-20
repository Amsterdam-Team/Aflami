package com.example.remotedatasource.datasource

import com.example.remotedatasource.client.KtorClient
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
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

class MovieRemoteSourceImpl(
    private val ktorClient: KtorClient,
    private val json: Json
) : MovieRemoteSource {
    override suspend fun getMoviesByKeyword(
        keyword: String,
        page: Int,
    ): RemoteMovieResponse =
        safeCall {
            ktorClient.get(SEARCH_MOVIE_URL) {
                parameter(QUERY_KEY, keyword)
                parameter(PAGE, page)
            }
        }

    override suspend fun getMoviesByActorName(name: String, page: Int): RemoteMovieResponse {
        val actorsByName = getActorIdByName(name, page)
            .actors
            .joinToString(separator = "|") { it.id.toString() }

        return safeCall {
            ktorClient.get(DISCOVER_MOVIE) { parameter(WITH_CAST_KEY, actorsByName) }
        }
    }

    private suspend fun getActorIdByName(
        name: String,
        page: Int,
    ): RemoteActorSearchResponse =
        safeCall {
            ktorClient.get(GET_ACTOR_NAME_BY_ID_URL) {
                parameter(QUERY_KEY, name)
                parameter(PAGE, page)
            }
        }

    override suspend fun getMoviesByCountryIsoCode(
        countryIsoCode: String,
        page: Int): RemoteMovieResponse {
        return safeCall{
            ktorClient.get(DISCOVER_MOVIE) {
                parameter(WITH_ORIGIN_COUNTRY, countryIsoCode)
                parameter(PAGE, page)
            }
        }
    }

    override suspend fun getCastByMovieId(movieId: Long): RemoteCastAndCrewResponse {
        return safeCall {
            val response = ktorClient.get(buildMovieCreditsEndpoint(movieId))
            return json.decodeFromString<RemoteCastAndCrewResponse>(response.bodyAsText())
        }
    }

    private fun buildMovieCreditsEndpoint(movieId: Long) = "movie/$movieId/credits"

    override suspend fun getMovieReviews(movieId: Long): ReviewsResponse {
        return safeCall<ReviewsResponse> {
            val response = ktorClient.get("movie/$movieId/reviews")
            return json.decodeFromString<ReviewsResponse>(response.bodyAsText())
        }
    }

    override suspend fun getSimilarMovies(movieId: Long): RemoteMovieResponse {
        return safeCall<RemoteMovieResponse> {
            val response = ktorClient.get("movie/$movieId/similar")
            return json.decodeFromString<RemoteMovieResponse>(response.bodyAsText())
        }
    }

    override suspend fun getMovieGallery(movieId: Long): RemoteMovieGalleryResponse {
        return safeCall<RemoteMovieGalleryResponse> {
            val response = ktorClient.get("movie/$movieId/images")
            return json.decodeFromString<RemoteMovieGalleryResponse>(response.bodyAsText())
        }
    }

    override suspend fun getProductionCompany(movieId: Long): ProductionCompanyResponse {
        return safeCall<ProductionCompanyResponse> {
            val response = ktorClient.get("movie/$movieId")
            return json.decodeFromString<ProductionCompanyResponse>(response.bodyAsText())
        }
    }

    override suspend fun getMovieDetailsById(movieId: Long): RemoteMovieItemDto {
        return safeCall<RemoteMovieItemDto> {
            val response = ktorClient.get("movie/$movieId")
            return json.decodeFromString<RemoteMovieItemDto>(response.bodyAsText())
        }
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
        const val PAGE = "page"

        const val WITH_ORIGIN_COUNTRY = "with_origin_country"
    }
}
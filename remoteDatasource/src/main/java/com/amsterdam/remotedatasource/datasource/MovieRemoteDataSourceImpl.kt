package com.amsterdam.remotedatasource.datasource

import com.amsterdam.remotedatasource.api.MovieApiService
import com.amsterdam.remotedatasource.utils.apiHandler.responseCall
import com.amsterdam.repository.datasource.remote.MovieRemoteSource
import com.amsterdam.repository.dto.remote.ProductionCompanyResponse
import com.amsterdam.repository.dto.remote.RemoteActorSearchResponse
import com.amsterdam.repository.dto.remote.RemoteCastAndCrewResponse
import com.amsterdam.repository.dto.remote.RemoteMovieItemDto
import com.amsterdam.repository.dto.remote.RemoteMovieResponse
import com.amsterdam.repository.dto.remote.movieGallery.RemoteGalleryResponse
import com.amsterdam.repository.dto.remote.review.ReviewsResponse
import javax.inject.Inject

class MovieRemoteDataSourceImpl @Inject constructor(
    private val movieApiService: MovieApiService
) : MovieRemoteSource {

    override suspend fun getMoviesByKeyword(keyword: String, page: Int): RemoteMovieResponse {
        return responseCall { movieApiService.getMoviesByKeyword(keyword, page) }
    }

    override suspend fun getMoviesByActorName(name: String, page: Int): RemoteMovieResponse {
        val actorId = getActorIdByName(name, page)
            .actors
            .joinToString(separator = "|") { it.id.toString() }
        return responseCall { movieApiService.getMoviesByActorId(actorId) }
    }

    private suspend fun getActorIdByName(name: String, page: Int): RemoteActorSearchResponse {
        return responseCall { movieApiService.getActorIdByName(name, page) }
    }

    override suspend fun getMoviesByCountryIsoCode(
        countryIsoCode: String,
        page: Int
    ): RemoteMovieResponse {
        return responseCall { movieApiService.getMoviesByCountryIsoCode(countryIsoCode, page) }
    }

    override suspend fun getCastByMovieId(movieId: Long): RemoteCastAndCrewResponse {
        return responseCall { movieApiService.getCastByMovieId(movieId) }
    }

    override suspend fun getMovieReviews(movieId: Long): ReviewsResponse {
        return responseCall { movieApiService.getMovieReviews(movieId) }
    }

    override suspend fun getSimilarMovies(movieId: Long): RemoteMovieResponse {
        return responseCall { movieApiService.getSimilarMovies(movieId) }
    }

    override suspend fun getMovieGallery(movieId: Long): RemoteGalleryResponse {
        return responseCall { movieApiService.getMovieGallery(movieId) }
    }

    override suspend fun getProductionCompany(movieId: Long): ProductionCompanyResponse {
        return responseCall { movieApiService.getProductionCompany(movieId) }
    }

    override suspend fun getMovieDetailsById(movieId: Long): RemoteMovieItemDto {
        return responseCall { movieApiService.getMovieDetailsById(movieId) }
    }

    override suspend fun getMoviePosters(movieId: Long): RemoteGalleryResponse {
        return responseCall { movieApiService.getMoviePosters(movieId) }
    }

    override suspend fun getPopularMovies(): RemoteMovieResponse {
        return responseCall { movieApiService.getPopularMovies() }
    }

    override suspend fun getUpcomingMovies(): RemoteMovieResponse {
        return responseCall { movieApiService.getUpcomingMovies() }
    }
    override suspend fun getTopRatedMovies(): RemoteMovieResponse {
        return responseCall { movieApiService.getTopRatedMovies() }
    }
    override suspend fun getMoviesByGenreIds(genresIds: List<Long>): RemoteMovieResponse {
        return responseCall { movieApiService.getMoviesByGenreIds(genresIds) }
    }
}
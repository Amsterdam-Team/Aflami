package com.example.remotedatasource.datasource

import com.example.remotedatasource.serviceProvider.MovieServiceProvider
import com.example.remotedatasource.utils.apiHandler.responseCall
import com.example.repository.datasource.remote.MovieRemoteSource
import com.example.repository.dto.remote.ProductionCompanyResponse
import com.example.repository.dto.remote.RemoteActorSearchResponse
import com.example.repository.dto.remote.RemoteCastAndCrewResponse
import com.example.repository.dto.remote.RemoteMovieItemDto
import com.example.repository.dto.remote.RemoteMovieResponse
import com.example.repository.dto.remote.movieGallery.RemoteMovieGalleryResponse
import com.example.repository.dto.remote.review.ReviewsResponse

class MovieRemoteDataSourceImpl(
    private val movieServiceProvider: MovieServiceProvider
) : MovieRemoteSource {

    override suspend fun getMoviesByKeyword(keyword: String, page: Int): RemoteMovieResponse {
        return movieServiceProvider.getMoviesByKeyword(keyword, page)
    }

    override suspend fun getMoviesByActorName(name: String, page: Int): RemoteMovieResponse {
        val actorsByName = getActorIdByName(name, page)
            .actors
            .joinToString(separator = "|") { it.id.toString() }

        return movieServiceProvider.getMoviesByActorId(actorsByName)
    }

    private suspend fun getActorIdByName(name: String, page: Int): RemoteActorSearchResponse {
        return movieServiceProvider.getActorIdByName(name, page)
    }

    override suspend fun getMoviesByCountryIsoCode(
        countryIsoCode: String,
        page: Int
    ): RemoteMovieResponse {
        return movieServiceProvider.getMoviesByCountryIsoCode(countryIsoCode, page)
    }

    override suspend fun getCastByMovieId(movieId: Long): RemoteCastAndCrewResponse {
        return movieServiceProvider.getCastByMovieId(movieId)
    }

    override suspend fun getMovieReviews(movieId: Long): ReviewsResponse {
        return movieServiceProvider.getMovieReviews(movieId)
    }

    override suspend fun getSimilarMovies(movieId: Long): RemoteMovieResponse {
        return movieServiceProvider.getSimilarMovies(movieId)
    }

    override suspend fun getMovieGallery(movieId: Long): RemoteMovieGalleryResponse {
        return movieServiceProvider.getMovieGallery(movieId)
    }

    override suspend fun getProductionCompany(movieId: Long): ProductionCompanyResponse {
        return movieServiceProvider.getProductionCompany(movieId)
    }

    override suspend fun getMovieDetailsById(movieId: Long): RemoteMovieItemDto {
        return movieServiceProvider.getMovieDetailsById(movieId)
    }

    override suspend fun getMoviePosters(movieId: Long): RemoteMovieGalleryResponse {
        return movieServiceProvider.getMoviePosters(movieId)
    }

    override suspend fun getPopularMovies(): RemoteMovieResponse {
        return movieServiceProvider.getPopularMovies()
    }

    override suspend fun getUpcomingMovies(): RemoteMovieResponse {
        return movieServiceProvider.getUpcomingMovies()
    }
}
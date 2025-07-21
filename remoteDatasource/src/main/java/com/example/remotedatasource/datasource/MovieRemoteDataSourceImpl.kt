package com.example.remotedatasource.datasource

import com.example.remotedatasource.api.MovieApiService
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
    private val movieApiService: MovieApiService
) : MovieRemoteSource {

    override suspend fun getMoviesByKeyword(keyword: String, page: Int): RemoteMovieResponse {
        return responseCall {
            movieApiService.getMoviesByKeyword(keyword, page)
        }
    }

    override suspend fun getMoviesByActorName(name: String, page: Int): RemoteMovieResponse {
        val actorsByName = getActorIdByName(name, page)
            .actors
            .joinToString(separator = "|") { it.id.toString() }

        return responseCall {
            movieApiService.getMoviesByActorId(actorsByName)
        }
    }

    private suspend fun getActorIdByName(name: String, page: Int): RemoteActorSearchResponse {
        return responseCall {
            movieApiService.getActorIdByName(name, page)
        }
    }

    override suspend fun getMoviesByCountryIsoCode(
        countryIsoCode: String,
        page: Int
    ): RemoteMovieResponse {
        return responseCall {
            movieApiService.getMoviesByCountryIsoCode(countryIsoCode, page)
        }
    }

    override suspend fun getCastByMovieId(movieId: Long): RemoteCastAndCrewResponse {
        return responseCall {
            movieApiService.getCastByMovieId(movieId)
        }
    }

    override suspend fun getMovieReviews(movieId: Long): ReviewsResponse {
        return responseCall {
            movieApiService.getMovieReviews(movieId)
        }
    }

    override suspend fun getSimilarMovies(movieId: Long): RemoteMovieResponse {
        return responseCall {
            movieApiService.getSimilarMovies(movieId)
        }
    }

    override suspend fun getMovieGallery(movieId: Long): RemoteMovieGalleryResponse {
        return responseCall {
            movieApiService.getMovieGallery(movieId)
        }
    }

    override suspend fun getProductionCompany(movieId: Long): ProductionCompanyResponse {
        return responseCall {
            movieApiService.getProductionCompany(movieId)
        }
    }

    override suspend fun getMovieDetailsById(movieId: Long): RemoteMovieItemDto {
        return responseCall {
            movieApiService.getMovieDetailsById(movieId)
        }
    }

    override suspend fun getMoviePosters(movieId: Long): RemoteMovieGalleryResponse {
        return responseCall {
            movieApiService.getMoviePosters(movieId)
        }
    }

    override suspend fun getPopularMovies(): RemoteMovieResponse {
        return responseCall {
            movieApiService.getPopularMovies()
        }
    }
}
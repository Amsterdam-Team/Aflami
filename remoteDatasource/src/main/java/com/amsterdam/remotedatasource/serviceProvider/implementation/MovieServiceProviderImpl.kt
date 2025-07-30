package com.amsterdam.remotedatasource.serviceProvider.implementation
import com.amsterdam.remotedatasource.api.MovieApiService
import com.amsterdam.remotedatasource.serviceProvider.MovieServiceProvider
import com.amsterdam.remotedatasource.utils.apiHandler.responseCall
import com.amsterdam.repository.dto.remote.RemoteActorSearchResponse
import com.amsterdam.repository.dto.remote.RemoteCastAndCrewResponse
import com.amsterdam.repository.dto.remote.RemoteMovieDetailsResponse
import com.amsterdam.repository.dto.remote.RemoteMovieResponse
import com.amsterdam.repository.dto.remote.movieGallery.RemoteGalleryResponse
import com.amsterdam.repository.dto.remote.review.ReviewsResponse
import javax.inject.Inject

class MovieServiceProviderImpl @Inject constructor(
    private val movieApiService: MovieApiService
) : MovieServiceProvider {

    override suspend fun getPopularMovies(): RemoteMovieResponse {
        return responseCall { movieApiService.getPopularMovies() }
    }

    override suspend fun getUpcomingMovies(): RemoteMovieResponse {
        return responseCall { movieApiService.getUpcomingMovies() }
    }

    override suspend fun getMoviesByKeyword(keyword: String, page: Int): RemoteMovieResponse {
        return responseCall { movieApiService.getMoviesByKeyword(keyword, page) }
    }

    override suspend fun getActorIdByName(name: String, page: Int): RemoteActorSearchResponse {
        return responseCall { movieApiService.getActorIdByName(name, page) }
    }

    override suspend fun getMoviesByActorId(actorIds: String): RemoteMovieResponse {
        return responseCall { movieApiService.getMoviesByActorId(actorIds) }
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

    override suspend fun getMovieDetailsById(movieId: Long): RemoteMovieDetailsResponse {
        return responseCall { movieApiService.getMovieDetailsById(movieId) }
    }

    override suspend fun getTopRatedMovies() : RemoteMovieResponse {
        return responseCall { movieApiService.getTopRatedMovies() }
    }

    override suspend fun getMoviesByGenreIds(genresIds: List<Long>): RemoteMovieResponse {
        return responseCall { movieApiService.getMoviesByGenreIds(genresIds) }
    }

}

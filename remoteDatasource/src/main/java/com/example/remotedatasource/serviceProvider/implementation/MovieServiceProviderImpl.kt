package com.example.remotedatasource.serviceProvider.implementation
import com.example.remotedatasource.api.MovieApiService
import com.example.remotedatasource.serviceProvider.MovieServiceProvider
import com.example.remotedatasource.utils.apiHandler.responseCall
import com.example.repository.dto.remote.ProductionCompanyResponse
import com.example.repository.dto.remote.RemoteActorSearchResponse
import com.example.repository.dto.remote.RemoteCastAndCrewResponse
import com.example.repository.dto.remote.RemoteMovieItemDto
import com.example.repository.dto.remote.RemoteMovieResponse
import com.example.repository.dto.remote.movieGallery.RemoteGalleryResponse
import com.example.repository.dto.remote.review.ReviewsResponse

class MovieServiceProviderImpl(
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

    override suspend fun getMovieReviews(movieId: Long): ReviewsResponse {
        return responseCall { movieApiService.getMovieReviews(movieId) }
    }

    override suspend fun getSimilarMovies(movieId: Long): RemoteMovieResponse {
        return responseCall { movieApiService.getSimilarMovies(movieId) }
    }

    override suspend fun getMovieGallery(movieId: Long): RemoteGalleryResponse {
        return responseCall { movieApiService.getMovieGallery(movieId) }
    }

    override suspend fun getMoviePosters(movieId: Long): RemoteGalleryResponse {
        return responseCall { movieApiService.getMoviePosters(movieId) }
    }

    override suspend fun getProductionCompany(movieId: Long): ProductionCompanyResponse {
        return responseCall { movieApiService.getProductionCompany(movieId) }
    }

    override suspend fun getMovieDetailsById(movieId: Long): RemoteMovieItemDto {
        return responseCall { movieApiService.getMovieDetailsById(movieId) }
    }

    override suspend fun getTopRatedMovies() : RemoteMovieResponse {
        return responseCall { movieApiService.getTopRatedMovies() }
    }

}

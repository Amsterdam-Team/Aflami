package com.amsterdam.remotedatasource.datasource

import com.amsterdam.remotedatasource.api.MovieApiService
import com.amsterdam.remotedatasource.utils.apiHandler.responseCall
import com.amsterdam.repository.datasource.remote.MovieRemoteSource
import com.amsterdam.repository.dto.remote.RemoteActorSearchResponse
import com.amsterdam.repository.dto.remote.RemoteCastAndCrewResponse
import com.amsterdam.repository.dto.remote.RemoteMovieDetailsResponse
import com.amsterdam.repository.dto.remote.RemoteMovieResponse
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

    override suspend fun getMovieDetailsById(movieId: Long): RemoteMovieDetailsResponse {
        return responseCall { movieApiService.getMovieDetailsById(movieId) }

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
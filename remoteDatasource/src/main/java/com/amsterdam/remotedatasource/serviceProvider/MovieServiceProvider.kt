package com.amsterdam.remotedatasource.serviceProvider

import com.amsterdam.repository.dto.remote.RemoteActorSearchResponse
import com.amsterdam.repository.dto.remote.RemoteCastAndCrewResponse
import com.amsterdam.repository.dto.remote.RemoteMovieDetailsResponse
import com.amsterdam.repository.dto.remote.RemoteMovieResponse

interface MovieServiceProvider {
    suspend fun getPopularMovies(): RemoteMovieResponse
    suspend fun getUpcomingMovies(): RemoteMovieResponse
    suspend fun getMoviesByKeyword(keyword: String, page: Int): RemoteMovieResponse
    suspend fun getActorIdByName(name: String, page: Int): RemoteActorSearchResponse
    suspend fun getMoviesByActorId(actorIds: String): RemoteMovieResponse
    suspend fun getMoviesByCountryIsoCode(countryIsoCode: String, page: Int): RemoteMovieResponse
    suspend fun getCastByMovieId(movieId: Long): RemoteCastAndCrewResponse
    suspend fun getMovieDetailsById(movieId: Long): RemoteMovieDetailsResponse
    suspend fun getTopRatedMovies() : RemoteMovieResponse
    suspend fun getMoviesByGenreIds(genresIds: List<Long>): RemoteMovieResponse
}
package com.amsterdam.repository.datasource.remote

import com.amsterdam.repository.dto.remote.RatingResponse
import com.amsterdam.repository.dto.remote.RemoteCastAndCrewResponse
import com.amsterdam.repository.dto.remote.RemoteMovieDetailsResponse
import com.amsterdam.repository.dto.remote.RemoteMovieResponse

interface MovieRemoteSource {

    suspend fun getMoviesByKeyword(keyword: String, page:Int): RemoteMovieResponse

    suspend fun getMoviesByActorIds(actorIds: List<Int>, page: Int): RemoteMovieResponse

    suspend fun getActorIdsByName(name: String, page: Int): List<Int>

    suspend fun getMoviesByCountryIsoCode(countryIsoCode: String, page:Int): RemoteMovieResponse

    suspend fun getCastByMovieId(movieId: Long): RemoteCastAndCrewResponse

    suspend fun getMovieDetailsById(movieId: Long, sessionId: String = ""): RemoteMovieDetailsResponse

    suspend fun getPopularMovies(): RemoteMovieResponse

    suspend fun getUpcomingMovies(): RemoteMovieResponse

    suspend fun getTopRatedMovies(page: Int): RemoteMovieResponse

    suspend fun getRatedMovies(sessionId: String): RemoteMovieResponse

    suspend fun setMovieRate(rate: Float, movieId: Long, sessionId: String): RatingResponse?

    suspend fun deleteMovieRate(movieId: Long, sessionId: String)

    suspend fun getMoviesByGenreIds(genresIds: List<Long>, page: Int): RemoteMovieResponse
    suspend fun getMoviesByGenreId(genreId: Long, page: Int): RemoteMovieResponse
}
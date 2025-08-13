package com.amsterdam.repository.datasource.remote

import com.amsterdam.repository.dto.remote.RatingResponse
import com.amsterdam.repository.dto.remote.RemoteCastAndCrewResponse
import com.amsterdam.repository.dto.remote.RemoteMovieDetailsResponse
import com.amsterdam.repository.dto.remote.RemoteMovieItemDto
import com.amsterdam.repository.dto.remote.RemoteMovieResponse

interface MovieRemoteSource {

    suspend fun getMoviesByKeyword(keyword: String, page:Int): RemoteMovieResponse

    suspend fun getMoviesByActorIds(actorIds: List<Int>, page: Int): RemoteMovieResponse

    suspend fun getActorIdsByName(name: String, page: Int): List<Int>

    suspend fun getMoviesByCountryIsoCode(countryIsoCode: String, page:Int): RemoteMovieResponse

    suspend fun getCastByMovieId(movieId: Long): RemoteCastAndCrewResponse

    suspend fun getMovieDetailsById(movieId: Long): RemoteMovieDetailsResponse

    suspend fun getPopularMovies(page: Int = 1): RemoteMovieResponse

    suspend fun getUpcomingMovies(): RemoteMovieResponse

    suspend fun getTopRatedMovies(page: Int): RemoteMovieResponse

    suspend fun getRatedMovies(): RemoteMovieResponse

    suspend fun setMovieRate(rate: Float, movieId: Long): RatingResponse?

    suspend fun deleteMovieRate(movieId: Long)

    suspend fun getMoviesByGenreIds(genresIds: List<Long>, page: Int): RemoteMovieResponse

    suspend fun getRandomMoviesWithNotNullDate(requiredMoviesNumber: Int): List<RemoteMovieItemDto>
    suspend fun getMoviesByGenreId(genreId: Long, page: Int): RemoteMovieResponse

}
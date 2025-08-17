package com.amsterdam.repository.datasource.remote

import com.amsterdam.repository.dto.remote.CastAndCrewRemoteResponse
import com.amsterdam.repository.dto.remote.MovieDetailsRemoteResponse
import com.amsterdam.repository.dto.remote.MovieItemRemoteDto
import com.amsterdam.repository.dto.remote.MovieRemoteResponse
import com.amsterdam.repository.dto.remote.RatingRemoteResponse

interface MovieRemoteDataSource {
    suspend fun getMoviesByKeyword(keyword: String, page: Int): MovieRemoteResponse

    suspend fun getMoviesByActorIds(actorIds: List<Int>, page: Int): MovieRemoteResponse

    suspend fun getActorIdsByName(name: String, page: Int): List<Int>

    suspend fun getMoviesByCountryIsoCode(countryIsoCode: String, page: Int): MovieRemoteResponse

    suspend fun getCastByMovieId(movieId: Long): CastAndCrewRemoteResponse

    suspend fun getMovieDetailsById(movieId: Long): MovieDetailsRemoteResponse

    suspend fun getPopularMovies(page: Int = 1): MovieRemoteResponse

    suspend fun getUpcomingMovies(): MovieRemoteResponse

    suspend fun getTopRatedMovies(page: Int): MovieRemoteResponse

    suspend fun getRatedMovies(): MovieRemoteResponse

    suspend fun setMovieRate(rate: Float, movieId: Long): RatingRemoteResponse?

    suspend fun deleteMovieRate(movieId: Long)

    suspend fun getMoviesByGenreIds(genresIds: List<Long>, page: Int): MovieRemoteResponse

    suspend fun getRandomMoviesWithReleaseDate(requiredMoviesNumber: Int): List<MovieItemRemoteDto>

    suspend fun getRandomMoviesWithPoster(requiredMoviesNumber: Int): List<MovieItemRemoteDto>

    suspend fun getMoviesByGenreId(genreId: Long, page: Int): MovieRemoteResponse
}
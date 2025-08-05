package com.amsterdam.domain.useCase.myRating.movie

import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.entity.Movie

class GetUserRatedMoviesUseCase(private val movieRepository: MovieRepository) {

    suspend fun getRatedMovie(movieId: Long): UserRatedMovie? {
        return getRatedMovies().find { it.movie.id == movieId }
    }

    suspend fun getRatedMovies(): List<UserRatedMovie> {
        return movieRepository.getUserRatedMovies()
    }

    data class UserRatedMovie(
        val movie: Movie,
        val userRate: Int
    )
}
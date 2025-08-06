package com.amsterdam.domain.useCase.myRating.movie

import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.entity.Movie

class GetUserRatedMoviesUseCase(private val movieRepository: MovieRepository) {

    suspend fun getRatedMovies(): List<UserRatedMovie> {
        return movieRepository.getUserRatedMovies().sortedByDescending { it.userRate }
    }

    data class UserRatedMovie(
        val movie: Movie,
        val userRate: Int
    )
}
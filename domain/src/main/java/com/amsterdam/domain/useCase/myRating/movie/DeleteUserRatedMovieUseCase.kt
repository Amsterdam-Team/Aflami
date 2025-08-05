package com.amsterdam.domain.useCase.myRating.movie

import com.amsterdam.domain.repository.MovieRepository

class DeleteUserRatedMovieUseCase(
    private val movieRepository: MovieRepository,
) {
    suspend fun deleteMovieRate(movieId: Long) {
        movieRepository.deleteMovieRate(movieId)
    }
}
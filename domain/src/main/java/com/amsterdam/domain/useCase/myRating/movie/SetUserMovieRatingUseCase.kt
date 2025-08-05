package com.amsterdam.domain.useCase.myRating.movie

import com.amsterdam.domain.repository.MovieRepository

class SetUserMovieRatingUseCase(private val movieRepository: MovieRepository, ) {
    suspend fun setUserMovieRate(rate: Int, movieId: Long){
        movieRepository.setMovieRate(rate = rate, movieId = movieId)
    }
}
package com.amsterdam.domain.useCase.home

import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.entity.Movie

class GetTopRatedMoviesUseCase(private val movieRepository: MovieRepository) {

    suspend operator fun invoke(): List<Movie> {
        return movieRepository.getTopRatedMovies()
    }

}
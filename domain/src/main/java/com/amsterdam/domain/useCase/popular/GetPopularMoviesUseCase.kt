package com.amsterdam.domain.useCase.popular

import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.entity.Movie

class GetPopularMoviesUseCase (private val movieRepository: MovieRepository) {

    suspend operator fun invoke(): List<Movie> {
        return movieRepository.getPopularMovies()
    }
}
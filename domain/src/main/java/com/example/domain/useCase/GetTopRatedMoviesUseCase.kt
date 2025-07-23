package com.example.domain.useCase

import com.example.domain.repository.MovieRepository
import com.example.entity.Movie

class GetTopRatedMoviesUseCase (private val movieRepository: MovieRepository) {

    suspend operator fun invoke(): List<Movie> {
        return movieRepository.getTopRatedMovies()
    }

}
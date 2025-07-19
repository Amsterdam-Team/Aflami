package com.example.domain.useCase

import com.example.domain.repository.MovieRepository
import com.example.entity.category.MovieGenre

class IncrementMovieGenreInterestUseCase(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(genre: MovieGenre) {
        movieRepository.incrementGenreInterest(genre)
    }
}

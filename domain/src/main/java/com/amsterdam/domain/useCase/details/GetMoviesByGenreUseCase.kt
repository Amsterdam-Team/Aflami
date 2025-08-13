package com.amsterdam.domain.useCase.details

import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.entity.Movie
import com.amsterdam.entity.category.MovieGenre

class GetMoviesByGenreUseCase(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(selectedGenre: MovieGenre, page: Int): List<Movie> {
        return movieRepository.getMoviesByGenre(selectedGenre, page)
    }
}

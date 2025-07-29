package com.amsterdam.domain.useCase.home

import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.entity.Movie
import com.amsterdam.entity.category.MovieGenre

class GetTopRatedMoviesUseCase(private val movieRepository: MovieRepository) {

    suspend operator fun invoke(
        page: Int = 1,
    ): List<Movie> {
        return movieRepository.getTopRatedMovies(
            page = page,
        )
    }

}
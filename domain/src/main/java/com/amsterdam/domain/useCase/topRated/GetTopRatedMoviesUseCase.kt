package com.amsterdam.domain.useCase.topRated

import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.entity.Movie

class GetTopRatedMoviesUseCase(private val movieRepository: MovieRepository) {

    suspend operator fun invoke(
        page: Int = 1,
    ): List<Movie> {
        return movieRepository.getTopRatedMovies(
            page = page,
        )
    }

}
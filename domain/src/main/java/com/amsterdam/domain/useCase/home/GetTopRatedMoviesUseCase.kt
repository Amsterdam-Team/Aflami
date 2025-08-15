package com.amsterdam.domain.useCase.home

import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.entity.Movie

class GetTopRatedMoviesUseCase(private val movieRepository: MovieRepository) {

    suspend operator fun invoke(
        page: Int = 1,
        isCached: Boolean = true
    ): List<Movie> {
        return if (isCached) movieRepository.getCachedTopRatedMovies(page = page)
        else movieRepository.getRemoteTopRatedMovies(page = page)
    }
}
package com.amsterdam.domain.useCase

import com.amsterdam.domain.exceptions.NoMoviesByKeywordFoundException
import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.entity.Movie

class GetMoviesByKeywordUseCase(
    private val movieRepository: MovieRepository
) {

    suspend operator fun invoke(
        keyword: String,
        rating: Float = 0f,
        categoryName: String = ""
    ): List<Movie> {

        return movieRepository.getMoviesByKeyword(keyword)
            .sortedByDescending { it.popularity }
            .ifEmpty {
                throw NoMoviesByKeywordFoundException()
            }
    }
}


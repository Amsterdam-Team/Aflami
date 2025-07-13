package com.example.domain.useCase

import com.example.domain.exceptions.NoMoviesByKeywordFoundException
import com.example.domain.exceptions.QueryTooShortException
import com.example.domain.repository.MovieRepository
import com.example.entity.Movie

class GetMoviesByKeywordUseCase(
    private val movieRepository: MovieRepository
) {

    suspend operator fun invoke(
        keyword: String,
        rating: Float = 0f,
        categoryName: String = ""
    ): List<Movie> {
        if (keyword.length < 3)
            throw QueryTooShortException()

        return movieRepository.getMoviesByKeyword(keyword)
            .sortedByDescending { it.popularity }
            .ifEmpty {
                throw NoMoviesByKeywordFoundException()
            }
    }
}


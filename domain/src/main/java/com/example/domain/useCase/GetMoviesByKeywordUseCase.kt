package com.example.domain.useCase

import com.example.domain.exceptions.NoSearchByKeywordResultFoundException
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

        return movieRepository.getMoviesByKeyword(keyword)
            .sortedByDescending { it.popularity }
            .ifEmpty {
                throw NoSearchByKeywordResultFoundException()
            }
    }
}


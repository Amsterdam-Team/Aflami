package com.example.domain.useCase

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
        return movieRepository.getMoviesByKeyword(keyword = keyword, rating = rating)
            //.filter { it.categories.any { category -> category.name == categoryName } }
            .sortedByDescending { it.popularity }

    }
}


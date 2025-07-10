package com.example.domain

import com.example.domain.repository.MovieRepository
import com.example.entity.Movie

class GetMoviesByKeywordUseCase(
    private val movieRepository: MovieRepository
) {

    suspend operator fun invoke(
        keyword: String,
        rating: String,
        categoryName: String
    ): List<Movie> {
       return movieRepository.getMoviesByKeyword(keyword)
           .filter { it.rating.toString() == rating }
           .filter { it.categories.any { category -> category.name == categoryName } }
    }
}


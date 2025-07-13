package com.example.domain.useCase

import com.example.domain.repository.MovieRepository
import com.example.entity.Movie

class GetMoviesByKeywordUseCase(
    private val movieRepository: MovieRepository
) {

    suspend operator fun invoke(
        keyword: String,
        rating: Float = 0f,
        genreType: GenreType = GenreType.ALL
    ): List<Movie> {
        return movieRepository.getMoviesByKeyword(keyword = keyword, rating = rating, genreType = genreType)
        if (keyword.length < 3)
            throw QueryTooShortException()

        return movieRepository.getMoviesByKeyword(keyword)
            .sortedByDescending { it.popularity }

    }
}


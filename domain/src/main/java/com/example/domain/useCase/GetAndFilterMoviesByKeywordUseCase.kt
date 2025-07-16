package com.example.domain.useCase

import com.example.domain.common.ContentFilteringExtensions.filterByCategory
import com.example.domain.common.ContentFilteringExtensions.filterByMinRating
import com.example.domain.common.ContentFilteringExtensions.sortByPopularityDescending
import com.example.domain.common.ContentFilteringExtensions.throwIfEmpty
import com.example.domain.exceptions.NoSearchByKeywordResultFoundException
import com.example.domain.repository.MovieRepository
import com.example.entity.Movie

class GetAndFilterMoviesByKeywordUseCase(
    private val movieRepository: MovieRepository
) {

    suspend operator fun invoke(
        keyword: String,
        rating: Int = 0,
        movieGenreId: Int = 0
    ): List<Movie> {
        return movieRepository
            .getMoviesByKeyword(keyword = keyword)
            .filterByMinRating(rating)
            .filterByCategory(movieGenreId)
            .sortByPopularityDescending()
            .throwIfEmpty { NoSearchByKeywordResultFoundException() }
    }
}
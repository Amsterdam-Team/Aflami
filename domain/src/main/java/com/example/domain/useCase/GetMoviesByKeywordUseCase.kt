package com.example.domain.useCase

import com.example.domain.common.ContentFilteringExtensions.filterByMinRating
import com.example.domain.common.ContentFilteringExtensions.sortByPopularityDescending
import com.example.domain.common.ContentFilteringExtensions.throwIfEmpty
import com.example.domain.exceptions.NoSearchByKeywordResultFoundException
import com.example.domain.repository.MovieRepository
import com.example.entity.Movie
import com.example.entity.category.MovieGenre

class GetMoviesByKeywordUseCase(
    private val movieRepository: MovieRepository
) {

    suspend operator fun invoke(
        keyword: String,
        rating: Int = 0,
        movieGenre: MovieGenre = MovieGenre.ALL
    ): List<Movie> {
        return movieRepository
            .getMoviesByKeyword(keyword = keyword)
            .filterByMinRating(rating)
            .filter { movie ->
                if (movieGenre == MovieGenre.ALL)
                    return@filter true

                movie.categories.any { it == movieGenre }
            }
            .sortByPopularityDescending()
            .throwIfEmpty { NoSearchByKeywordResultFoundException() }
    }
}
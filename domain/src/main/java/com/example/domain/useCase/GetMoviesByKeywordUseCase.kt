package com.example.domain.useCase

import com.example.domain.exceptions.NoSearchByKeywordResultFoundException
import com.example.domain.repository.MovieRepository
import com.example.entity.Movie
import kotlin.math.roundToInt

class GetMoviesByKeywordUseCase(
    private val movieRepository: MovieRepository
) {

    suspend operator fun invoke(
        keyword: String,
        rating: Int = 0,
        movieGenreId: Int = 0
    ): List<Movie> {
        return movieRepository
            .getMoviesByKeyword(keyword = keyword)
            .filter { movie -> movie.rating.roundToInt() >= rating }
            .filter { movie ->
                if (movieGenreId == 0)
                    return@filter true

                movie.categories.any { it.id == movieGenreId.toLong() }
            }
            .sortedByDescending {
                it.popularity
            }
            .ifEmpty { throw NoSearchByKeywordResultFoundException() }

    }
}


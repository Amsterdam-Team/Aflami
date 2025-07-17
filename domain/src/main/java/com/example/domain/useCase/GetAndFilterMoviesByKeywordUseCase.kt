package com.example.domain.useCase

import com.example.domain.repository.MovieRepository
import com.example.entity.Movie
import com.example.entity.category.MovieGenre
import kotlin.math.roundToInt

class GetAndFilterMoviesByKeywordUseCase(
    private val movieRepository: MovieRepository
) {

    suspend operator fun invoke(
        keyword: String,
        rating: Int = 0,
        movieGenre: MovieGenre = MovieGenre.ALL
    ): List<Movie> {
        return movieRepository
            .getMoviesByKeyword(keyword = keyword)
            .filter { item -> item.rating.roundToInt() >= rating }
            .filter { movie ->
                if (movieGenre == MovieGenre.ALL) return@filter true
                movie.categories.any { it == movieGenre }
            }
    }
}
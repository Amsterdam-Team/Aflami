package com.example.domain.useCase

import com.example.domain.repository.MovieRepository
import com.example.entity.Movie
import com.example.entity.category.MovieGenre
import kotlin.math.floor

class GetAndFilterMoviesByKeywordUseCase(
    private val movieRepository: MovieRepository
) {

    suspend operator fun invoke(
        keyword: String,
        rating: Int = 0,
        movieGenre: MovieGenre = MovieGenre.ALL
    ): List<Movie> {
        val userInterest = movieRepository.getAllGenreInterests()

        return movieRepository
            .getMoviesByKeyword(keyword = keyword)
            .filterMoviesWithRatingAndGenre(rating, genre = movieGenre)
            .sortedByDescending { movie ->
                movie.categories.maxOfOrNull { userInterest[it] ?: 0 }
            }
    }


    private fun List<Movie>.filterMoviesWithRatingAndGenre(
        rating: Int,
        genre: MovieGenre
    ): List<Movie> {
        return this.filter { item -> floor(item.rating) >= rating }
            .filter { movie ->
                if (genre == MovieGenre.ALL) return@filter true
                movie.categories.any { it == genre }
            }
    }
}
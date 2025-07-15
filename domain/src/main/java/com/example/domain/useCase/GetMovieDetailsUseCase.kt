package com.example.domain.useCase

import com.example.domain.repository.CategoryRepository
import com.example.domain.repository.MovieRepository
import com.example.entity.Category
import com.example.entity.Movie
import com.example.entity.Review

class GetMovieDetailsUseCase(
    private val movieRepository: MovieRepository,
    private val categoryRepository: CategoryRepository,
) {
    suspend operator fun invoke(movieId: Long): MovieDetails {
        val movie = movieRepository.getMovieById(movieId)
        val reviews = movieRepository.getMovieReviews(movieId)
        val categoriesForMovie = categoryRepository.getMovieCategories(movieId)

    }

    data class MovieDetails(
        val movie: Movie ,
        val review: Review,
        val categories: List<Category>,

        )
}
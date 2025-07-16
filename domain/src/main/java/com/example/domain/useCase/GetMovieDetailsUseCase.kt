package com.example.domain.useCase

import com.example.domain.repository.CategoryRepository
import com.example.domain.repository.MovieRepository
import com.example.entity.Actor
import com.example.entity.Category
import com.example.entity.Movie
import com.example.entity.ProductionCompany
import com.example.entity.Review

class GetMovieDetailsUseCase(
    private val movieRepository: MovieRepository,
    private val categoryRepository: CategoryRepository,
) {
    suspend operator fun invoke(movieId: Long): MovieDetails {
        val movie = movieRepository.getMovieDetailsById(movieId)
        val reviews = movieRepository.getMovieReviews(movieId)
        val categoriesForMovie = categoryRepository.getMovieCategories(movieId)
        val actors = movieRepository.getActorsByMovieId(movieId)
        val similarMovies = movieRepository.getSimilarMovies(movieId)
        val movieGallery = movieRepository.getMovieGallery(movieId)
        val productionsCompanies = movieRepository.getProductionCompany(movieId)
       println(categoriesForMovie)
        return MovieDetails(
            movie = movie,
            reviews = reviews,
            categories = categoriesForMovie,
            actors = actors,
            similarMovies = similarMovies,
            movieGallery = movieGallery,
            productionsCompanies = productionsCompanies
        )
    }

    data class MovieDetails(
        val movie: Movie,
        val reviews: List<Review>,
        val categories: List<Category>,
        val actors: List<Actor>,
        val similarMovies: List<Movie>,
        val movieGallery: List<String>,
        val productionsCompanies : List<ProductionCompany>
    )
}
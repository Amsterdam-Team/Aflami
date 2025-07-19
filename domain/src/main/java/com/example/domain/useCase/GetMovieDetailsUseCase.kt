package com.example.domain.useCase

import com.example.domain.repository.MovieRepository
import com.example.entity.Actor
import com.example.entity.Movie
import com.example.entity.ProductionCompany
import com.example.entity.Review
import com.example.entity.category.MovieGenre

class GetMovieDetailsUseCase(
    private val movieRepository: MovieRepository,
) {
    suspend operator fun invoke(movieId: Long): MovieDetails {
        val movie = movieRepository.getMovieDetailsById(movieId)
        val reviews = movieRepository.getMovieReviews(movieId)
        val actors = movieRepository.getActorsByMovieId(movieId)
        val similarMovies = movieRepository.getSimilarMovies(movieId)
        val movieGallery = movieRepository.getMovieGallery(movieId)
        val moviePosters = movieRepository.getMoviePosters(movieId).take(10)
        val productionsCompanies = movieRepository.getProductionCompany(movieId)
        return MovieDetails(
            movie = movie,
            reviews = reviews,
            categories = movie.categories,
            actors = actors,
            similarMovies = similarMovies,
            movieGallery = movieGallery,
            moviePosters = moviePosters,
            productionsCompanies = productionsCompanies
        )
    }

    data class MovieDetails(
        val movie: Movie,
        val reviews: List<Review>,
        val categories: List<MovieGenre>,
        val actors: List<Actor>,
        val similarMovies: List<Movie>,
        val movieGallery: List<String>,
        val moviePosters : List<String>,
        val productionsCompanies : List<ProductionCompany>
    )
}
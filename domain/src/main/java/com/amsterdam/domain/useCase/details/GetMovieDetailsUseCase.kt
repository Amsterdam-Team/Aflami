package com.amsterdam.domain.useCase.details

import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.domain.useCase.common.AddMovieWatchHistoryUseCase
import com.amsterdam.entity.Actor
import com.amsterdam.entity.Movie
import com.amsterdam.entity.ProductionCompany
import com.amsterdam.entity.Review
import com.amsterdam.entity.category.MovieGenre

class GetMovieDetailsUseCase(
    private val movieRepository: MovieRepository,
    private val addWatchHistoryUseCase: AddMovieWatchHistoryUseCase,
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
        ).also {
            addWatchHistoryUseCase(movieId)
        }
    }

    data class MovieDetails(
        val movie: Movie,
        val reviews: List<Review>,
        val categories: List<MovieGenre>,
        val actors: List<Actor>,
        val similarMovies: List<Movie>,
        val movieGallery: List<String>,
        val moviePosters: List<String>,
        val productionsCompanies: List<ProductionCompany>
    )
}
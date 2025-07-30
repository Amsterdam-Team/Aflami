package com.amsterdam.domain.useCase.details

import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.domain.useCase.common.AddMovieWatchHistoryUseCase
import com.amsterdam.entity.Actor
import com.amsterdam.entity.Movie
import com.amsterdam.entity.Review

class GetMovieDetailsUseCase (
    private val movieRepository: MovieRepository,
    private val addWatchHistoryUseCase: AddMovieWatchHistoryUseCase,
) {
    suspend operator fun invoke(movieId: Long): MovieDetails {
        return movieRepository.getMovieDetailsById(movieId).also { addWatchHistoryUseCase(movieId) }
    }

    data class MovieDetails(
        val movie: Movie,
        val reviews: List<Review>,
        val actors: List<Actor>,
        val similarMovies: List<Movie>,
        val movieGallery: List<String>,
        val moviePosters: List<String>,
    )
}
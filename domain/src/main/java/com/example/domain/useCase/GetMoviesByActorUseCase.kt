package com.example.domain.useCase

import com.example.domain.repository.MovieRepository
import com.example.entity.Movie

class GetMoviesByActorUseCase(private val movieRepository: MovieRepository) {
    suspend operator fun invoke(
        actorName: String,
        page: Int = 1,
        moviesPerPage: Int = 20
    ): List<Movie> {
        return movieRepository.getMoviesByActor(actorName, page, moviesPerPage)
    }
}
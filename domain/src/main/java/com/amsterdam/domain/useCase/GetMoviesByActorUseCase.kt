package com.amsterdam.domain.useCase

import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.entity.Movie

class GetMoviesByActorUseCase(
    private val movieRepository: MovieRepository,
) {
    suspend operator fun invoke(actorName: String): List<Movie> {
        return movieRepository.getMoviesByActor(actorName)
    }
}

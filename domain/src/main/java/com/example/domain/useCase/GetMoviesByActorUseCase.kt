package com.example.domain.useCase

import com.example.domain.common.ContentFilteringExtensions.sortByPopularityDescending
import com.example.domain.repository.MovieRepository
import com.example.entity.Movie

class GetMoviesByActorUseCase(
    private val movieRepository: MovieRepository,
) {
    suspend operator fun invoke(actorName: String): List<Movie> {
        return movieRepository.getMoviesByActor(actorName)
            .sortByPopularityDescending()
    }
}
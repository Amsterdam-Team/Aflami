package com.example.domain.useCase

import com.example.domain.repository.MovieRepository
import com.example.entity.Actor

class GetMovieCastUseCase(private val movieRepository: MovieRepository) {

    suspend operator fun invoke(movieId: Long): List<Actor>{
        return movieRepository.getActorsByMovieId(movieId)
            .sortedByDescending { it.popularity }

    }
}
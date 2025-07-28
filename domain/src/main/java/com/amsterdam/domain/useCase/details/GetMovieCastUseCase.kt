package com.amsterdam.domain.useCase.details

import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.entity.Actor

class GetMovieCastUseCase (private val movieRepository: MovieRepository) {

    suspend operator fun invoke(movieId: Long): List<Actor>{
        return movieRepository.getActorsByMovieId(movieId)
            .sortedByDescending { it.popularity }

    }
}
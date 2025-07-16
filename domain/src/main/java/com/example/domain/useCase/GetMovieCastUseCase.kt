package com.example.domain.useCase

import com.example.domain.exceptions.NoCastFoundException
import com.example.domain.repository.MovieRepository
import com.example.entity.Actor

class GetMovieCastUseCase(private val movieRepository: MovieRepository) {

    suspend operator fun invoke(movieId: Long?): List<Actor>{
        if (movieId == null) throw NoCastFoundException()

        return movieRepository.getCastByMovieId(movieId)
            .sortedByDescending { it.popularity }
            .ifEmpty { throw NoCastFoundException() }
    }
}
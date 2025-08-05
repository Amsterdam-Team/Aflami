package com.amsterdam.domain.useCase.home

import com.amsterdam.domain.models.Mood
import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.entity.Movie

class GetMoviesByMoodUseCase(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(mood: Mood): List<Movie> {
        val randomPage = (1..MAX_PAGES).random()
        return movieRepository.getMoviesByGenres(mood.movieGenres, randomPage)
    }

    companion object {
        const val MAX_PAGES = 100
    }
}
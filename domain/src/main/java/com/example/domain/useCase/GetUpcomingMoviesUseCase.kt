package com.example.domain.useCase

import com.example.domain.repository.MovieRepository
import com.example.entity.Movie
import com.example.entity.category.MovieGenre

class GetUpcomingMoviesUseCase(private val movieRepository: MovieRepository) {
    suspend operator fun invoke(genre: MovieGenre): List<Movie> {
        return movieRepository.getUpcomingMovies()
            .let { movies ->
                if (genre == MovieGenre.ALL) movies
                else movies.filter { it.categories.contains(genre) }
            }
    }
}
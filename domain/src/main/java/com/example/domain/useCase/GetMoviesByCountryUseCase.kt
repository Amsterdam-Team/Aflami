package com.example.domain.useCase

import com.example.domain.repository.MovieRepository
import com.example.entity.Country
import com.example.entity.Movie

class GetMoviesByCountryUseCase(private val movieRepository: MovieRepository) {
    suspend operator fun invoke(
        country: Country,
        page: Int = 1,
        moviesPerPage: Int = 20
    ): List<Movie> {
        return movieRepository.getMoviesByCountry(country, page, moviesPerPage)
    }
}
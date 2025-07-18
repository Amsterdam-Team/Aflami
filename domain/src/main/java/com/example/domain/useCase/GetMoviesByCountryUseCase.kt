package com.example.domain.useCase

import com.example.domain.repository.MovieRepository
import com.example.entity.Country
import com.example.entity.Movie

class GetMoviesByCountryUseCase(private val movieRepository: MovieRepository) {

    suspend operator fun invoke(country: Country): List<Movie> {
        return movieRepository.getMoviesByCountry(country)
    }

}
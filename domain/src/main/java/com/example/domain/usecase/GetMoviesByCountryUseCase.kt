package com.example.domain.usecase

import com.example.domain.repository.MovieRepository
import com.example.entity.Movie

class GetMoviesByCountryUseCase(private val movieRepository: MovieRepository) {

    suspend operator fun invoke(countryIsoCode: String): List<Movie> {
        return movieRepository.getMoviesByCountryIsoCode(countryIsoCode)
    }

}
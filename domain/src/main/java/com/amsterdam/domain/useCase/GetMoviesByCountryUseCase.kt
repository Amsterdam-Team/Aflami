package com.amsterdam.domain.useCase

import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.entity.Movie

class GetMoviesByCountryUseCase(private val movieRepository: MovieRepository) {

    suspend operator fun invoke(countryIsoCode: String): List<Movie> {
        return movieRepository.getMoviesByCountryIsoCode(countryIsoCode)
    }

}
package com.amsterdam.domain.useCase.search

import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.entity.Country
import com.amsterdam.entity.Movie

class GetMoviesByCountryUseCase (private val movieRepository: MovieRepository) {
    suspend operator fun invoke(
        country: Country,
        page: Int = 1,
        moviesPerPage: Int = 20
    ): List<Movie> {
        return movieRepository.getMoviesByCountry(country, page, moviesPerPage)
    }
}
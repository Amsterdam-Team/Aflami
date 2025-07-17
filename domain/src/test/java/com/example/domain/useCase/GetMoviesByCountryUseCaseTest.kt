package com.example.domain.useCase

import com.example.domain.repository.MovieRepository
import com.example.entity.Country
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetMoviesByCountryUseCaseTest {
    private lateinit var movieRepository: MovieRepository
    private lateinit var getMoviesByCountryUseCase: GetMoviesByCountryUseCase
    private lateinit var country: Country

    @BeforeEach
    fun setUp() {
        movieRepository = mockk(relaxed = true)
        country = Country("EGYPT", "EG")
        getMoviesByCountryUseCase = GetMoviesByCountryUseCase(movieRepository)
    }

    @Test
    fun `should call getMoviesByCountryIsoCode when a country ISO code is provided`() =
        runTest {
            getMoviesByCountryUseCase(country)
            coVerify { movieRepository.getMoviesByCountry(country) }
        }
}

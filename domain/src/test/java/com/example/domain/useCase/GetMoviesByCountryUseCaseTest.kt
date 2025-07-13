package com.example.domain.useCase

import com.example.domain.repository.MovieRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetMoviesByCountryUseCaseTest {
    private lateinit var movieRepository: MovieRepository
    private lateinit var getMoviesByCountryUseCase: GetMoviesByCountryUseCase

    @BeforeEach
    fun setUp() {
        movieRepository = mockk(relaxed = true)
        getMoviesByCountryUseCase = GetMoviesByCountryUseCase(movieRepository)
    }

    @Test
    fun `should call getMoviesByCountryIsoCode from movieRepository`() =
        runBlocking {
            getMoviesByCountryUseCase("countryIsoCode")
            coVerify { movieRepository.getMoviesByCountryIsoCode("countryIsoCode") }
        }
}

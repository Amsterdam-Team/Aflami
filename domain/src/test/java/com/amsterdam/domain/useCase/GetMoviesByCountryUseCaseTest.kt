package com.amsterdam.domain.useCase

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.domain.useCase.search.GetMoviesByCountryUseCase
import com.amsterdam.domain.useCase.utils.fakeMovieList
import com.amsterdam.entity.Country
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetMoviesByCountryUseCaseTest {
    private lateinit var movieRepository: MovieRepository
    private lateinit var getMoviesByCountryUseCase: GetMoviesByCountryUseCase
    private val country = Country("EGYPT", "EG")

    @BeforeEach
    fun setUp() {
        movieRepository = mockk(relaxed = true)
        getMoviesByCountryUseCase = GetMoviesByCountryUseCase(movieRepository)
    }

    @Test
    fun `should call getMoviesByCountry with default pagination parameters`() = runTest {
        // When
        getMoviesByCountryUseCase(country)

        // Then
        coVerify(exactly = 1) {
            movieRepository.getMoviesByCountry(
                country = country,
                page = 1,
                moviesPerPage = 20
            )
        }
    }

    @Test
    fun `should call getMoviesByCountry with custom pagination parameters`() = runTest {
        // Given
        val page = 3
        val moviesPerPage = 50

        // When
        getMoviesByCountryUseCase(country, page, moviesPerPage)

        // Then
        coVerify(exactly = 1) {
            movieRepository.getMoviesByCountry(
                country = country,
                page = page,
                moviesPerPage = moviesPerPage
            )
        }
    }

    @Test
    fun `should return movies when repository returns data`() = runTest {
        // Given
        coEvery {
            movieRepository.getMoviesByCountry(
                country = country,
                page = any(),
                moviesPerPage = any()
            )
        } returns fakeMovieList

        // When
        val result = getMoviesByCountryUseCase(country)

        // Then
        assertThat(result).isEqualTo(fakeMovieList)
    }

    @Test
    fun `should return an empty list when repository returns no movies`() = runTest {
        // Given
        coEvery {
            movieRepository.getMoviesByCountry(
                country = country,
                page = any(),
                moviesPerPage = any()
            )
        } returns emptyList()

        // When
        val result = getMoviesByCountryUseCase(country)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should throw AflamiException when repository call fails`() = runTest {
        // Given
        coEvery {
            movieRepository.getMoviesByCountry(
                country = country,
                page = any(),
                moviesPerPage = any()
            )
        } throws AflamiException()

        // When & Then
        assertThrows<AflamiException> { getMoviesByCountryUseCase(country) }
    }
}
package com.amsterdam.domain.useCase.search

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.domain.useCase.utils.fakeMovieList
import com.amsterdam.entity.Country
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetMoviesByCountryUseCaseTest {
    private val movieRepository: MovieRepository = mockk(relaxed = true)
    private val getMoviesByCountryUseCase by lazy {
        GetMoviesByCountryUseCase(movieRepository)
    }

    @Test
    fun `should call getMoviesByCountry with default pagination parameters`() = runTest {
        getMoviesByCountryUseCase(country)

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
        val page = 3
        val moviesPerPage = 50

        getMoviesByCountryUseCase(country, page, moviesPerPage)

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
        coEvery {
            movieRepository.getMoviesByCountry(
                country = country,
                page = any(),
                moviesPerPage = any()
            )
        } returns fakeMovieList

        val result = getMoviesByCountryUseCase(country)

        assertThat(result).isEqualTo(fakeMovieList)
    }

    @Test
    fun `should return an empty list when repository returns no movies`() = runTest {
        coEvery {
            movieRepository.getMoviesByCountry(
                country = country,
                page = any(),
                moviesPerPage = any()
            )
        } returns emptyList()

        val result = getMoviesByCountryUseCase(country)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should throw AflamiException when repository call fails`() = runTest {
        coEvery {
            movieRepository.getMoviesByCountry(
                country = country,
                page = any(),
                moviesPerPage = any()
            )
        } throws AflamiException()

        assertThrows<AflamiException> { getMoviesByCountryUseCase(country) }
    }

    private val country = Country("EGYPT", "EG")
}
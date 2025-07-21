package com.example.domain.useCase

import com.example.domain.exceptions.AflamiException
import com.example.domain.repository.MovieRepository
import com.example.domain.useCase.utils.fakeMovieList
import com.example.entity.Country
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

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
    fun `getMoviesByCountryUseCase should call getMoviesByCountry exactly one time when called`() = runTest {
        getMoviesByCountryUseCase(
            country,
            page = 1
        )
        coVerify(exactly = 1) { movieRepository.getMoviesByCountry(
            any(),
            page = 1
        ) }
    }

    @Test
    fun `getMoviesByCountryUseCase should return movies when data is available`() = runTest {
        coEvery { movieRepository.getMoviesByCountry(
            country,
            page = 1
        ) } returns fakeMovieList

        val result = getMoviesByCountryUseCase(
            country,
            page = 1
        )
        assertThat(result).isEqualTo(fakeMovieList)
    }

    @Test
    fun `getMoviesByCountryUseCase should return an empty list when repository returns no movies`() = runTest {
        coEvery { movieRepository.getMoviesByCountry(
            any(),
            page = 1
        ) } returns emptyList()

        val result = getMoviesByCountryUseCase(
            country,
            page = 1
        )
        assertThat(result).isEmpty()
    }

    @Test
    fun `getMoviesByCountryUseCase should return Aflami exception when an error happened`() = runTest {
        coEvery { movieRepository.getMoviesByCountry(
            country,
            page = 1
        ) } throws AflamiException()
        assertThrows<AflamiException> { getMoviesByCountryUseCase(
            country,
            page = 1
        ) }
    }
}

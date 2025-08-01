package com.amsterdam.domain.useCase

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.domain.useCase.home.GetTopRatedMoviesUseCase
import com.amsterdam.domain.useCase.utils.specificMovieList
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetTopRatedMoviesUseCaseTest {

    private lateinit var movieRepository: MovieRepository
    private lateinit var getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase

    @BeforeEach
    fun setUp() {
        movieRepository = mockk(relaxed = true)
        getTopRatedMoviesUseCase = GetTopRatedMoviesUseCase(movieRepository)
    }

    @Test
    fun `should call getTopRatedMovies with default page when no page is provided`() = runTest {
        // When
        getTopRatedMoviesUseCase()

        // Then
        coVerify(exactly = 1) { movieRepository.getTopRatedMovies(page = 1) }
    }

    @Test
    fun `should call getTopRatedMovies with specified page`() = runTest {
        // Given
        val page = 5

        // When
        getTopRatedMoviesUseCase(page)

        // Then
        coVerify(exactly = 1) { movieRepository.getTopRatedMovies(page = page) }
    }

    @Test
    fun `should return top rated movies when repository returns them`() = runTest {
        // Given
        coEvery { movieRepository.getTopRatedMovies(any()) } returns specificMovieList

        // When
        val result = getTopRatedMoviesUseCase()

        // Then
        assertThat(result).isEqualTo(specificMovieList)
    }

    @Test
    fun `should return empty list when repository returns an empty list`() = runTest {
        // Given
        coEvery { movieRepository.getTopRatedMovies(any()) } returns emptyList()

        // When
        val result = getTopRatedMoviesUseCase()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should throw AflamiException when repository throws an exception`() = runTest {
        // Given
        val expectedException = AflamiException()
        coEvery { movieRepository.getTopRatedMovies(any()) } throws expectedException

        // When
        val exception = assertThrows<AflamiException> {
            getTopRatedMoviesUseCase()
        }

        // Then
        assertThat(exception).isEqualTo(expectedException)
    }
}
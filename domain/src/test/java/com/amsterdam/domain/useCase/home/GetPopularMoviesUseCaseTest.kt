package com.amsterdam.domain.useCase.home

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.domain.useCase.utils.fakeMovieList
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetPopularMoviesUseCaseTest {
    private lateinit var movieRepository: MovieRepository
    private lateinit var getPopularMoviesUseCase: GetPopularMoviesUseCase

    @BeforeEach
    fun setUp() {
        movieRepository = mockk(relaxed = true)
        getPopularMoviesUseCase = GetPopularMoviesUseCase(movieRepository)
    }

    @Test
    fun `should call getPopularMovies exactly once`() = runTest {
        coEvery { movieRepository.getPopularMovies() } returns fakeMovieList
        getPopularMoviesUseCase()
        coVerify(exactly = 1) { movieRepository.getPopularMovies() }
    }

    @Test
    fun `should return list of popular movies when data is available`() = runTest {
        coEvery { movieRepository.getPopularMovies() } returns fakeMovieList
        val result = getPopularMoviesUseCase()
        assertThat(result).isEqualTo(fakeMovieList)
    }

    @Test
    fun `should return empty list when no popular movies are available`() = runTest {
        coEvery { movieRepository.getPopularMovies() } returns emptyList()
        val result = getPopularMoviesUseCase()
        assertThat(result).isEmpty()
    }

    @Test
    fun `should throw AflamiException when getPopularMovies fails`() = runTest {
        coEvery { movieRepository.getPopularMovies() } throws AflamiException()
        assertThrows<AflamiException> { getPopularMoviesUseCase() }
    }
}
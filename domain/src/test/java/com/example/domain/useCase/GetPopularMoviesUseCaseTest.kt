package com.example.domain.useCase

import com.example.domain.repository.MovieRepository
import com.example.domain.useCase.utils.specificMovieList
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
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
    fun `should return popular movies when repository returns them`() = runTest {
        // Given
        coEvery { movieRepository.getPopularMovies() } returns specificMovieList

        // When
        val result = getPopularMoviesUseCase()

        // Then
        assertThat(result).isEqualTo(specificMovieList)
    }

    @Test
    fun `should throw exception when repository throw exception`() = runTest {
        // Given
        val expectedMessage = "Repository failure"
        coEvery { movieRepository.getPopularMovies() } throws Exception(expectedMessage)

        // When
        val exception = assertThrows<Exception> {
            getPopularMoviesUseCase()
        }

        // Then
        assertThat(exception).hasMessageThat().isEqualTo(expectedMessage)
    }
}
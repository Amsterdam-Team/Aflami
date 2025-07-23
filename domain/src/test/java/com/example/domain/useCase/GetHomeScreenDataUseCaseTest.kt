package com.example.domain.useCase

import com.example.domain.useCase.utils.specificMovieList
import com.example.entity.category.MovieGenre
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


class GetHomeScreenDataUseCaseTest {
    private lateinit var getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase
    private lateinit var getPopularMoviesUseCase: GetPopularMoviesUseCase
    private lateinit var getHomeScreenDataUseCase: GetHomeScreenDataUseCase
    private lateinit var  getUpcomingMoviesUseCase :  GetUpcomingMoviesUseCase
    @BeforeEach
    fun setUp() {
        getTopRatedMoviesUseCase = mockk(relaxed = true)
        getPopularMoviesUseCase = mockk(relaxed = true)
        getUpcomingMoviesUseCase = mockk(relaxed = true)
        getHomeScreenDataUseCase = GetHomeScreenDataUseCase(
            getTopRatedMoviesUseCase = getTopRatedMoviesUseCase,
            getPopularMoviesUseCase = getPopularMoviesUseCase,
            getUpcomingMoviesUseCase = getUpcomingMoviesUseCase
        )
    }

    @Test
    fun `should return all data when top rated, popular, and upcoming return data`() = runTest {
        // Given
        coEvery { getTopRatedMoviesUseCase() } returns specificMovieList
        coEvery { getPopularMoviesUseCase() } returns specificMovieList
        coEvery { getUpcomingMoviesUseCase(MovieGenre.ALL) } returns specificMovieList

        // When
        val result = getHomeScreenDataUseCase()

        // Then
        assertThat(result).isEqualTo(
            GetHomeScreenDataUseCase.HomeScreenData(
                topRatedMovies = specificMovieList,
                popularMovies = specificMovieList,
                upComingMovies = specificMovieList
            )
        )
    }

    @Test
    fun `should throw exception when getTopRatedMoviesUseCase throws`() = runTest {
        // Given
        val expectedMessage = "Top rated error"
        coEvery { getTopRatedMoviesUseCase() } throws Exception(expectedMessage)

        // When
        val exception = assertThrows<Exception> {
            getHomeScreenDataUseCase()
        }

        // Then
        assertThat(exception).hasMessageThat().isEqualTo(expectedMessage)
    }

    @Test
    fun `should throw exception when getPopularMoviesUseCase throws`() = runTest {
        // Given
        val expectedMessage = "Popular error"
        coEvery { getTopRatedMoviesUseCase() } returns specificMovieList
        coEvery { getPopularMoviesUseCase() } throws Exception(expectedMessage)

        // When
        val exception = assertThrows<Exception> {
            getHomeScreenDataUseCase()
        }

        // Then
        assertThat(exception).hasMessageThat().isEqualTo(expectedMessage)
    }

    @Test
    fun `should throw exception when getUpcomingMoviesUseCase throws`() = runTest {
        // Given
        val expectedMessage = "Upcoming error"
        coEvery { getTopRatedMoviesUseCase() } returns specificMovieList
        coEvery { getPopularMoviesUseCase() } returns specificMovieList
        coEvery { getUpcomingMoviesUseCase(MovieGenre.ALL) } throws Exception(expectedMessage)

        // When
        val exception = assertThrows<Exception> {
            getHomeScreenDataUseCase()
        }

        // Then
        assertThat(exception).hasMessageThat().isEqualTo(expectedMessage)
    }

}
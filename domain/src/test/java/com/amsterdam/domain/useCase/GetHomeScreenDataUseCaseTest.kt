package com.amsterdam.domain.useCase

import com.amsterdam.domain.useCase.home.GetHomeScreenDataUseCase
import com.amsterdam.domain.useCase.home.GetPopularMoviesUseCase
import com.amsterdam.domain.useCase.home.GetPopularTvShowsUseCase
import com.amsterdam.domain.useCase.home.GetTopRatedMoviesUseCase
import com.amsterdam.domain.useCase.home.GetTopRatedTvShowsUseCase
import com.amsterdam.domain.useCase.home.GetUpcomingMoviesUseCase
import com.amsterdam.domain.useCase.utils.fakeMovieList
import com.amsterdam.domain.useCase.utils.fakeTvShowList
import com.amsterdam.entity.category.MovieGenre
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


class GetHomeScreenDataUseCaseTest {
    private lateinit var getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase
    private lateinit var getTopRatedTvShowsUseCase: GetTopRatedTvShowsUseCase
    private lateinit var getPopularMoviesUseCase: GetPopularMoviesUseCase
    private lateinit var getPopularTvShowsUseCase: GetPopularTvShowsUseCase
    private lateinit var getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase
    private lateinit var getHomeScreenDataUseCase: GetHomeScreenDataUseCase

    @BeforeEach
    fun setUp() {
        getTopRatedMoviesUseCase = mockk(relaxed = true)
        getTopRatedTvShowsUseCase = mockk(relaxed = true)
        getPopularMoviesUseCase = mockk(relaxed = true)
        getPopularTvShowsUseCase = mockk(relaxed = true)
        getUpcomingMoviesUseCase = mockk(relaxed = true)
        getHomeScreenDataUseCase = GetHomeScreenDataUseCase(
            getTopRatedMoviesUseCase = getTopRatedMoviesUseCase,
            getTopRatedTvShowsUseCase = getTopRatedTvShowsUseCase,
            getPopularMoviesUseCase = getPopularMoviesUseCase,
            getPopularTvShowsUseCase = getPopularTvShowsUseCase,
            getUpcomingMoviesUseCase = getUpcomingMoviesUseCase
        )
    }

    @Test
    fun `should return all data when all child use cases return data`() = runTest {
        // Given
        coEvery { getTopRatedMoviesUseCase() } returns fakeMovieList
        coEvery { getTopRatedTvShowsUseCase() } returns fakeTvShowList
        coEvery { getPopularMoviesUseCase() } returns fakeMovieList
        coEvery { getPopularTvShowsUseCase() } returns fakeTvShowList
        coEvery { getUpcomingMoviesUseCase(MovieGenre.ALL) } returns fakeMovieList

        // When
        val result = getHomeScreenDataUseCase()

        // Then
        assertThat(result).isEqualTo(
            GetHomeScreenDataUseCase.HomeScreenData(
                topRatedMovies = fakeMovieList,
                topRatedTvShows = fakeTvShowList,
                popularMovies = fakeMovieList,
                popularTvShows = fakeTvShowList,
                upComingMovies = fakeMovieList
            )
        )
    }

    @Test
    fun `should call all child use cases exactly once`() = runTest {
        // Given
        coEvery { getTopRatedMoviesUseCase() } returns emptyList()
        coEvery { getTopRatedTvShowsUseCase() } returns emptyList()
        coEvery { getPopularMoviesUseCase() } returns emptyList()
        coEvery { getPopularTvShowsUseCase() } returns emptyList()
        coEvery { getUpcomingMoviesUseCase(MovieGenre.ALL) } returns emptyList()

        // When
        getHomeScreenDataUseCase()

        // Then
        coVerify(exactly = 1) { getTopRatedMoviesUseCase() }
        coVerify(exactly = 1) { getTopRatedTvShowsUseCase() }
        coVerify(exactly = 1) { getPopularMoviesUseCase() }
        coVerify(exactly = 1) { getPopularTvShowsUseCase() }
        coVerify(exactly = 1) { getUpcomingMoviesUseCase(MovieGenre.ALL) }
    }

    @Test
    fun `should throw exception when getTopRatedMoviesUseCase throws`() = runTest {
        // Given
        val expectedMessage = "Top rated movies error"
        coEvery { getTopRatedMoviesUseCase() } throws Exception(expectedMessage)

        // When
        val exception = assertThrows<Exception> {
            getHomeScreenDataUseCase()
        }

        // Then
        assertThat(exception).hasMessageThat().isEqualTo(expectedMessage)
    }

    @Test
    fun `should throw exception when getTopRatedTvShowsUseCase throws`() = runTest {
        // Given
        val expectedMessage = "Top rated TV shows error"
        coEvery { getTopRatedMoviesUseCase() } returns fakeMovieList
        coEvery { getTopRatedTvShowsUseCase() } throws Exception(expectedMessage)

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
        val expectedMessage = "Popular movies error"
        coEvery { getTopRatedMoviesUseCase() } returns fakeMovieList
        coEvery { getTopRatedTvShowsUseCase() } returns fakeTvShowList
        coEvery { getPopularMoviesUseCase() } throws Exception(expectedMessage)

        // When
        val exception = assertThrows<Exception> {
            getHomeScreenDataUseCase()
        }

        // Then
        assertThat(exception).hasMessageThat().isEqualTo(expectedMessage)
    }

    @Test
    fun `should throw exception when getPopularTvShowsUseCase throws`() = runTest {
        // Given
        val expectedMessage = "Popular TV shows error"
        coEvery { getTopRatedMoviesUseCase() } returns fakeMovieList
        coEvery { getTopRatedTvShowsUseCase() } returns fakeTvShowList
        coEvery { getPopularMoviesUseCase() } returns fakeMovieList
        coEvery { getPopularTvShowsUseCase() } throws Exception(expectedMessage)

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
        val expectedMessage = "Upcoming movies error"
        coEvery { getTopRatedMoviesUseCase() } returns fakeMovieList
        coEvery { getTopRatedTvShowsUseCase() } returns fakeTvShowList
        coEvery { getPopularMoviesUseCase() } returns fakeMovieList
        coEvery { getPopularTvShowsUseCase() } returns fakeTvShowList
        coEvery { getUpcomingMoviesUseCase(MovieGenre.ALL) } throws Exception(expectedMessage)

        // When
        val exception = assertThrows<Exception> {
            getHomeScreenDataUseCase()
        }

        // Then
        assertThat(exception).hasMessageThat().isEqualTo(expectedMessage)
    }
}
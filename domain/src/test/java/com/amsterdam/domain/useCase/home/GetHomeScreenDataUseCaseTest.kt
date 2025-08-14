package com.amsterdam.domain.useCase.home

import com.amsterdam.domain.useCase.utils.fakeMovieList
import com.amsterdam.domain.useCase.utils.fakeTvShowList
import com.amsterdam.entity.category.MovieGenre
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


class GetHomeScreenDataUseCaseTest {
    private val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase = mockk(relaxed = true)
    private val getTopRatedTvShowsUseCase: GetTopRatedTvShowsUseCase = mockk(relaxed = true)
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase = mockk(relaxed = true)
    private val getPopularTvShowsUseCase: GetPopularTvShowsUseCase = mockk(relaxed = true)
    private val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase = mockk(relaxed = true)
    private val getHomeScreenDataUseCase by lazy {
        GetHomeScreenDataUseCase(
            getTopRatedMoviesUseCase = getTopRatedMoviesUseCase,
            getTopRatedTvShowsUseCase = getTopRatedTvShowsUseCase,
            getPopularMoviesUseCase = getPopularMoviesUseCase,
            getPopularTvShowsUseCase = getPopularTvShowsUseCase,
            getUpcomingMoviesUseCase = getUpcomingMoviesUseCase
        )
    }


    @Test
    fun `should return all data when all child use cases return data`() = runTest {
        coEvery { getTopRatedMoviesUseCase() } returns fakeMovieList
        coEvery { getTopRatedTvShowsUseCase() } returns fakeTvShowList
        coEvery { getPopularMoviesUseCase() } returns fakeMovieList
        coEvery { getPopularTvShowsUseCase() } returns fakeTvShowList
        coEvery { getUpcomingMoviesUseCase(MovieGenre.ALL) } returns fakeMovieList

        val result = getHomeScreenDataUseCase()

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
        coEvery { getTopRatedMoviesUseCase() } returns emptyList()
        coEvery { getTopRatedTvShowsUseCase() } returns emptyList()
        coEvery { getPopularMoviesUseCase() } returns emptyList()
        coEvery { getPopularTvShowsUseCase() } returns emptyList()
        coEvery { getUpcomingMoviesUseCase(MovieGenre.ALL) } returns emptyList()

        getHomeScreenDataUseCase()

        coVerify(exactly = 1) { getTopRatedMoviesUseCase() }
        coVerify(exactly = 1) { getTopRatedTvShowsUseCase() }
        coVerify(exactly = 1) { getPopularMoviesUseCase() }
        coVerify(exactly = 1) { getPopularTvShowsUseCase() }
        coVerify(exactly = 1) { getUpcomingMoviesUseCase(MovieGenre.ALL) }
    }

    @Test
    fun `should throw exception when getTopRatedMoviesUseCase throws`() = runTest {
        val expectedMessage = "Top rated movies error"
        coEvery { getTopRatedMoviesUseCase() } throws Exception(expectedMessage)

        val exception = assertThrows<Exception> {
            getHomeScreenDataUseCase()
        }

        assertThat(exception).hasMessageThat().isEqualTo(expectedMessage)
    }

    @Test
    fun `should throw exception when getTopRatedTvShowsUseCase throws`() = runTest {
        val expectedMessage = "Top rated TV shows error"
        coEvery { getTopRatedMoviesUseCase() } returns fakeMovieList
        coEvery { getTopRatedTvShowsUseCase() } throws Exception(expectedMessage)

        val exception = assertThrows<Exception> {
            getHomeScreenDataUseCase()
        }

        assertThat(exception).hasMessageThat().isEqualTo(expectedMessage)
    }

    @Test
    fun `should throw exception when getPopularMoviesUseCase throws`() = runTest {
        val expectedMessage = "Popular movies error"
        coEvery { getTopRatedMoviesUseCase() } returns fakeMovieList
        coEvery { getTopRatedTvShowsUseCase() } returns fakeTvShowList
        coEvery { getPopularMoviesUseCase() } throws Exception(expectedMessage)

        val exception = assertThrows<Exception> {
            getHomeScreenDataUseCase()
        }

        assertThat(exception).hasMessageThat().isEqualTo(expectedMessage)
    }

    @Test
    fun `should throw exception when getPopularTvShowsUseCase throws`() = runTest {
        val expectedMessage = "Popular TV shows error"
        coEvery { getTopRatedMoviesUseCase() } returns fakeMovieList
        coEvery { getTopRatedTvShowsUseCase() } returns fakeTvShowList
        coEvery { getPopularMoviesUseCase() } returns fakeMovieList
        coEvery { getPopularTvShowsUseCase() } throws Exception(expectedMessage)

        val exception = assertThrows<Exception> {
            getHomeScreenDataUseCase()
        }

        assertThat(exception).hasMessageThat().isEqualTo(expectedMessage)
    }

    @Test
    fun `should throw exception when getUpcomingMoviesUseCase throws`() = runTest {
        val expectedMessage = "Upcoming movies error"
        coEvery { getTopRatedMoviesUseCase() } returns fakeMovieList
        coEvery { getTopRatedTvShowsUseCase() } returns fakeTvShowList
        coEvery { getPopularMoviesUseCase() } returns fakeMovieList
        coEvery { getPopularTvShowsUseCase() } returns fakeTvShowList
        coEvery { getUpcomingMoviesUseCase(MovieGenre.ALL) } throws Exception(expectedMessage)

        val exception = assertThrows<Exception> {
            getHomeScreenDataUseCase()
        }

        assertThat(exception).hasMessageThat().isEqualTo(expectedMessage)
    }
}
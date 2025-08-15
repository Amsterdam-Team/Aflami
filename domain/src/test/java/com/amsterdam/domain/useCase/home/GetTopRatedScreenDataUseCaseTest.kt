package com.amsterdam.domain.useCase.home

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.useCase.utils.fakeMovieList
import com.amsterdam.domain.useCase.utils.fakeTvShowList
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetTopRatedScreenDataUseCaseTest {
    private val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase = mockk()
    private val getTopRatedTvShowsUseCase: GetTopRatedTvShowsUseCase = mockk()
    private val getTopRatedScreenDataUseCase by lazy {
        GetTopRatedScreenDataUseCase(getTopRatedMoviesUseCase, getTopRatedTvShowsUseCase)
    }

    @Test
    fun `should call both child use cases with default page number`() = runTest {
        coEvery { getTopRatedMoviesUseCase(any(), any()) } returns emptyList()
        coEvery { getTopRatedTvShowsUseCase(any(), any()) } returns emptyList()

        getTopRatedScreenDataUseCase()

        coVerify(exactly = 1) { getTopRatedMoviesUseCase(1, false) }
        coVerify(exactly = 1) { getTopRatedTvShowsUseCase(1, false) }
    }

    @Test
    fun `should call both child use cases with specified page number`() = runTest {
        val page = 5
        coEvery { getTopRatedMoviesUseCase(any(), any()) } returns emptyList()
        coEvery { getTopRatedTvShowsUseCase(any(), any()) } returns emptyList()

        getTopRatedScreenDataUseCase(page)

        coVerify(exactly = 1) { getTopRatedMoviesUseCase(page, false) }
        coVerify(exactly = 1) { getTopRatedTvShowsUseCase(page, false) }
    }

    @Test
    fun `should return TopRatedScreenData object with correct lists`() = runTest {
        coEvery { getTopRatedMoviesUseCase(any(), any()) } returns fakeMovieList
        coEvery { getTopRatedTvShowsUseCase(any(), any()) } returns fakeTvShowList

        val result = getTopRatedScreenDataUseCase()

        assertThat(result.topRatedMovies).isEqualTo(fakeMovieList)
        assertThat(result.topRatedTvShows).isEqualTo(fakeTvShowList)
    }

    @Test
    fun `should propagate exception when getTopRatedMoviesUseCase throws`() = runTest {
        coEvery { getTopRatedMoviesUseCase(any(), any()) } throws AflamiException()

        assertThrows<AflamiException> {
            getTopRatedScreenDataUseCase()
        }
    }

    @Test
    fun `should propagate exception when getTopRatedTvShowsUseCase throws`() = runTest {
        coEvery { getTopRatedMoviesUseCase(any(), any()) } returns emptyList()
        coEvery { getTopRatedTvShowsUseCase(any(), any()) } throws AflamiException()

        assertThrows<AflamiException> {
            getTopRatedScreenDataUseCase()
        }
    }
}
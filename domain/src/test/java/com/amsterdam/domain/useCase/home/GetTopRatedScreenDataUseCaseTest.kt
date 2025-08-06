package com.amsterdam.domain.useCase.home

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.useCase.utils.fakeMovieList
import com.amsterdam.domain.useCase.utils.fakeTvShowList
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetTopRatedScreenDataUseCaseTest {
    private lateinit var getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase
    private lateinit var getTopRatedTvShowsUseCase: GetTopRatedTvShowsUseCase
    private lateinit var getTopRatedScreenDataUseCase: GetTopRatedScreenDataUseCase

    @BeforeEach
    fun setUp() {
        getTopRatedMoviesUseCase = mockk()
        getTopRatedTvShowsUseCase = mockk()
        getTopRatedScreenDataUseCase =
            GetTopRatedScreenDataUseCase(getTopRatedMoviesUseCase, getTopRatedTvShowsUseCase)
    }

    @Test
    fun `should call both child use cases with default page number`() = runTest {
        // Given
        coEvery { getTopRatedMoviesUseCase(any()) } returns emptyList()
        coEvery { getTopRatedTvShowsUseCase(any()) } returns emptyList()

        // When
        getTopRatedScreenDataUseCase()

        // Then
        coVerify(exactly = 1) { getTopRatedMoviesUseCase(1) }
        coVerify(exactly = 1) { getTopRatedTvShowsUseCase(1) }
    }

    @Test
    fun `should call both child use cases with specified page number`() = runTest {
        // Given
        val page = 5
        coEvery { getTopRatedMoviesUseCase(any()) } returns emptyList()
        coEvery { getTopRatedTvShowsUseCase(any()) } returns emptyList()

        // When
        getTopRatedScreenDataUseCase(page)

        // Then
        coVerify(exactly = 1) { getTopRatedMoviesUseCase(page) }
        coVerify(exactly = 1) { getTopRatedTvShowsUseCase(page) }
    }

    @Test
    fun `should return TopRatedScreenData object with correct lists`() = runTest {
        // Given
        coEvery { getTopRatedMoviesUseCase(any()) } returns fakeMovieList
        coEvery { getTopRatedTvShowsUseCase(any()) } returns fakeTvShowList

        // When
        val result = getTopRatedScreenDataUseCase()

        // Then
        assertThat(result.topRatedMovies).isEqualTo(fakeMovieList)
        assertThat(result.topRatedTvShows).isEqualTo(fakeTvShowList)
    }

    @Test
    fun `should propagate exception when getTopRatedMoviesUseCase throws`() = runTest {
        // Given
        coEvery { getTopRatedMoviesUseCase(any()) } throws AflamiException()

        // When & Then
        assertThrows<AflamiException> {
            getTopRatedScreenDataUseCase()
        }
    }

    @Test
    fun `should propagate exception when getTopRatedTvShowsUseCase throws`() = runTest {
        // Given
        coEvery { getTopRatedMoviesUseCase(any()) } returns emptyList()
        coEvery { getTopRatedTvShowsUseCase(any()) } throws AflamiException()

        // When & Then
        assertThrows<AflamiException> {
            getTopRatedScreenDataUseCase()
        }
    }
}
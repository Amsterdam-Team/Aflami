package com.amsterdam.domain.useCase

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.repository.TvShowRepository
import com.amsterdam.domain.useCase.home.GetTopRatedTvShowsUseCase
import com.amsterdam.domain.useCase.utils.fakeTvShowList
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetTopRatedTvShowsUseCaseTest {
    private lateinit var tvShowRepository: TvShowRepository
    private lateinit var getTopRatedTvShowsUseCase: GetTopRatedTvShowsUseCase

    @BeforeEach
    fun setUp() {
        tvShowRepository = mockk(relaxed = true)
        getTopRatedTvShowsUseCase = GetTopRatedTvShowsUseCase(tvShowRepository)
    }

    @Test
    fun `should call getTopRatedTvShows with default page when no page is provided`() = runTest {
        // When
        getTopRatedTvShowsUseCase()

        // Then
        coVerify(exactly = 1) { tvShowRepository.getTopRatedTvShows(page = 1) }
    }

    @Test
    fun `should call getTopRatedTvShows with a specific page`() = runTest {
        // Given
        val page = 3

        // When
        getTopRatedTvShowsUseCase(page)

        // Then
        coVerify(exactly = 1) { tvShowRepository.getTopRatedTvShows(page = page) }
    }

    @Test
    fun `should return a list of top rated tv shows when data is available`() = runTest {
        // Given
        coEvery { tvShowRepository.getTopRatedTvShows(any()) } returns fakeTvShowList

        // When
        val result = getTopRatedTvShowsUseCase()

        // Then
        assertThat(result).isEqualTo(fakeTvShowList)
    }

    @Test
    fun `should return an empty list when no top rated tv shows are available`() = runTest {
        // Given
        coEvery { tvShowRepository.getTopRatedTvShows(any()) } returns emptyList()

        // When
        val result = getTopRatedTvShowsUseCase()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should throw an AflamiException when the repository call fails`() = runTest {
        // Given
        coEvery { tvShowRepository.getTopRatedTvShows(any()) } throws AflamiException()

        // When & Then
        assertThrows<AflamiException> { getTopRatedTvShowsUseCase() }
    }
}
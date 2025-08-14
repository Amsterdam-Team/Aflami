package com.amsterdam.domain.useCase.home

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.repository.TvShowRepository
import com.amsterdam.domain.useCase.utils.fakeTvShowList
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetTopRatedTvShowsUseCaseTest {
    private val tvShowRepository: TvShowRepository = mockk(relaxed = true)
    private val getTopRatedTvShowsUseCase by lazy {
        GetTopRatedTvShowsUseCase(tvShowRepository)
    }


    @Test
    fun `should call getTopRatedTvShows with default page when no page is provided`() = runTest {
        getTopRatedTvShowsUseCase()

        coVerify(exactly = 1) { tvShowRepository.getTopRatedTvShows(page = 1) }
    }

    @Test
    fun `should call getTopRatedTvShows with a specific page`() = runTest {
        val page = 3

        getTopRatedTvShowsUseCase(page)

        coVerify(exactly = 1) { tvShowRepository.getTopRatedTvShows(page = page) }
    }

    @Test
    fun `should return a list of top rated tv shows when data is available`() = runTest {
        coEvery { tvShowRepository.getTopRatedTvShows(any()) } returns fakeTvShowList

        val result = getTopRatedTvShowsUseCase()

        assertThat(result).isEqualTo(fakeTvShowList)
    }

    @Test
    fun `should return an empty list when no top rated tv shows are available`() = runTest {
        coEvery { tvShowRepository.getTopRatedTvShows(any()) } returns emptyList()

        val result = getTopRatedTvShowsUseCase()

        assertThat(result).isEmpty()
    }

    @Test
    fun `should throw an AflamiException when the repository call fails`() = runTest {
        coEvery { tvShowRepository.getTopRatedTvShows(any()) } throws AflamiException()

        assertThrows<AflamiException> { getTopRatedTvShowsUseCase() }
    }
}
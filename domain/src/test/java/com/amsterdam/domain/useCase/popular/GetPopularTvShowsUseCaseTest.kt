package com.amsterdam.domain.useCase.popular

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

class GetPopularTvShowsUseCaseTest {
    private val tvShowRepository: TvShowRepository = mockk(relaxed = true)
    private val getPopularTvShowsUseCase by lazy {
        GetPopularTvShowsUseCase(tvShowRepository)
    }

    @Test
    fun `should call getPopularTvShows exactly once`() = runTest {
        getPopularTvShowsUseCase()

        coVerify(exactly = 1) { tvShowRepository.getPopularTvShows() }
    }

    @Test
    fun `should return a list of popular tv shows when data is available`() = runTest {
        coEvery { tvShowRepository.getPopularTvShows() } returns fakeTvShowList

        val result = getPopularTvShowsUseCase()

        assertThat(result).isEqualTo(fakeTvShowList)
    }

    @Test
    fun `should return an empty list when no popular tv shows are available`() = runTest {
        coEvery { tvShowRepository.getPopularTvShows() } returns emptyList()

        val result = getPopularTvShowsUseCase()

        assertThat(result).isEmpty()
    }

    @Test
    fun `should throw an AflamiException when the repository call fails`() = runTest {
        coEvery { tvShowRepository.getPopularTvShows() } throws AflamiException()

        assertThrows<AflamiException> { getPopularTvShowsUseCase() }
    }
}
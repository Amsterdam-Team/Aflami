package com.amsterdam.domain.useCase.myRating.tvShow

import com.amsterdam.domain.repository.TvShowRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class DeleteUserRatedTvShowUseCaseTest {

    private val tvShowRepository: TvShowRepository = mockk()
    private val deleteUserRatedTvShowUseCase by lazy {
        DeleteUserRatedTvShowUseCase(tvShowRepository)
    }

    @Test
    fun `should call repository with correct ID when invoked`() = runTest {
        coEvery { tvShowRepository.deleteTvShowRate(tvShowId) } just Runs

        val result = deleteUserRatedTvShowUseCase.deleteTvShowRate(tvShowId)

        coVerify(exactly = 1) { tvShowRepository.deleteTvShowRate(tvShowId) }
        assertThat(result).isEqualTo(Unit)
    }

    private val tvShowId = 123L
}

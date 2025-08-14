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

class SetUserTvShowRatingUseCaseTest {

    private val tvShowRepository: TvShowRepository = mockk(relaxed = true)
    private val setUserTvShowRatingUseCase by lazy {
        SetUserTvShowRatingUseCase(tvShowRepository)
    }

    @Test
    fun `should call setTvShowRate on repository with correct parameters`() = runTest {
        coEvery { tvShowRepository.setTvShowRate(rate = rate, tvShowId = tvShowId) } just Runs

        val result = setUserTvShowRatingUseCase.setUserMovieRate(rate, tvShowId)

        assertThat(result).isEqualTo(Unit)
        coVerify { tvShowRepository.setTvShowRate(rate = rate, tvShowId = tvShowId) }
    }

    private val rate = 8
    private val tvShowId = 123L
}

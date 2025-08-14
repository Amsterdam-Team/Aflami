package com.amsterdam.domain.useCase.details

import com.amsterdam.domain.repository.TvShowRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetEpisodeVideosUseCaseTest {
    private val tvShowRepository: TvShowRepository = mockk(relaxed = true)
    private val getEpisodeVideosUseCase by lazy {
        GetEpisodeVideosUseCase(tvShowRepository)
    }


    @Test
    fun `should return video url when called with correct data`() = runTest {
        coEvery {
            tvShowRepository.getEpisodeVideoUrl(
                tvShowId,
                seasonNumber,
                episodeNumber
            )
        } returns expectedVideoUrl

        val result = getEpisodeVideosUseCase(
            tvShowId,
            seasonNumber,
            episodeNumber
        )

        assertThat(result).isEqualTo(expectedVideoUrl)
        coVerify {
            tvShowRepository.getEpisodeVideoUrl(
                tvShowId,
                seasonNumber,
                episodeNumber
            )
        }

    }

    private val tvShowId = 2435L
    private val seasonNumber = 1
    private val episodeNumber = 1
    private val expectedVideoUrl = "efhafjj"
}
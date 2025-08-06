package com.amsterdam.domain.useCase.details

import com.amsterdam.domain.repository.TvShowRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetEpisodeVideosUseCaseTest {
    private lateinit var tvShowRepository: TvShowRepository
    private lateinit var getEpisodeVideosUseCase: GetEpisodeVideosUseCase

    @BeforeEach
    fun setUp() {
        tvShowRepository = mockk(relaxed = true)
        getEpisodeVideosUseCase = GetEpisodeVideosUseCase(tvShowRepository)
    }

    @Test
    fun `getEpisodeVideosUseCase should return video url when  `() = runTest {
        val tvShowId = 2435L
        val seasonNumber = 1
        val episodeNumber = 1
        val expectedVideoUrl = "efhafjj"
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
}
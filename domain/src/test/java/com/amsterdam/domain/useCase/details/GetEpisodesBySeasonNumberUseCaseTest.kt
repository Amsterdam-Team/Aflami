package com.amsterdam.domain.useCase.details

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.repository.TvShowRepository
import com.amsterdam.entity.Episode
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetEpisodesBySeasonNumberUseCaseTest {
    private val tvShowRepository: TvShowRepository = mockk(relaxed = true)
    private val getEpisodesBySeasonNumberUseCase by lazy {
        GetEpisodesBySeasonNumberUseCase(tvShowRepository)
    }

    @Test
    fun `should call tvShowRepository when given valid tvShowId and seasonNumber`() = runTest {
        coEvery {
            tvShowRepository.getEpisodesBySeasonNumber(
                tvShowId,
                seasonNumber
            )
        } returns emptyList()

        getEpisodesBySeasonNumberUseCase(tvShowId, seasonNumber)

        coVerify(exactly = 1) { tvShowRepository.getEpisodesBySeasonNumber(tvShowId, seasonNumber) }
    }

    @Test
    fun `should return a list of episodes when repository returns data`() = runTest {

        coEvery {
            tvShowRepository.getEpisodesBySeasonNumber(
                tvShowId,
                seasonNumber
            )
        } returns fakeEpisodesList

        val result = getEpisodesBySeasonNumberUseCase(tvShowId, seasonNumber)

        assertThat(result).isEqualTo(fakeEpisodesList)
    }

    @Test
    fun `should return an empty list when repository returns no episodes`() = runTest {
        coEvery {
            tvShowRepository.getEpisodesBySeasonNumber(
                tvShowId,
                seasonNumber
            )
        } returns emptyList()

        val result = getEpisodesBySeasonNumberUseCase(tvShowId, seasonNumber)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should throw AflamiException when repository call fails`() = runTest {
        coEvery {
            tvShowRepository.getEpisodesBySeasonNumber(
                tvShowId,
                seasonNumber
            )
        } throws AflamiException()

        assertThrows<AflamiException> { getEpisodesBySeasonNumberUseCase(tvShowId, seasonNumber) }
    }

    @Test
    fun `should fail gracefully when given invalid data`() = runTest {
        coEvery {
            tvShowRepository.getEpisodesBySeasonNumber(
                invalidTvShowId,
                invalidSeasonNumber
            )
        } returns emptyList()

        val result = getEpisodesBySeasonNumberUseCase(invalidTvShowId, invalidSeasonNumber)

        coVerify(exactly = 1) {
            tvShowRepository.getEpisodesBySeasonNumber(
                invalidTvShowId,
                invalidSeasonNumber
            )
        }
        assertThat(result).isEmpty()
    }

    private val tvShowId = 123L
    private val seasonNumber = 1
    private val invalidTvShowId = -1L
    private val invalidSeasonNumber = -1
    private val fakeEpisode = Episode(
        id = 1L,
        title = "Test Episode",
        episodeNumber = 1,
        description = "Description",
        episodeImageUrl = "image.jpg",
        rating = 8.5f,
        airDate = LocalDate(2023, 1, 1),
        seasonNumber = 1,
        runTimeInMinutes = 45,
        videoUrl = ""
    )
    private val fakeEpisodesList = listOf(fakeEpisode, fakeEpisode.copy(id = 2L, episodeNumber = 2))
}
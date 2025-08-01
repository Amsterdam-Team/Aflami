package com.amsterdam.domain.useCase

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.repository.TvShowRepository
import com.amsterdam.domain.useCase.details.GetEpisodesBySeasonNumberUseCase
import com.amsterdam.entity.Episode
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetEpisodesBySeasonNumberUseCaseTest {
    private lateinit var tvShowRepository: TvShowRepository
    private lateinit var getEpisodesBySeasonNumberUseCase: GetEpisodesBySeasonNumberUseCase

    private val fakeEpisode = Episode(
        id = 1L,
        title = "Test Episode",
        episodeNumber = 1,
        description = "Description",
        episodeImageUrl = "image.jpg",
        rating = 8.5f,
        airDate = LocalDate(2023, 1, 1),
        seasonNumber = 1,
        runTimeInMinutes = 45
    )
    private val fakeEpisodesList = listOf(fakeEpisode, fakeEpisode.copy(id = 2L, episodeNumber = 2))

    @BeforeEach
    fun setUp() {
        tvShowRepository = mockk(relaxed = true)
        getEpisodesBySeasonNumberUseCase = GetEpisodesBySeasonNumberUseCase(tvShowRepository)
    }

    @Test
    fun `should call tvShowRepository with correct tvShowId and seasonNumber`() = runTest {
        // Given
        val tvShowId = 123L
        val seasonNumber = 2
        coEvery {
            tvShowRepository.getEpisodesBySeasonNumber(
                tvShowId,
                seasonNumber
            )
        } returns emptyList()

        // When
        getEpisodesBySeasonNumberUseCase(tvShowId, seasonNumber)

        // Then
        coVerify(exactly = 1) { tvShowRepository.getEpisodesBySeasonNumber(tvShowId, seasonNumber) }
    }

    @Test
    fun `should return a list of episodes when repository returns data`() = runTest {
        // Given
        val tvShowId = 123L
        val seasonNumber = 1
        coEvery {
            tvShowRepository.getEpisodesBySeasonNumber(
                tvShowId,
                seasonNumber
            )
        } returns fakeEpisodesList

        // When
        val result = getEpisodesBySeasonNumberUseCase(tvShowId, seasonNumber)

        // Then
        assertThat(result).isEqualTo(fakeEpisodesList)
    }

    @Test
    fun `should return an empty list when repository returns no episodes`() = runTest {
        // Given
        val tvShowId = 123L
        val seasonNumber = 1
        coEvery {
            tvShowRepository.getEpisodesBySeasonNumber(
                tvShowId,
                seasonNumber
            )
        } returns emptyList()

        // When
        val result = getEpisodesBySeasonNumberUseCase(tvShowId, seasonNumber)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should throw AflamiException when repository call fails`() = runTest {
        // Given
        val tvShowId = 123L
        val seasonNumber = 1
        coEvery {
            tvShowRepository.getEpisodesBySeasonNumber(
                tvShowId,
                seasonNumber
            )
        } throws AflamiException()

        // When & Then
        assertThrows<AflamiException> { getEpisodesBySeasonNumberUseCase(tvShowId, seasonNumber) }
    }

    @Test
    fun `should handle invalid input gracefully`() = runTest {
        // Given
        val invalidTvShowId = -1L
        val invalidSeasonNumber = -1
        coEvery {
            tvShowRepository.getEpisodesBySeasonNumber(
                invalidTvShowId,
                invalidSeasonNumber
            )
        } returns emptyList()

        // When
        val result = getEpisodesBySeasonNumberUseCase(invalidTvShowId, invalidSeasonNumber)

        // Then
        coVerify(exactly = 1) {
            tvShowRepository.getEpisodesBySeasonNumber(
                invalidTvShowId,
                invalidSeasonNumber
            )
        }
        assertThat(result).isEmpty()
    }
}
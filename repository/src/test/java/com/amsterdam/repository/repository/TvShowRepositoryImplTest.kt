package com.amsterdam.repository.repository

import com.amsterdam.entity.Season
import com.amsterdam.repository.datasource.local.AppPreferences
import com.amsterdam.repository.datasource.local.AuthenticationLocalDataSource
import com.amsterdam.repository.datasource.local.CategoryLocalDataSource
import com.amsterdam.repository.datasource.local.TvShowLocalDataSource
import com.amsterdam.repository.datasource.remote.CategoryRemoteSource
import com.amsterdam.repository.datasource.remote.TvShowsRemoteSource
import com.amsterdam.repository.dto.remote.RemoteCastAndCrewResponse
import com.amsterdam.repository.dto.remote.RemoteTvShowResponse
import com.amsterdam.repository.dto.remote.VideoResponse
import com.amsterdam.repository.mapper.remote.toEntity
import com.amsterdam.repository.mapper.remote.toEntityList
import com.amsterdam.repository.security.CryptoData
import com.amsterdam.repository.utils.episodeResponse
import com.amsterdam.repository.utils.remoteCastDto
import com.amsterdam.repository.utils.remoteEpisodeDto
import com.amsterdam.repository.utils.remoteTvShowItemDto
import com.amsterdam.repository.utils.tvShowDetailsRemoteResponse
import com.amsterdam.repository.utils.videoDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class TvShowRepositoryImplTest {
    private lateinit var tvShowRepository: TvShowRepositoryImpl
    private val localTvDataSource: TvShowLocalDataSource = mockk()
    private val remoteTvDataSource: TvShowsRemoteSource = mockk()
    private val authenticationLocalDataSource: AuthenticationLocalDataSource = mockk()
    private val cryptoData: CryptoData = mockk()
    private val categoryLocalDataSource: CategoryLocalDataSource = mockk()
    private val categoryRemoteSource: CategoryRemoteSource = mockk()
    private val preferences: AppPreferences = mockk()

    @BeforeEach
    fun setUp() {
        tvShowRepository = TvShowRepositoryImpl(
            localTvDataSource,
            remoteTvDataSource,
            authenticationLocalDataSource,
            preferences,
            cryptoData,
            categoryLocalDataSource,
            categoryRemoteSource

        )
    }

    @Test
    fun `getTvShowByKeyword should return list of TvShow`() = runTest {
        // Given
        val keyword = "Breaking"
        val page = 1
        val tvShowsPerPage = 20
        val expectedTvShows = listOf(
            remoteTvShowItemDto
        )
        coEvery {
            remoteTvDataSource.getTvShowsByKeyword(
                keyword,
                page
            )
        } returns RemoteTvShowResponse(
            page = 1,
            results = expectedTvShows,
            totalPages = 1,
            totalResults = 1
        )
        // When
        val result = tvShowRepository.getTvShowByKeyword(keyword, page, tvShowsPerPage)
        // Then
        assertThat(result).isEqualTo(expectedTvShows.toEntityList())
        coVerify { remoteTvDataSource.getTvShowsByKeyword(keyword, page) }
    }

    @Test
    fun `getEpisodeVideosUrl should return episode videos url`() = runTest {
        // Given
        val tvShowId = 1L
        val seasonNumber = 1
        val episodeNumber = 1
        val expectedResult = VideoResponse(results = listOf(videoDto))
        val episodeVideosUrl = "https://www.youtube.com/watch?v=someKey"
        coEvery {
            remoteTvDataSource.getEpisodeVideos(tvShowId, seasonNumber, episodeNumber)
        } returns expectedResult
        // When
        val result = tvShowRepository.getEpisodeVideoUrl(tvShowId, seasonNumber, episodeNumber)
        // Then
        assertThat(result).isEqualTo(episodeVideosUrl)
        coVerify { remoteTvDataSource.getEpisodeVideos(tvShowId, seasonNumber, episodeNumber) }
    }

    @Test
    fun `getTvShowCast should return list of Actor`() = runTest {
        // Given
        val tvShowId = 123L
        val remoteCastList = listOf(
            remoteCastDto
        )
        val response = RemoteCastAndCrewResponse(cast = remoteCastList)
        val expectedActors = remoteCastList.toEntityList()

        coEvery { remoteTvDataSource.getTvShowCast(tvShowId) } returns response

        // When
        val result = tvShowRepository.getTvShowCast(tvShowId)

        // Then
        assertThat(result).isEqualTo(expectedActors)
        coVerify(exactly = 1) { remoteTvDataSource.getTvShowCast(tvShowId) }
    }

    @Test
    fun `getTvShowSeasons should return mapped season list`() = runTest {
        // Given
        val tvShowId = 1L
        val expectedSeasons = listOf(
            Season(
                id = 1,
                title = "Season 1",
                episodeCount = 10,
                seasonNumber = 1
            ),
            Season(
                id = 2,
                title = "Season 2",
                episodeCount = 12,
                seasonNumber = 2
            )
        )
        val remoteTvShowDetailsDto = tvShowDetailsRemoteResponse

        coEvery { remoteTvDataSource.getTvShowDetailsById(tvShowId) } returns remoteTvShowDetailsDto

        // When
        val result = tvShowRepository.getTvShowSeasons(tvShowId)

        // Then
        assertThat(result).hasSize(expectedSeasons.size)
        assertThat(result[0].title).isEqualTo(expectedSeasons[0].title)
        assertThat(result[1].episodeCount).isEqualTo(expectedSeasons[1].episodeCount)
        coVerify(exactly = 1) { remoteTvDataSource.getTvShowDetailsById(tvShowId) }
    }

    /*@Test
    fun `getEpisodesBySeasonNumber returns list of Episode with video urls`() = runTest {
        // Given
        val tvShowId = 1L
        val seasonNumber = 1
        val episodeDtos = listOf(
            remoteEpisodeDto,
        )
        val remoteResponse = episodeResponse
        val mockVideoDto = videoDto.copy(site = "YouTube", key = "someKey")

        coEvery {
            remoteTvDataSource.getEpisodesBySeasonNumber(tvShowId, seasonNumber)
        } returns remoteResponse
        coEvery {
            remoteTvDataSource.getEpisodeVideos(tvShowId, seasonNumber, any())
        } returns VideoResponse(results = listOf(mockVideoDto))

        val expected = listOf(
            episodeDtos[0].toEntity().copy(videoUrl = "https://www.youtube.com/watch?v=someKey")
        )

        // When
        val result = tvShowRepository.getEpisodesBySeasonNumber(tvShowId, seasonNumber)

        // Then
        assertThat(result).isEqualTo(expected)
        coVerify { remoteTvDataSource.getEpisodesBySeasonNumber(tvShowId, seasonNumber) }
        coVerify(exactly = 1) { remoteTvDataSource.getEpisodeVideos(tvShowId, seasonNumber, any()) }
    }*/
}
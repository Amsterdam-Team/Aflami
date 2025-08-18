package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.TvShowRepository
import com.amsterdam.entity.Season
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.entity.category.TvShowGenre
import com.amsterdam.repository.datasource.remote.TvShowsRemoteDataSource
import com.amsterdam.repository.dto.remote.CastAndCrewRemoteResponse
import com.amsterdam.repository.dto.remote.TvShowRemoteResponse
import com.amsterdam.repository.dto.remote.VideoRemoteResponse
import com.amsterdam.repository.mapper.toEntityList
import com.amsterdam.repository.utils.remoteCastDto
import com.amsterdam.repository.utils.remoteTvShowItemDto
import com.amsterdam.repository.utils.tvShowDetailsRemoteResponse
import com.amsterdam.repository.utils.videoDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class TvShowRepositoryImplTest {
    private val tvShowRemoteDataSource: TvShowsRemoteDataSource = mockk()
    private val tvShowRepository: TvShowRepository by lazy {
        TvShowRepositoryImpl(
            localTvDataSource = mockk(),
            remoteTvDataSource = tvShowRemoteDataSource,
            preferences = mockk(),
            categoryLocalDataSource = mockk(),
            categoryRemoteDataSource = mockk()
        )
    }

    @Test
    fun `getTvShowByKeyword should return list of TvShow`() = runTest {
        coEvery {
            tvShowRemoteDataSource.getTvShowsByKeyword(
                any(),
                any()
            )
        } returns remoteTvShowResponse

        val result = tvShowRepository.getTvShowByKeyword(keyword, page, tvShowsPerPage)

        assertThat(result).isEqualTo(expectedTvShows.toEntityList())
    }

    @Test
    fun `getEpisodeVideosUrl should return episode videos url`() = runTest {
        coEvery {
            tvShowRemoteDataSource.getEpisodeVideos(tvShowId, seasonNumber, episodeNumber)
        } returns expectedResult

        val result = tvShowRepository.getEpisodeVideoUrl(tvShowId, seasonNumber, episodeNumber)

        assertThat(result).isEqualTo(episodeVideosUrl)
    }

    @Test
    fun `getTvShowCast should return list of Actor`() = runTest {
        coEvery { tvShowRemoteDataSource.getTvShowCast(tvShowId) } returns response

        val result = tvShowRepository.getTvShowCast(tvShowId)

        assertThat(result).isEqualTo(expectedActors)
    }

    @Test
    fun `getTvShowSeasons should return mapped season list`() = runTest {
        coEvery { tvShowRemoteDataSource.getTvShowDetailsById(tvShowId) } returns tvShowDetailsRemoteResponse

        val result = tvShowRepository.getTvShowSeasons(tvShowId)

        assertThat(result).isEqualTo(expectedSeasons)

    }

    @Test
    fun `getTvShowsByGenreIds should return list of TvShow`() = runTest {
        coEvery {
            tvShowRemoteDataSource.getTvShowsByGenreId(
                any(),
                any()
            )
        } returns remoteTvShowResponse
        val result = tvShowRepository.getTvShowsByGenre(genre, page)
        assertThat(result).isEqualTo(remoteTvShowResponse.results.toEntityList())

    }

    private val keyword = "Breaking"
    private val page = 1
    private val tvShowsPerPage = 20
    private val expectedTvShows = listOf(remoteTvShowItemDto)
    private val tvShowId = 1L
    private val seasonNumber = 1
    private val episodeNumber = 1
    private val expectedResult = VideoRemoteResponse(results = listOf(videoDto))
    private val episodeVideosUrl = "https://www.youtube.com/watch?v=someKey"
    private val remoteCastList = listOf(remoteCastDto)
    private val expectedSeasons = listOf(
        Season(id = 1, title = "Season 1", episodeCount = 10, seasonNumber = 1),
        Season(id = 2, title = "Season 2", episodeCount = 12, seasonNumber = 2)
    )
    private val remoteTvShowResponse = TvShowRemoteResponse(
        page = 1,
        results = expectedTvShows,
        totalPages = 1,
        totalResults = 1
    )
    private val response = CastAndCrewRemoteResponse(cast = remoteCastList)
    private val expectedActors = remoteCastList.toEntityList()
    val genre =  TvShowGenre.COMEDY
}
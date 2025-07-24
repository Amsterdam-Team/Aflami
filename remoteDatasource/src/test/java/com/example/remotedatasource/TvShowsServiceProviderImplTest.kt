package com.example.remotedatasource

import com.example.remotedatasource.api.TvShowsApiService
import com.example.remotedatasource.serviceProvider.implementation.TvShowsServiceProviderImpl
import com.example.repository.dto.remote.EpisodeDto
import com.example.repository.dto.remote.EpisodeResponse
import com.example.repository.dto.remote.RemoteCastAndCrewResponse
import com.example.repository.dto.remote.RemoteTvShowResponse
import com.example.repository.dto.remote.TvShowDetailsRemoteResponse
import com.example.repository.dto.remote.movieGallery.RemoteGalleryResponse
import com.example.repository.dto.remote.review.ReviewsResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach

class TvShowsServiceProviderImplTest {

    private lateinit var tvShowsApiService: TvShowsApiService
    private lateinit var tvShowsServiceProviderImpl: TvShowsServiceProviderImpl

    @BeforeEach
    fun setUp() {
        tvShowsApiService = mockk()
        tvShowsServiceProviderImpl = TvShowsServiceProviderImpl(tvShowsApiService)
    }

    @Test
    fun `getTvShowsByKeyword should call TvShowsApiService`() = runTest {
        // Given
        val keyword = "test"
        val page = 1
        val dummyResponse = RemoteTvShowResponse(
            page = 1,
            results = emptyList(),
            totalPages = 1,
            totalResults = 0
        )
        coEvery { tvShowsApiService.getTvShowsByKeyword(keyword, page) } returns dummyResponse

        // When
        val result = tvShowsServiceProviderImpl.getTvShowsByKeyword(keyword, page)

        // Then
        coVerify(exactly = 1) { tvShowsApiService.getTvShowsByKeyword(keyword, page) }
        assertThat(result).isEqualTo(dummyResponse)
    }

    @Test
    fun `getTvShowDetailsById should call TvShowsApiService`() = runTest {
        // Given
        val tvShowId = 123L
        val dummyResponse = TvShowDetailsRemoteResponse(
            adult = false, backdropPath = null, genres = emptyList(), id = tvShowId,
            originCountry = emptyList(), originalLanguage = "", originalTitle = "",
            overview = "", popularity = 0.0, posterPath = null, releaseDate = "",
            title = "", voteAverage = 0.0, seasons = emptyList(), seasonCount = 0
        )
        coEvery { tvShowsApiService.getTvShowDetailsById(tvShowId) } returns dummyResponse

        // When
        val result = tvShowsServiceProviderImpl.getTvShowDetailsById(tvShowId)

        // Then
        coVerify(exactly = 1) { tvShowsApiService.getTvShowDetailsById(tvShowId) }
        assertThat(result).isEqualTo(dummyResponse)
    }

    @Test
    fun `getTvShowCast should call TvShowsApiService`() = runTest {
        // Given
        val tvShowId = 123L
        val dummyResponse = RemoteCastAndCrewResponse(
            id = tvShowId.toInt(),
            cast = emptyList(),
            crew = emptyList()
        )
        coEvery { tvShowsApiService.getTvShowCast(tvShowId) } returns dummyResponse

        // When
        val result = tvShowsServiceProviderImpl.getTvShowCast(tvShowId)

        // Then
        coVerify(exactly = 1) { tvShowsApiService.getTvShowCast(tvShowId) }
        assertThat(result).isEqualTo(dummyResponse)
    }

    @Test
    fun `getSimilarTvShows should call TvShowsApiService`() = runTest {
        // Given
        val tvShowId = 123L
        val dummyResponse = RemoteTvShowResponse(
            page = 1,
            results = emptyList(),
            totalPages = 1,
            totalResults = 0
        )
        coEvery { tvShowsApiService.getSimilarTvShows(tvShowId) } returns dummyResponse

        // When
        val result = tvShowsServiceProviderImpl.getSimilarTvShows(tvShowId)

        // Then
        coVerify(exactly = 1) { tvShowsApiService.getSimilarTvShows(tvShowId) }
        assertThat(result).isEqualTo(dummyResponse)
    }

    @Test
    fun `getTvShowReviews should call TvShowsApiService`() = runTest {
        // Given
        val tvShowId = 123L
        val dummyResponse = ReviewsResponse(
            id = tvShowId,
            page = 1,
            results = emptyList(),
            totalPages = 1,
            totalResults = 0
        )
        coEvery { tvShowsApiService.getTvShowReviews(tvShowId) } returns dummyResponse

        // When
        val result = tvShowsServiceProviderImpl.getTvShowReviews(tvShowId)

        // Then
        coVerify(exactly = 1) { tvShowsApiService.getTvShowReviews(tvShowId) }
        assertThat(result).isEqualTo(dummyResponse)
    }

    @Test
    fun `getTvShowGallery should call TvShowsApiService`() = runTest {
        // Given
        val tvShowId = 123L
        val dummyResponse = RemoteGalleryResponse(
            id = tvShowId,
            backdrops = emptyList(),
            logos = emptyList(),
            posters = emptyList()
        )
        coEvery { tvShowsApiService.getTvShowGallery(tvShowId) } returns dummyResponse

        // When
        val result = tvShowsServiceProviderImpl.getTvShowGallery(tvShowId)

        // Then
        coVerify(exactly = 1) { tvShowsApiService.getTvShowGallery(tvShowId) }
        assertThat(result).isEqualTo(dummyResponse)
    }

    @Test
    fun `getEpisodesBySeasonNumber should call TvShowsApiService`() = runTest {
        // Given
        val tvShowId = 123L
        val seasonNumber = 1
        val dummyEpisode = EpisodeDto(
            id = 1, episodeNumber = 1, title = "", runtime = "0",
            airDate = "", overview = "", stillPath = null, voteAverage = "0.0", seasonNumber = 1
        )
        val dummyResponse = EpisodeResponse(
            airDate = null, episodes = listOf(dummyEpisode), name = "", overview = "",
            id = 456L, posterPath = null, seasonNumber = seasonNumber.toLong(), voteAverage = 0.0
        )
        coEvery {
            tvShowsApiService.getEpisodesBySeasonNumber(
                tvShowId,
                seasonNumber
            )
        } returns dummyResponse

        // When
        val result = tvShowsServiceProviderImpl.getEpisodesBySeasonNumber(tvShowId, seasonNumber)

        // Then
        coVerify(exactly = 1) {
            tvShowsApiService.getEpisodesBySeasonNumber(
                tvShowId,
                seasonNumber
            )
        }
        assertThat(result).isEqualTo(dummyResponse)
    }
}
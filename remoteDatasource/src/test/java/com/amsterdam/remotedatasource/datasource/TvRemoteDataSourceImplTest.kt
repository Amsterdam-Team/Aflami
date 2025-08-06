package com.amsterdam.remotedatasource.datasource

import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.remotedatasource.api.TvShowsApiService
import com.amsterdam.remotedatasource.util.episodeResponse
import com.amsterdam.remotedatasource.util.remoteTvShowDetailsResponse
import com.amsterdam.remotedatasource.util.remoteTvShowItemDto
import com.amsterdam.remotedatasource.util.videoDto
import com.amsterdam.repository.dto.remote.RatingResponse
import com.amsterdam.repository.dto.remote.RemoteCastAndCrewResponse
import com.amsterdam.repository.dto.remote.RemoteTvShowResponse
import com.amsterdam.repository.dto.remote.VideoResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TvRemoteDataSourceImplTest {

    private lateinit var tvShowsApiService: TvShowsApiService
    private lateinit var tvRemoteDataSourceImpl: TvRemoteDataSourceImpl

    @BeforeEach
    fun setUp() {
        tvShowsApiService = mockk()
        tvRemoteDataSourceImpl = TvRemoteDataSourceImpl(tvShowsApiService)
    }

    @Test
    fun `getPopularTvShows should return a list of TV shows when successful`() = runTest {
        // Given
        val expectedResponse = RemoteTvShowResponse(
            page = 1,
            results = listOf(
                remoteTvShowItemDto
            ),
            totalPages = 1,
            totalResults = 1
        )
        coEvery { tvShowsApiService.getPopularTvShows() } returns expectedResponse

        // When
        val popularTvShows = tvRemoteDataSourceImpl.getPopularTvShows()

        // Then
        assertThat(popularTvShows).isEqualTo(expectedResponse)
        coVerify(exactly = 1) { tvShowsApiService.getPopularTvShows() }
    }

    @Test
    fun `getPopularTvShows should rethrow NetworkException from service provider`() = runTest {
        // Given
        coEvery { tvShowsApiService.getPopularTvShows() } throws NetworkException()

        // When & Then
        assertThrows<NetworkException> {
            tvRemoteDataSourceImpl.getPopularTvShows()
        }
    }

    @Test
    fun `getPopularTvShows should return an empty list when API response is empty`() = runTest {
        // Given
        val expectedResponse = RemoteTvShowResponse(
            page = 1,
            results = emptyList(),
            totalPages = 1,
            totalResults = 0
        )
        coEvery { tvShowsApiService.getPopularTvShows() } returns expectedResponse

        // When
        val popularTvShows = tvRemoteDataSourceImpl.getPopularTvShows()

        // Then
        assertThat(popularTvShows.results).isEmpty()
    }


    @Test
    fun `getTopRatedTvShows should return a list of top rated TV shows when successful`() =
        runTest {
            // Given
            val page = 1
            val expectedResponse = RemoteTvShowResponse(
                page = page,
                results = listOf(
                    remoteTvShowItemDto
                ),
                totalPages = 1,
                totalResults = 1
            )
            coEvery { tvShowsApiService.getTopRatedTvShows(page) } returns expectedResponse

            // When
            val topRatedTvShows = tvRemoteDataSourceImpl.getTopRatedTvShows(page)

            // Then
            assertThat(topRatedTvShows).isEqualTo(expectedResponse)
            coVerify(exactly = 1) { tvShowsApiService.getTopRatedTvShows(page) }
        }

    @Test
    fun `getTopRatedTvShows should rethrow NetworkException from service provider`() = runTest {
        // Given
        val page = 1
        coEvery { tvShowsApiService.getTopRatedTvShows(page) } throws NetworkException()

        // When & Then
        assertThrows<NetworkException> {
            tvRemoteDataSourceImpl.getTopRatedTvShows(page)
        }
    }


    @Test
    fun `getTvShowsByKeyword should return a list of TV shows when successful`() = runTest {
        // Given
        val keyword = "Game of Thrones"
        val page = 1
        val expectedResponse = RemoteTvShowResponse(
            page = page,
            results = listOf(
                remoteTvShowItemDto
            ),
            totalPages = 1,
            totalResults = 1
        )
        coEvery { tvShowsApiService.getTvShowsByKeyword(keyword, page) } returns expectedResponse

        // When
        val tvShows = tvRemoteDataSourceImpl.getTvShowsByKeyword(keyword, page)

        // Then
        assertThat(tvShows).isEqualTo(expectedResponse)
        coVerify(exactly = 1) { tvShowsApiService.getTvShowsByKeyword(keyword, page) }
    }

    @Test
    fun `getTvShowsByKeyword should rethrow NetworkException from service provider when exception occurs`() =
        runTest {
            // Given
            val keyword = "test"
            val page = 1
            coEvery {
                tvShowsApiService.getTvShowsByKeyword(
                    keyword,
                    page
                )
            } throws NetworkException()

            // When & Then
            assertThrows<NetworkException> {
                tvRemoteDataSourceImpl.getTvShowsByKeyword(keyword, page)
            }
        }


    @Test
    fun `getTvShowDetailsById should return detailed TV show information when successful`() =
        runTest {
            // Given
            val tvShowId = 1399L
            val expectedDetailsResponse = remoteTvShowDetailsResponse
            coEvery { tvShowsApiService.getTvShowDetailsById(tvShowId) } returns expectedDetailsResponse

            // When
            val details = tvRemoteDataSourceImpl.getTvShowDetailsById(tvShowId)

            // Then
            assertThat(details).isEqualTo(expectedDetailsResponse)
            coVerify(exactly = 1) { tvShowsApiService.getTvShowDetailsById(tvShowId) }
        }

    @Test
    fun `getTvShowDetailsById should rethrow NetworkException from service provider`() = runTest {
        // Given
        val tvShowId = 1399L
        coEvery { tvShowsApiService.getTvShowDetailsById(tvShowId) } throws NetworkException()

        // When & Then
        assertThrows<NetworkException> {
            tvRemoteDataSourceImpl.getTvShowDetailsById(tvShowId)
        }
    }

    @Test
    fun `getTvShowCast should return cast and crew for a TV show when successful`() = runTest {
        // Given
        val tvShowId = 1399L
        val expectedCastAndCrewResponse = RemoteCastAndCrewResponse(
            id = tvShowId,
            cast = emptyList(),
            crew = emptyList()
        )
        coEvery { tvShowsApiService.getTvShowCast(tvShowId) } returns expectedCastAndCrewResponse

        // When
        val castAndCrew = tvRemoteDataSourceImpl.getTvShowCast(tvShowId)

        // Then
        assertThat(castAndCrew).isEqualTo(expectedCastAndCrewResponse)
        coVerify(exactly = 1) { tvShowsApiService.getTvShowCast(tvShowId) }
    }

    @Test
    fun `getTvShowCast should rethrow NetworkException from service provider`() = runTest {
        // Given
        val tvShowId = 1399L
        coEvery { tvShowsApiService.getTvShowCast(tvShowId) } throws NetworkException()

        // When & Then
        assertThrows<NetworkException> {
            tvRemoteDataSourceImpl.getTvShowCast(tvShowId)
        }
    }

    @Test
    fun `getEpisodesBySeasonNumber should return a list of episodes when successful`() = runTest {
        // Given
        val tvShowId = 1399L
        val seasonNumber = 1
        val expectedResponse = episodeResponse
        coEvery {
            tvShowsApiService.getEpisodesBySeasonNumber(
                tvShowId,
                seasonNumber
            )
        } returns expectedResponse

        // When
        val episodes = tvRemoteDataSourceImpl.getEpisodesBySeasonNumber(tvShowId, seasonNumber)

        // Then
        assertThat(episodes).isEqualTo(expectedResponse)
        coVerify(exactly = 1) {
            tvShowsApiService.getEpisodesBySeasonNumber(
                tvShowId,
                seasonNumber
            )
        }
    }

    @Test
    fun `getEpisodesBySeasonNumber should rethrow NetworkException from service provider`() =
        runTest {
            // Given
            val tvShowId = 1399L
            val seasonNumber = 1
            coEvery {
                tvShowsApiService.getEpisodesBySeasonNumber(
                    tvShowId,
                    seasonNumber
                )
            } throws NetworkException()

            // When & Then
            assertThrows<NetworkException> {
                tvRemoteDataSourceImpl.getEpisodesBySeasonNumber(tvShowId, seasonNumber)
            }
        }

    @Test
    fun `getEpisodeVideos should return a VideoResponse object when successful`() = runTest {
        // Given
        val tvShowId = 1399L
        val seasonNumber = 1
        val episodeNumber = 1
        val expectedResponse = VideoResponse(
            results = listOf(
                videoDto
            )
        )
        coEvery {
            tvShowsApiService.getEpisodeVideos(
                tvShowId,
                seasonNumber,
                episodeNumber
            )
        } returns expectedResponse

        // When
        val videoResponse = tvRemoteDataSourceImpl.getEpisodeVideos(
            tvShowId,
            seasonNumber,
            episodeNumber
        )

        // Then
        assertThat(videoResponse).isEqualTo(expectedResponse)
        coVerify(exactly = 1) {
            tvShowsApiService.getEpisodeVideos(
                tvShowId,
                seasonNumber,
                episodeNumber
            )
        }

    }

    @Test
    fun `setTvShowRate should return RatingResponse when API call is successful`() = runTest {
        // Given
        val tvShowId = 123L
        val sessionId = "mock_session"
        val rate = 8
        val expectedResponse = RatingResponse(statusCode = 1, statusMessage = "Success")

        coEvery {
            tvShowsApiService.postTvRating(tvShowId, rate.toFloat(), sessionId)
        } returns expectedResponse

        // When
        val result = tvRemoteDataSourceImpl.setTvShowRate(
            rate = rate,
            tvShowId = tvShowId,
            sessionId = sessionId
        )

        // Then
        assertThat(result).isEqualTo(expectedResponse)
        coVerify(exactly = 1) {
            tvShowsApiService.postTvRating(tvShowId, rate.toFloat(), sessionId)
        }
    }


    @Test
    fun `getTvShowRated should return rating for a TV show`() = runTest {
        //Given
        val sessionId = "session_id"

        val expectedResponse = RemoteTvShowResponse(
            page = 1,
            results = listOf(
                remoteTvShowItemDto
            ),
            totalPages = 1,
            totalResults = 1
        )
        coEvery { tvShowsApiService.getRatedTvShows(0, sessionId) } returns expectedResponse
        //When
        val ratingResponse = tvRemoteDataSourceImpl.getRatedTvShows(sessionId)
        //Then
        assertThat(ratingResponse).isEqualTo(expectedResponse)
        coVerify(exactly = 1) { tvShowsApiService.getRatedTvShows(0, sessionId) }

    }

    @Test
    fun `deleteTvShowRate should call deleteTvRating API with correct parameters`() = runTest {
        // Given
        val tvShowId = 1399L
        val sessionId = "session_id"
        val expectedResponse = RatingResponse(
            statusCode = 200,
            statusMessage = "Success"
        )
        coEvery {
            tvShowsApiService.deleteTvRating(tvId = tvShowId, sessionId = sessionId)
        } returns expectedResponse

        // When
        tvRemoteDataSourceImpl.deleteTvShowRate(tvShowId, sessionId)

        // Then
        coVerify(exactly = 1) {
            tvShowsApiService.deleteTvRating(tvId = tvShowId, sessionId = sessionId)
        }
    }

}
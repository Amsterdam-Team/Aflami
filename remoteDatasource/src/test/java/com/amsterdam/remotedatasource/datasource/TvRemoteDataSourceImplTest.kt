package com.amsterdam.remotedatasource.datasource

import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.remotedatasource.api.TvShowsApiService
import com.amsterdam.remotedatasource.util.TV_SHOW_TEST_EPISODE_NUMBER
import com.amsterdam.remotedatasource.util.TV_SHOW_TEST_GENRE_ID
import com.amsterdam.remotedatasource.util.TV_SHOW_TEST_ID
import com.amsterdam.remotedatasource.util.TV_SHOW_TEST_KEYWORD
import com.amsterdam.remotedatasource.util.TV_SHOW_TEST_PAGE
import com.amsterdam.remotedatasource.util.TV_SHOW_TEST_RATING
import com.amsterdam.remotedatasource.util.TV_SHOW_TEST_SEASON_NUMBER
import com.amsterdam.remotedatasource.util.episodeRemoteResponse
import com.amsterdam.remotedatasource.util.tvShowCastAndCrewRemoteResponse
import com.amsterdam.remotedatasource.util.tvShowDetailsRemoteResponse
import com.amsterdam.remotedatasource.util.tvShowItemRemoteDto
import com.amsterdam.remotedatasource.util.tvShowRatingRemoteResponse
import com.amsterdam.remotedatasource.util.tvShowVideoRemoteResponse
import com.amsterdam.repository.dto.remote.TvShowRemoteResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TvRemoteDataSourceImplTest {

    private val tvShowsApiService: TvShowsApiService = mockk()
    private val tvRemoteDataSourceImpl: TvRemoteDataSourceImpl =
        TvRemoteDataSourceImpl(tvShowsApiService)

    @Test
    fun `getPopularTvShows should return a list of TV shows and call the API exactly once when successful`() =
        runTest {
            coEvery { tvShowsApiService.getPopularTvShows() } returns validTvShowResponse

            val popularTvShows = tvRemoteDataSourceImpl.getPopularTvShows()

            assertThat(popularTvShows).isEqualTo(validTvShowResponse)
            coVerify(exactly = 1) { tvShowsApiService.getPopularTvShows() }
        }

    @Test
    fun `getPopularTvShows should rethrow a NetworkException and call the API exactly once when the service provider throws one`() =
        runTest {
            coEvery { tvShowsApiService.getPopularTvShows() } throws networkException

            assertThrows<NetworkException> {
                tvRemoteDataSourceImpl.getPopularTvShows()
            }
            coVerify(exactly = 1) { tvShowsApiService.getPopularTvShows() }
        }

    @Test
    fun `getPopularTvShows should return an empty list and call the API exactly once when the API response is empty`() =
        runTest {
            coEvery { tvShowsApiService.getPopularTvShows() } returns emptyTvShowResponse

            val popularTvShows = tvRemoteDataSourceImpl.getPopularTvShows()

            assertThat(popularTvShows.results).isEmpty()
            coVerify(exactly = 1) { tvShowsApiService.getPopularTvShows() }
        }

    @Test
    fun `getTopRatedTvShows should return a list of top rated TV shows and call the API exactly once when successful`() =
        runTest {
            coEvery { tvShowsApiService.getTopRatedTvShows(TV_SHOW_TEST_PAGE) } returns validTvShowResponse

            val topRatedTvShows = tvRemoteDataSourceImpl.getTopRatedTvShows(TV_SHOW_TEST_PAGE)

            assertThat(topRatedTvShows).isEqualTo(validTvShowResponse)
            coVerify(exactly = 1) { tvShowsApiService.getTopRatedTvShows(TV_SHOW_TEST_PAGE) }
        }

    @Test
    fun `getTopRatedTvShows should rethrow a NetworkException and call the API exactly once when the service provider throws one`() =
        runTest {
            coEvery { tvShowsApiService.getTopRatedTvShows(TV_SHOW_TEST_PAGE) } throws networkException

            assertThrows<NetworkException> {
                tvRemoteDataSourceImpl.getTopRatedTvShows(TV_SHOW_TEST_PAGE)
            }
            coVerify(exactly = 1) { tvShowsApiService.getTopRatedTvShows(TV_SHOW_TEST_PAGE) }
        }

    @Test
    fun `getTvShowsByKeyword should return a list of TV shows and call the API exactly once when successful`() =
        runTest {
            coEvery {
                tvShowsApiService.getTvShowsByKeyword(
                    TV_SHOW_TEST_KEYWORD,
                    TV_SHOW_TEST_PAGE
                )
            } returns validTvShowResponse

            val tvShows =
                tvRemoteDataSourceImpl.getTvShowsByKeyword(TV_SHOW_TEST_KEYWORD, TV_SHOW_TEST_PAGE)

            assertThat(tvShows).isEqualTo(validTvShowResponse)
            coVerify(exactly = 1) {
                tvShowsApiService.getTvShowsByKeyword(
                    TV_SHOW_TEST_KEYWORD,
                    TV_SHOW_TEST_PAGE
                )
            }
        }

    @Test
    fun `getTvShowsByKeyword should rethrow a NetworkException and call the API exactly once when the service provider throws one`() =
        runTest {
            coEvery {
                tvShowsApiService.getTvShowsByKeyword(
                    TV_SHOW_TEST_KEYWORD,
                    TV_SHOW_TEST_PAGE
                )
            } throws networkException

            assertThrows<NetworkException> {
                tvRemoteDataSourceImpl.getTvShowsByKeyword(TV_SHOW_TEST_KEYWORD, TV_SHOW_TEST_PAGE)
            }
            coVerify(exactly = 1) {
                tvShowsApiService.getTvShowsByKeyword(
                    TV_SHOW_TEST_KEYWORD,
                    TV_SHOW_TEST_PAGE
                )
            }
        }

    @Test
    fun `getTvShowDetailsById should return detailed TV show information and call the API exactly once when successful`() =
        runTest {
            coEvery { tvShowsApiService.getTvShowDetailsById(TV_SHOW_TEST_ID) } returns tvShowDetailsRemoteResponse

            val details = tvRemoteDataSourceImpl.getTvShowDetailsById(TV_SHOW_TEST_ID)

            assertThat(details).isEqualTo(tvShowDetailsRemoteResponse)
            coVerify(exactly = 1) { tvShowsApiService.getTvShowDetailsById(TV_SHOW_TEST_ID) }
        }

    @Test
    fun `getTvShowDetailsById should rethrow a NetworkException and call the API exactly once when the service provider throws one`() =
        runTest {
            coEvery { tvShowsApiService.getTvShowDetailsById(TV_SHOW_TEST_ID) } throws networkException

            assertThrows<NetworkException> {
                tvRemoteDataSourceImpl.getTvShowDetailsById(TV_SHOW_TEST_ID)
            }
            coVerify(exactly = 1) { tvShowsApiService.getTvShowDetailsById(TV_SHOW_TEST_ID) }
        }

    @Test
    fun `getTvShowCast should return cast and crew for a TV show and call the API exactly once when successful`() =
        runTest {
            coEvery { tvShowsApiService.getTvShowCast(TV_SHOW_TEST_ID) } returns tvShowCastAndCrewRemoteResponse

            val castAndCrew = tvRemoteDataSourceImpl.getTvShowCast(TV_SHOW_TEST_ID)

            assertThat(castAndCrew).isEqualTo(tvShowCastAndCrewRemoteResponse)
            coVerify(exactly = 1) { tvShowsApiService.getTvShowCast(TV_SHOW_TEST_ID) }
        }

    @Test
    fun `getTvShowCast should rethrow a NetworkException and call the API exactly once when the service provider throws one`() =
        runTest {
            coEvery { tvShowsApiService.getTvShowCast(TV_SHOW_TEST_ID) } throws networkException

            assertThrows<NetworkException> {
                tvRemoteDataSourceImpl.getTvShowCast(TV_SHOW_TEST_ID)
            }
            coVerify(exactly = 1) { tvShowsApiService.getTvShowCast(TV_SHOW_TEST_ID) }
        }

    @Test
    fun `getEpisodesBySeasonNumber should return a list of episodes and call the API exactly once when successful`() =
        runTest {
            coEvery {
                tvShowsApiService.getEpisodesBySeasonNumber(
                    TV_SHOW_TEST_ID,
                    TV_SHOW_TEST_SEASON_NUMBER
                )
            } returns episodeRemoteResponse

            val episodes = tvRemoteDataSourceImpl.getEpisodesBySeasonNumber(
                TV_SHOW_TEST_ID,
                TV_SHOW_TEST_SEASON_NUMBER
            )

            assertThat(episodes).isEqualTo(episodeRemoteResponse)
            coVerify(exactly = 1) {
                tvShowsApiService.getEpisodesBySeasonNumber(
                    TV_SHOW_TEST_ID,
                    TV_SHOW_TEST_SEASON_NUMBER
                )
            }
        }

    @Test
    fun `getEpisodesBySeasonNumber should rethrow a NetworkException and call the API exactly once when the service provider throws one`() =
        runTest {
            coEvery {
                tvShowsApiService.getEpisodesBySeasonNumber(
                    TV_SHOW_TEST_ID,
                    TV_SHOW_TEST_SEASON_NUMBER
                )
            } throws networkException

            assertThrows<NetworkException> {
                tvRemoteDataSourceImpl.getEpisodesBySeasonNumber(
                    TV_SHOW_TEST_ID,
                    TV_SHOW_TEST_SEASON_NUMBER
                )
            }
            coVerify(exactly = 1) {
                tvShowsApiService.getEpisodesBySeasonNumber(
                    TV_SHOW_TEST_ID,
                    TV_SHOW_TEST_SEASON_NUMBER
                )
            }
        }

    @Test
    fun `getEpisodeVideos should return a VideoResponse object and call the API exactly once when successful`() =
        runTest {
            coEvery {
                tvShowsApiService.getEpisodeVideos(
                    TV_SHOW_TEST_ID,
                    TV_SHOW_TEST_SEASON_NUMBER,
                    TV_SHOW_TEST_EPISODE_NUMBER
                )
            } returns tvShowVideoRemoteResponse

            val videoResponse = tvRemoteDataSourceImpl.getEpisodeVideos(
                TV_SHOW_TEST_ID,
                TV_SHOW_TEST_SEASON_NUMBER,
                TV_SHOW_TEST_EPISODE_NUMBER
            )

            assertThat(videoResponse).isEqualTo(tvShowVideoRemoteResponse)
            coVerify(exactly = 1) {
                tvShowsApiService.getEpisodeVideos(
                    TV_SHOW_TEST_ID,
                    TV_SHOW_TEST_SEASON_NUMBER,
                    TV_SHOW_TEST_EPISODE_NUMBER
                )
            }
        }

    @Test
    fun `getEpisodeVideos should return an empty list and call the API exactly once when the API response is empty`() =
        runTest {
            coEvery {
                tvShowsApiService.getEpisodeVideos(
                    TV_SHOW_TEST_ID,
                    TV_SHOW_TEST_SEASON_NUMBER,
                    TV_SHOW_TEST_EPISODE_NUMBER
                )
            } returns tvShowVideoResponseWithEmptyResults

            val videoResponse = tvRemoteDataSourceImpl.getEpisodeVideos(
                TV_SHOW_TEST_ID,
                TV_SHOW_TEST_SEASON_NUMBER,
                TV_SHOW_TEST_EPISODE_NUMBER
            )

            assertThat(videoResponse.results).isEmpty()
            coVerify(exactly = 1) {
                tvShowsApiService.getEpisodeVideos(
                    TV_SHOW_TEST_ID,
                    TV_SHOW_TEST_SEASON_NUMBER,
                    TV_SHOW_TEST_EPISODE_NUMBER
                )
            }
        }

    @Test
    fun `setTvShowRate should return a RatingResponse and call the API exactly once when successful`() =
        runTest {
            coEvery {
                tvShowsApiService.postTvRating(
                    TV_SHOW_TEST_ID,
                    rateInt.toFloat(),
                )
            } returns tvShowRatingRemoteResponse

            val result = tvRemoteDataSourceImpl.setTvShowRate(
                rate = rateInt,
                tvShowId = TV_SHOW_TEST_ID,
            )

            assertThat(result).isEqualTo(tvShowRatingRemoteResponse)
            coVerify(exactly = 1) {
                tvShowsApiService.postTvRating(TV_SHOW_TEST_ID, rateInt.toFloat())
            }
        }

    @Test
    fun `getRatedTvShows should return rated TV shows and call the API exactly once when successful`() =
        runTest {
            coEvery { tvShowsApiService.getRatedTvShows() } returns validTvShowResponse

            val ratingResponse = tvRemoteDataSourceImpl.getRatedTvShows()

            assertThat(ratingResponse).isEqualTo(validTvShowResponse)
            coVerify(exactly = 1) { tvShowsApiService.getRatedTvShows() }
        }

    @Test
    fun `getRatedTvShows should throw NetworkException and call the API exactly once when the API call fails`() =
        runTest {
            coEvery { tvShowsApiService.getRatedTvShows() } throws networkException

            assertThrows<NetworkException> {
                tvRemoteDataSourceImpl.getRatedTvShows()
            }
            coVerify(exactly = 1) { tvShowsApiService.getRatedTvShows() }
        }

    @Test
    fun `deleteTvShowRate should call the deleteTvRating API exactly once with the correct parameters`() =
        runTest {
            coEvery {
                tvShowsApiService.deleteTvRating(tvId = TV_SHOW_TEST_ID)
            } returns tvShowRatingRemoteResponse

            tvRemoteDataSourceImpl.deleteTvShowRate(TV_SHOW_TEST_ID)

            coVerify(exactly = 1) {
                tvShowsApiService.deleteTvRating(tvId = TV_SHOW_TEST_ID)
            }
        }

    @Test
    fun `getTvShowsByGenreId should return a list of TV shows and call the API exactly once when successful`() =
        runTest {
            coEvery {
                tvShowsApiService.getTvShowsByGenreIds(
                    genreList,
                    TV_SHOW_TEST_PAGE
                )
            } returns validTvShowResponse

            val tvShows =
                tvRemoteDataSourceImpl.getTvShowsByGenreId(TV_SHOW_TEST_GENRE_ID, TV_SHOW_TEST_PAGE)

            assertThat(tvShows).isEqualTo(validTvShowResponse)
            coVerify(exactly = 1) {
                tvShowsApiService.getTvShowsByGenreIds(
                    genreList,
                    TV_SHOW_TEST_PAGE
                )
            }
        }

    @Test
    fun `getEpisodeVideos should use season 1 for invalid season numbers and call the API exactly once`() =
        runTest {
            coEvery {
                tvShowsApiService.getEpisodeVideos(
                    TV_SHOW_TEST_ID,
                    expectedSeasonNumber,
                    TV_SHOW_TEST_EPISODE_NUMBER
                )
            } returns tvShowVideoRemoteResponse

            val videoResponse = tvRemoteDataSourceImpl.getEpisodeVideos(
                TV_SHOW_TEST_ID,
                invalidSeasonNumber,
                TV_SHOW_TEST_EPISODE_NUMBER
            )

            assertThat(videoResponse).isEqualTo(tvShowVideoRemoteResponse)

            coVerify(exactly = 1) {
                tvShowsApiService.getEpisodeVideos(
                    TV_SHOW_TEST_ID,
                    expectedSeasonNumber,
                    TV_SHOW_TEST_EPISODE_NUMBER
                )
            }
        }
    private val networkException = NetworkException()
    private val validTvShowResponse = TvShowRemoteResponse(
        page = TV_SHOW_TEST_PAGE,
        results = listOf(tvShowItemRemoteDto),
        totalPages = 1,
        totalResults = 1
    )

    private val emptyTvShowResponse = TvShowRemoteResponse(
        page = 1,
        results = emptyList(),
        totalPages = 1,
        totalResults = 0
    )

    private val tvShowVideoResponseWithEmptyResults = tvShowVideoRemoteResponse.copy(results = emptyList())

    private val rateInt = TV_SHOW_TEST_RATING.toInt()
    private val genreList = listOf(TV_SHOW_TEST_GENRE_ID)
    private val invalidSeasonNumber = 0
    private val expectedSeasonNumber = 1

}
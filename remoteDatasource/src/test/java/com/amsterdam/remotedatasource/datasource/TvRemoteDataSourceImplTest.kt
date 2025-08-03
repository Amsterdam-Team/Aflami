package com.amsterdam.remotedatasource.datasource

import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.remotedatasource.api.TvShowsApiService
import com.amsterdam.repository.dto.remote.EpisodeDto
import com.amsterdam.repository.dto.remote.EpisodeResponse
import com.amsterdam.repository.dto.remote.RemoteCastAndCrewResponse
import com.amsterdam.repository.dto.remote.RemoteCategoryDto
import com.amsterdam.repository.dto.remote.RemoteTvShowItemDto
import com.amsterdam.repository.dto.remote.RemoteTvShowResponse
import com.amsterdam.repository.dto.remote.TvShowDetailsRemoteResponse
import com.amsterdam.repository.dto.remote.movieGallery.RemoteGalleryResponse
import com.amsterdam.repository.dto.remote.review.ReviewsResponse
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
                RemoteTvShowItemDto(
                    id = 1L,
                    title = "Popular Show",
                    adult = false,
                    backdropPath = null,
                    genreIds = emptyList(),
                    originCountry = emptyList(),
                    originalLanguage = "en",
                    originalTitle = "Popular Show",
                    overview = "An overview of a popular show.",
                    popularity = 500.0,
                    posterPath = "/path/to/poster.jpg",
                    releaseDate = "2024-01-01",
                    voteAverage = 8.5,
                    voteCount = 1000
                )
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
                    RemoteTvShowItemDto(
                        id = 2L,
                        title = "Top Rated Show",
                        adult = false,
                        backdropPath = null,
                        genreIds = emptyList(),
                        originCountry = emptyList(),
                        originalLanguage = "en",
                        originalTitle = "Top Rated Show",
                        overview = "An overview of a top rated show.",
                        popularity = 600.0,
                        posterPath = null,
                        releaseDate = "2023-01-01",
                        voteAverage = 9.0,
                        voteCount = 5000
                    )
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
                RemoteTvShowItemDto(
                    id = 1399L,
                    title = "Game of Thrones",
                    adult = false,
                    backdropPath = "/suopoADq0bPmYhnN8jQePVKzfdg.jpg",
                    genreIds = listOf(10765, 10759, 18),
                    originCountry = listOf("US"),
                    originalLanguage = "en",
                    originalTitle = "Game of Thrones",
                    overview = "Seven noble families...",
                    popularity = 450.0,
                    posterPath = "/2yafLgJ9jL6t7jM0W7W9Bv0qP7j.jpg",
                    releaseDate = "2011-04-17",
                    voteAverage = 8.4,
                    voteCount = 20000
                )
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
            val expectedDetailsResponse = TvShowDetailsRemoteResponse(
                id = tvShowId,
                title = "Game of Thrones",
                overview = "Seven noble families...",
                backdropPath = "/backdrop_got.jpg",
                posterPath = "/poster_got.jpg",
                seasonCount = 8,
                adult = false,
                genres = listOf(RemoteCategoryDto(id = 10765, name = "Sci-Fi & Fantasy")),
                originCountry = listOf("US"),
                originalLanguage = "en",
                originalTitle = "Game of Thrones",
                popularity = 450.0,
                releaseDate = "2011-04-17",
                voteAverage = 8.4,
                productionCompanies = emptyList(),
                reviews = ReviewsResponse(
                    id = tvShowId,
                    page = 1,
                    results = emptyList(),
                    totalPages = 1,
                    totalResults = 0
                ),
                credits = RemoteCastAndCrewResponse(
                    id = tvShowId,
                    cast = emptyList(),
                    crew = emptyList()
                ),
                similar = RemoteTvShowResponse(
                    page = 1,
                    results = emptyList(),
                    totalPages = 1,
                    totalResults = 0
                ),
                images = RemoteGalleryResponse(
                    id = tvShowId,
                    backdrops = emptyList(),
                    logos = emptyList(),
                    posters = emptyList()
                )
            )
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
        val expectedResponse = EpisodeResponse(
            id = 123L,
            episodes = listOf(
                EpisodeDto(
                    id = 456L,
                    title = "Winter is Coming",
                    seasonNumber = 1,
                    episodeNumber = 1,
                    overview = "An overview...",
                    voteAverage = 8.5,
                    runtime = "60",
                    stillPath = "/still_path.jpg",
                    airDate = "2011-04-17"
                )
            ),
            airDate = "2011-04-17",
            name = "Season 1",
            overview = "Season 1 overview",
            posterPath = null,
            seasonNumber = 1L,
            voteAverage = 8.4
        )
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
}
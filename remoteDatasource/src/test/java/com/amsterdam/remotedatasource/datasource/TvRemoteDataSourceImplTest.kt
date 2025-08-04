package com.amsterdam.remotedatasource.datasource

import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.remotedatasource.api.TvShowsApiService
import com.amsterdam.repository.dto.remote.ProductionCompanyResponse
import com.amsterdam.repository.dto.remote.RemoteCastAndCrewResponse
import com.amsterdam.repository.dto.remote.RemoteTvShowResponse
import com.amsterdam.repository.dto.remote.TvShowDetailsRemoteResponse
import com.amsterdam.repository.dto.remote.movieGallery.RemoteGalleryResponse
import com.amsterdam.repository.dto.remote.review.ReviewsResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TvRemoteDataSourceImplTest {

    private lateinit var tvShowsApiService: TvShowsApiService
    private lateinit var tvRemoteDataSourceImpl: TvRemoteDataSourceImpl

    private val jsonSerializer = Json { ignoreUnknownKeys = true }

    @BeforeEach
    fun setUp() {
        tvShowsApiService = mockk()
        tvRemoteDataSourceImpl = TvRemoteDataSourceImpl(tvShowsApiService)
    }

    @Test
    fun `getTvShowsByKeyword should return a list of TV shows when executed`() = runTest {
        val keyword = "Game of Thrones"
        val page = 1

        val jsonString = """
            {
              "page": 1,
              "results": [
                {
                  "adult": false,
                  "backdrop_path": "/suopoADq0bPmYhnN8jQePVKzfdg.jpg",
                  "genre_ids": [10765, 10759, 18],
                  "id": 1399,
                  "name": "Game of Thrones",
                  "origin_country": ["US"],
                  "original_language": "en",
                  "original_name": "Game of Thrones",
                  "overview": "Seven noble families...",
                  "popularity": 450.0,
                  "poster_path": "/2yafLgJ9jL6t7jM0W7W9Bv0qP7j.jpg",
                  "first_air_date": "2011-04-17",
                  "vote_average": 8.4,
                  "vote_count": 20000
                }
              ],
              "total_pages": 1,
              "total_results": 1
            }
        """.trimIndent()

        val expectedTvShowResponse =
            jsonSerializer.decodeFromString<RemoteTvShowResponse>(jsonString)

        coEvery {
            tvShowsApiService.getTvShowsByKeyword(keyword, page)
        } returns expectedTvShowResponse

        val tvShows =
            tvRemoteDataSourceImpl.getTvShowsByKeyword(keyword, page)

        coVerify(exactly = 1) { tvShowsApiService.getTvShowsByKeyword(keyword, page) }

        assertThat(tvShows.results).hasSize(1)
        assertThat(tvShows.results[0].title).isEqualTo("Game of Thrones")
        assertThat(tvShows.results[0].id).isEqualTo(1399)
        assertThat(tvShows.results[0].releaseDate).isEqualTo("2011-04-17")
        assertThat(tvShows.results[0].overview).isNotNull()
        assertThat(tvShows.results[0].popularity).isWithin(0.001).of(450.0)
        assertThat(tvShows.results[0].backdropPath).isEqualTo("/suopoADq0bPmYhnN8jQePVKzfdg.jpg")
        assertThat(tvShows.results[0].posterPath).isEqualTo("/2yafLgJ9jL6t7jM0W7W9Bv0qP7j.jpg")
        assertThat(tvShows.results[0].voteAverage).isWithin(0.001).of(8.4)
        assertThat(tvShows.results[0].voteCount).isEqualTo(20000)
    }

    @Test
    fun `getTvShowsByKeyword should rethrow NetworkException from service provider when exception occurs`() =
        runTest {
            val keyword = "test"
            val page = 1
            coEvery {
                tvShowsApiService.getTvShowsByKeyword(
                    keyword,
                    page
                )
            } throws NetworkException()

            assertThrows<NetworkException> {
                tvRemoteDataSourceImpl.getTvShowsByKeyword(keyword, page)
            }
        }

    @Test
    fun `getTvShowDetailsById should return detailed TV show information when executed`() =
        runTest {
            val tvShowId = 1399L
            val jsonString = """
            {
              "adult": false,
              "backdrop_path": "/backdrop_got.jpg",
              "created_by": [],
              "episode_run_times": [60],
              "first_air_date": "2011-04-17",
              "genres": [{"id": 10765, "name": "Sci-Fi & Fantasy"}],
              "homepage": "http://www.hbo.com/game-of-thrones",
              "id": 1399,
              "in_production": false,
              "languages": ["en"],
              "last_air_date": "2019-05-19",
              "last_episode_to_air": null,
              "name": "Game of Thrones",
              "next_episode_to_air": null,
              "networks": [],
              "number_of_episodes": 73,
              "number_of_seasons": 8,
              "origin_country": ["US"],
              "original_language": "en",
              "original_name": "Game of Thrones",
              "overview": "Seven noble families...",
              "popularity": 450.0,
              "poster_path": "/poster_got.jpg",
              "production_companies": [],
              "production_countries": [],
              "seasons": [],
              "spoken_languages": [],
              "status": "Ended",
              "tagline": "Winter is coming.",
              "type": "Scripted",
              "vote_average": 8.4,
              "vote_count": 20000
            }
        """.trimIndent()

            val expectedDetailsResponse =
                jsonSerializer.decodeFromString<TvShowDetailsRemoteResponse>(jsonString)

            coEvery { tvShowsApiService.getTvShowDetailsById(tvShowId) } returns expectedDetailsResponse

            val details = tvRemoteDataSourceImpl.getTvShowDetailsById(tvShowId)

            coVerify(exactly = 1) { tvShowsApiService.getTvShowDetailsById(tvShowId) }
            assertThat(details.id).isEqualTo(tvShowId)
            assertThat(details.title).isEqualTo("Game of Thrones")
            assertThat(details.seasonCount).isEqualTo(8)
        }

    @Test
    fun `getTvShowDetailsById should rethrow NetworkException from service provider`() = runTest {
        val tvShowId = 1399L
        coEvery { tvShowsApiService.getTvShowDetailsById(tvShowId) } throws NetworkException()

        assertThrows<NetworkException> {
            tvRemoteDataSourceImpl.getTvShowDetailsById(tvShowId)
        }
    }

    @Test
    fun `getTvShowCast should return cast and crew for a TV show when executed`() = runTest {
        val tvShowId = 1399L
        val jsonString = """
            {
              "id": 1399,
              "cast": [
                {
                  "adult": false,
                  "gender": 2,
                  "id": 23209,
                  "known_for_department": "Acting",
                  "name": "Peter Dinklage",
                  "original_name": "Peter Dinklage",
                  "popularity": 50.0,
                  "profile_path": "/profile_peter.jpg",
                  "character": "Tyrion Lannister",
                  "cast_id": 4,         
                  "credit_id": "52fe4250c3a36847f80149f3", 
                  "order": 0           
                }
              ],
              "crew": [
                {
                  "adult": false,
                  "gender": 2,
                  "id": 1228230,
                  "known_for_department": "Directing",
                  "name": "David Benioff",
                  "original_name": "David Benioff",
                  "popularity": 10.0,
                  "profile_path": "/profile_david.jpg",
                  "department": "Production",
                  "job": "Executive Producer",
                  "credit_id": "52fe4250c3a36847f80149c9" 
                }
              ]
            }
        """.trimIndent()

        val expectedCastAndCrewResponse =
            jsonSerializer.decodeFromString<RemoteCastAndCrewResponse>(jsonString)

        coEvery { tvShowsApiService.getTvShowCast(tvShowId) } returns expectedCastAndCrewResponse

        val castAndCrew = tvRemoteDataSourceImpl.getTvShowCast(tvShowId)

        coVerify(exactly = 1) { tvShowsApiService.getTvShowCast(tvShowId) }
        assertThat(castAndCrew.id).isEqualTo(tvShowId.toInt())
        assertThat(castAndCrew.cast).hasSize(1)
        assertThat(castAndCrew.cast[0].name).isEqualTo("Peter Dinklage")
        assertThat(castAndCrew.cast[0].character).isEqualTo("Tyrion Lannister")
        assertThat(castAndCrew.crew).hasSize(1)
        assertThat(castAndCrew.crew[0].name).isEqualTo("David Benioff")
        assertThat(castAndCrew.crew[0].job).isEqualTo("Executive Producer")
        assertThat(castAndCrew.cast[0].castId).isEqualTo(4)
        assertThat(castAndCrew.cast[0].creditId).isEqualTo("52fe4250c3a36847f80149f3")
        assertThat(castAndCrew.cast[0].order).isEqualTo(0)
        assertThat(castAndCrew.crew[0].creditId).isEqualTo("52fe4250c3a36847f80149c9")
    }

    @Test
    fun `getTvShowCast should rethrow NetworkException from service provider`() = runTest {
        val tvShowId = 1399L
        coEvery { tvShowsApiService.getTvShowCast(tvShowId) } throws NetworkException()

        assertThrows<NetworkException> {
            tvRemoteDataSourceImpl.getTvShowCast(tvShowId)
        }
    }



    @Test
    fun `getEpisodesBySeasonNumber should rethrow NetworkException from service provider`() =
        runTest {
            val tvShowId = 1399L
            val seasonNumber = 1
            coEvery {
                tvShowsApiService.getEpisodesBySeasonNumber(
                    tvShowId,
                    seasonNumber
                )
            } throws NetworkException()

            assertThrows<NetworkException> {
                tvRemoteDataSourceImpl.getEpisodesBySeasonNumber(tvShowId, seasonNumber)
            }
        }
}
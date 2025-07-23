package com.example.remotedatasource.datasource

import com.example.domain.exceptions.NetworkException
import com.example.domain.exceptions.NoInternetException
import com.example.domain.exceptions.ServerErrorException
import com.example.remotedatasource.serviceProvider.TvShowsServiceProvider
import com.example.repository.dto.remote.EpisodeResponse
import com.example.repository.dto.remote.ProductionCompanyResponse
import com.example.repository.dto.remote.RemoteCastAndCrewResponse
import com.example.repository.dto.remote.RemoteTvShowResponse
import com.example.repository.dto.remote.TvShowDetailsRemoteResponse
import com.example.repository.dto.remote.movieGallery.RemoteGalleryResponse
import com.example.repository.dto.remote.review.ReviewsResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

class TvRemoteDataSourceImplTest {

    private lateinit var tvShowsServiceProvider: TvShowsServiceProvider
    private lateinit var tvRemoteDataSourceImpl: TvRemoteDataSourceImpl

    private val jsonSerializer = Json { ignoreUnknownKeys = true }

    @Before
    fun setUp() {
        tvShowsServiceProvider = mockk()
        tvRemoteDataSourceImpl = TvRemoteDataSourceImpl(tvShowsServiceProvider)
    }

    @Test
    fun `getTvShowsByKeyword should return a list of TV shows when executed`() = runTest {
        // Given
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
            tvShowsServiceProvider.getTvShowsByKeyword(keyword, page)
        } returns expectedTvShowResponse

        // When
        val tvShows =
            tvRemoteDataSourceImpl.getTvShowsByKeyword(keyword, page)

        coVerify(exactly = 1) { tvShowsServiceProvider.getTvShowsByKeyword(keyword, page) }

        // Then
        assertEquals(1, tvShows.results.size)
        assertEquals(
            "Game of Thrones",
            tvShows.results[0].title
        )
        assertEquals(1399, tvShows.results[0].id)
        assertEquals(
            "2011-04-17",
            tvShows.results[0].releaseDate
        )
        assertNotNull(tvShows.results[0].overview)
        assertEquals(450.0, tvShows.results[0].popularity, 0.001)
        assertEquals("/suopoADq0bPmYhnN8jQePVKzfdg.jpg", tvShows.results[0].backdropPath)
        assertEquals("/2yafLgJ9jL6t7jM0W7W9Bv0qP7j.jpg", tvShows.results[0].posterPath)
        assertEquals(8.4, tvShows.results[0].voteAverage, 0.001)
        assertEquals(20000, tvShows.results[0].voteCount)
    }

    @Test
    fun `getTvShowsByKeyword should rethrow ServerErrorException from service provider when exception occurs`() =
        runTest {
            // Given
            val keyword = "test"
            val page = 1
            coEvery {
                tvShowsServiceProvider.getTvShowsByKeyword(
                    keyword,
                    page
                )
            } throws ServerErrorException()

            // When & Then
            assertFailsWith<ServerErrorException> {
                tvRemoteDataSourceImpl.getTvShowsByKeyword(keyword, page)
            }
        }

    @Test
    fun `getTvShowsByKeyword should rethrow NoInternetException from service provider when exception occurs`() =
        runTest {
            // Given
            val keyword = "test"
            val page = 1
            coEvery {
                tvShowsServiceProvider.getTvShowsByKeyword(
                    keyword,
                    page
                )
            } throws NoInternetException()

            // When & Then
            assertFailsWith<NoInternetException> {
                tvRemoteDataSourceImpl.getTvShowsByKeyword(keyword, page)
            }
        }

    @Test
    fun `getTvShowsByKeyword should rethrow NetworkException from service provider when exception occurs`() =
        runTest {
            // Given
            val keyword = "test"
            val page = 1
            coEvery {
                tvShowsServiceProvider.getTvShowsByKeyword(
                    keyword,
                    page
                )
            } throws NetworkException()

            // When & Then
            assertFailsWith<NetworkException> {
                tvRemoteDataSourceImpl.getTvShowsByKeyword(keyword, page)
            }
        }

    @Test
    fun `getTvShowDetailsById should return detailed TV show information when executed`() =
        runTest {
            // Given
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

            coEvery { tvShowsServiceProvider.getTvShowDetailsById(tvShowId) } returns expectedDetailsResponse

            // When
            val details = tvRemoteDataSourceImpl.getTvShowDetailsById(tvShowId)

            // Then
            coVerify(exactly = 1) { tvShowsServiceProvider.getTvShowDetailsById(tvShowId) }
            assertEquals(tvShowId, details.id)
            assertEquals("Game of Thrones", details.title)
            assertEquals(8, details.seasonCount)
        }

    @Test
    fun `getTvShowDetailsById should rethrow ServerErrorException from service provider`() =
        runTest {
            // Given
            val tvShowId = 1399L
            coEvery { tvShowsServiceProvider.getTvShowDetailsById(tvShowId) } throws ServerErrorException()

            // When & Then
            assertFailsWith<ServerErrorException> {
                tvRemoteDataSourceImpl.getTvShowDetailsById(tvShowId)
            }
        }

    @Test
    fun `getTvShowDetailsById should rethrow NoInternetException from service provider`() =
        runTest {
            // Given
            val tvShowId = 1399L
            coEvery { tvShowsServiceProvider.getTvShowDetailsById(tvShowId) } throws NoInternetException()

            // When & Then
            assertFailsWith<NoInternetException> {
                tvRemoteDataSourceImpl.getTvShowDetailsById(tvShowId)
            }
        }

    @Test
    fun `getTvShowDetailsById should rethrow NetworkException from service provider`() = runTest {
        // Given
        val tvShowId = 1399L
        coEvery { tvShowsServiceProvider.getTvShowDetailsById(tvShowId) } throws NetworkException()

        // When & Then
        assertFailsWith<NetworkException> {
            tvRemoteDataSourceImpl.getTvShowDetailsById(tvShowId)
        }
    }

    @Test
    fun `getTvShowCast should return cast and crew for a TV show when executed`() = runTest {
        // Given
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

        coEvery { tvShowsServiceProvider.getTvShowCast(tvShowId) } returns expectedCastAndCrewResponse

        // When
        val castAndCrew = tvRemoteDataSourceImpl.getTvShowCast(tvShowId)

        // Then
        coVerify(exactly = 1) { tvShowsServiceProvider.getTvShowCast(tvShowId) }
        assertEquals(
            tvShowId.toInt(),
            castAndCrew.id
        )
        assertEquals(1, castAndCrew.cast.size)
        assertEquals("Peter Dinklage", castAndCrew.cast[0].name)
        assertEquals("Tyrion Lannister", castAndCrew.cast[0].character)
        assertEquals(1, castAndCrew.crew.size)
        assertEquals("David Benioff", castAndCrew.crew[0].name)
        assertEquals("Executive Producer", castAndCrew.crew[0].job)
        assertEquals(4, castAndCrew.cast[0].castId)
        assertEquals("52fe4250c3a36847f80149f3", castAndCrew.cast[0].creditId)
        assertEquals(0, castAndCrew.cast[0].order)
        assertEquals("52fe4250c3a36847f80149c9", castAndCrew.crew[0].creditId)
    }

    @Test
    fun `getTvShowCast should rethrow ServerErrorException from service provider`() = runTest {
        // Given
        val tvShowId = 1399L
        coEvery { tvShowsServiceProvider.getTvShowCast(tvShowId) } throws ServerErrorException()

        // When & Then
        assertFailsWith<ServerErrorException> {
            tvRemoteDataSourceImpl.getTvShowCast(tvShowId)
        }
    }

    @Test
    fun `getTvShowCast should rethrow NoInternetException from service provider`() = runTest {
        // Given
        val tvShowId = 1399L
        coEvery { tvShowsServiceProvider.getTvShowCast(tvShowId) } throws NoInternetException()

        // When & Then
        assertFailsWith<NoInternetException> {
            tvRemoteDataSourceImpl.getTvShowCast(tvShowId)
        }
    }

    @Test
    fun `getTvShowCast should rethrow NetworkException from service provider`() = runTest {
        // Given
        val tvShowId = 1399L
        coEvery { tvShowsServiceProvider.getTvShowCast(tvShowId) } throws NetworkException()

        // When & Then
        assertFailsWith<NetworkException> {
            tvRemoteDataSourceImpl.getTvShowCast(tvShowId)
        }
    }

    @Test
    fun `getSimilarTvShows should return similar TV shows when executed`() = runTest {
        // Given
        val tvShowId = 1399L

        val jsonString = """
            {
              "page": 1,
              "results": [
                {
                  "adult": false,
                  "backdrop_path": "/similar_show_backdrop.jpg",
                  "genre_ids": [18, 10765],
                  "id": 1400,
                  "name": "House of the Dragon",
                  "overview": "Prequel to Game of Thrones.",
                  "popularity": 300.0,
                  "poster_path": "/similar_show_poster.jpg",
                  "first_air_date": "2022-08-21",
                  "vote_average": 8.5,
                  "vote_count": 10000,
                  "origin_country": ["US"],      
                  "original_language": "en",      
                  "original_name": "House of the Dragon" 
                }
              ],
              "total_pages": 1,
              "total_results": 1
            }
        """.trimIndent()

        val expectedSimilarTvShowResponse =
            jsonSerializer.decodeFromString<RemoteTvShowResponse>(jsonString)

        coEvery { tvShowsServiceProvider.getSimilarTvShows(tvShowId) } returns expectedSimilarTvShowResponse

        // When
        val similarTvShows = tvRemoteDataSourceImpl.getSimilarTvShows(tvShowId)

        // Then
        coVerify(exactly = 1) { tvShowsServiceProvider.getSimilarTvShows(tvShowId) }
        assertEquals(1, similarTvShows.results.size)
        assertEquals("House of the Dragon", similarTvShows.results[0].title)
        assertEquals(1400, similarTvShows.results[0].id)
    }

    @Test
    fun `getSimilarTvShows should rethrow ServerErrorException from service provider`() = runTest {
        // Given
        val tvShowId = 1399L
        coEvery { tvShowsServiceProvider.getSimilarTvShows(tvShowId) } throws ServerErrorException()

        // When & Then
        assertFailsWith<ServerErrorException> {
            tvRemoteDataSourceImpl.getSimilarTvShows(tvShowId)
        }
    }

    @Test
    fun `getSimilarTvShows should rethrow NoInternetException from service provider`() = runTest {
        // Given
        val tvShowId = 1399L
        coEvery { tvShowsServiceProvider.getSimilarTvShows(tvShowId) } throws NoInternetException()

        // When & Then
        assertFailsWith<NoInternetException> {
            tvRemoteDataSourceImpl.getSimilarTvShows(tvShowId)
        }
    }

    @Test
    fun `getSimilarTvShows should rethrow NetworkException from service provider`() = runTest {
        // Given
        val tvShowId = 1399L
        coEvery { tvShowsServiceProvider.getSimilarTvShows(tvShowId) } throws NetworkException()

        // When & Then
        assertFailsWith<NetworkException> {
            tvRemoteDataSourceImpl.getSimilarTvShows(tvShowId)
        }
    }

    @Test
    fun `getTvShowReviews should return reviews for a TV show when executed`() = runTest {
        // Given
        val tvShowId = 1399L
        val jsonString = """
            {
              "id": 1399,
              "page": 1,
              "results": [
                {
                  "author": "Reviewer A",
                  "author_details": {
                    "name": "Reviewer A",
                    "username": "reviewer_a",
                    "avatar_path": null,
                    "rating": 9.0
                  },
                  "content": "An epic series with incredible depth.",
                  "created_at": "2023-01-10T00:00:00.000Z",
                  "id": "review123",
                  "updated_at": "2023-01-10T00:00:00.000Z",
                  "url": "http://example.com/review123"
                }
              ],
              "total_pages": 1,
              "total_results": 1
            }
        """.trimIndent()

        val expectedReviewsResponse = jsonSerializer.decodeFromString<ReviewsResponse>(jsonString)

        coEvery { tvShowsServiceProvider.getTvShowReviews(tvShowId) } returns expectedReviewsResponse

        // When
        val reviews = tvRemoteDataSourceImpl.getTvShowReviews(tvShowId)

        // Then
        coVerify(exactly = 1) { tvShowsServiceProvider.getTvShowReviews(tvShowId) }
        assertEquals(tvShowId, reviews.id)
        assertEquals(1, reviews.results.size)
        assertEquals("Reviewer A", reviews.results[0].author)
        assertEquals("An epic series with incredible depth.", reviews.results[0].content)
    }

    @Test
    fun `getTvShowReviews should rethrow ServerErrorException from service provider`() = runTest {
        // Given
        val tvShowId = 1399L
        coEvery { tvShowsServiceProvider.getTvShowReviews(tvShowId) } throws ServerErrorException()

        // When & Then
        assertFailsWith<ServerErrorException> {
            tvRemoteDataSourceImpl.getTvShowReviews(tvShowId)
        }
    }

    @Test
    fun `getTvShowReviews should rethrow NoInternetException from service provider`() = runTest {
        // Given
        val tvShowId = 1399L
        coEvery { tvShowsServiceProvider.getTvShowReviews(tvShowId) } throws NoInternetException()

        // When & Then
        assertFailsWith<NoInternetException> {
            tvRemoteDataSourceImpl.getTvShowReviews(tvShowId)
        }
    }

    @Test
    fun `getTvShowReviews should rethrow NetworkException from service provider`() = runTest {
        // Given
        val tvShowId = 1399L
        coEvery { tvShowsServiceProvider.getTvShowReviews(tvShowId) } throws NetworkException()

        // When & Then
        assertFailsWith<NetworkException> {
            tvRemoteDataSourceImpl.getTvShowReviews(tvShowId)
        }
    }

    @Test
    fun `getTvShowGallery should return TV show images when executed`() = runTest {
        // Given
        val tvShowId = 1399L
        val jsonString = """
            {
              "id": 1399,
              "backdrops": [
                {"aspect_ratio": 1.778, "file_path": "/got_backdrop1.jpg", "height": 1080, "iso_639_1": null, "vote_average": 5.0, "vote_count": 1, "width": 1920}
              ],
              "logos": [],
              "posters": [
                {"aspect_ratio": 0.667, "file_path": "/got_poster1.jpg", "height": 900, "iso_639_1": null, "vote_average": 5.0, "vote_count": 1, "width": 600}
              ]
            }
        """.trimIndent()

        val expectedGalleryResponse =
            jsonSerializer.decodeFromString<RemoteGalleryResponse>(jsonString)

        coEvery { tvShowsServiceProvider.getTvShowGallery(tvShowId) } returns expectedGalleryResponse

        // When
        val gallery = tvRemoteDataSourceImpl.getTvShowGallery(tvShowId)

        // Then
        coVerify(exactly = 1) { tvShowsServiceProvider.getTvShowGallery(tvShowId) }
        assertEquals(tvShowId, gallery.id)
        assertEquals(1, gallery.backdrops?.size)
        assertEquals(1, gallery.posters?.size)
        assertEquals("/got_backdrop1.jpg", gallery.backdrops?.get(0)?.filePath)
        assertEquals("/got_poster1.jpg", gallery.posters?.get(0)?.filePath)
    }

    @Test
    fun `getTvShowGallery should rethrow ServerErrorException from service provider`() = runTest {
        // Given
        val tvShowId = 1399L
        coEvery { tvShowsServiceProvider.getTvShowGallery(tvShowId) } throws ServerErrorException()

        // When & Then
        assertFailsWith<ServerErrorException> {
            tvRemoteDataSourceImpl.getTvShowGallery(tvShowId)
        }
    }

    @Test
    fun `getTvShowGallery should rethrow NoInternetException from service provider`() = runTest {
        // Given
        val tvShowId = 1399L
        coEvery { tvShowsServiceProvider.getTvShowGallery(tvShowId) } throws NoInternetException()

        // When & Then
        assertFailsWith<NoInternetException> {
            tvRemoteDataSourceImpl.getTvShowGallery(tvShowId)
        }
    }

    @Test
    fun `getTvShowGallery should rethrow NetworkException from service provider`() = runTest {
        // Given
        val tvShowId = 1399L
        coEvery { tvShowsServiceProvider.getTvShowGallery(tvShowId) } throws NetworkException()

        // When & Then
        assertFailsWith<NetworkException> {
            tvRemoteDataSourceImpl.getTvShowGallery(tvShowId)
        }
    }

    @Test
    fun `getTvShowCompanyProduction should return production company details for a TV show when executed`() =
        runTest {
            // Given
            val tvShowId = 1399L
            val jsonString = """
            {
              "adult": false,
              "backdrop_path": "/backdrop_got.jpg",
              "created_by": [],
              "episode_run_times": [60],
              "first_air_date": "2011-04-17",
              "genres": [],
              "homepage": "",
              "id": 1399,
              "in_production": false,
              "languages": [],
              "last_air_date": "2019-05-19",
              "last_episode_to_air": null,
              "name": "Game of Thrones",
              "next_episode_to_air": null,
              "networks": [],
              "number_of_episodes": 73,
              "number_of_seasons": 8,
              "origin_country": [],
              "original_language": "en",
              "original_name": "Game of Thrones",
              "overview": "",
              "popularity": 0.0,
              "poster_path": "",
              "production_companies": [
                {
                  "id": 14902,
                  "logo_path": "/production_company_hbo_logo.png",
                  "name": "HBO",
                  "origin_country": "US"
                }
              ],
              "production_countries": [],
              "seasons": [],
              "spoken_languages": [],
              "status": "Ended",
              "tagline": "",
              "type": "Scripted",
              "vote_average": 0.0,
              "vote_count": 0
            }
        """.trimIndent()

            val expectedProductionCompanyResponse =
                jsonSerializer.decodeFromString<ProductionCompanyResponse>(jsonString)

            coEvery { tvShowsServiceProvider.getTvShowCompanyProduction(tvShowId) } returns expectedProductionCompanyResponse

            // When
            val productionCompany = tvRemoteDataSourceImpl.getTvShowCompanyProduction(tvShowId)

            // Then
            coVerify(exactly = 1) { tvShowsServiceProvider.getTvShowCompanyProduction(tvShowId) }
            assertEquals(1, productionCompany.productionCompanies.size)
            assertEquals("HBO", productionCompany.productionCompanies[0].name)
            assertEquals(14902, productionCompany.productionCompanies[0].id)
        }

    @Test
    fun `getTvShowCompanyProduction should rethrow ServerErrorException from service provider`() =
        runTest {
            // Given
            val tvShowId = 1399L
            coEvery { tvShowsServiceProvider.getTvShowCompanyProduction(tvShowId) } throws ServerErrorException()

            // When & Then
            assertFailsWith<ServerErrorException> {
                tvRemoteDataSourceImpl.getTvShowCompanyProduction(tvShowId)
            }
        }

    @Test
    fun `getTvShowCompanyProduction should rethrow NoInternetException from service provider`() =
        runTest {
            // Given
            val tvShowId = 1399L
            coEvery { tvShowsServiceProvider.getTvShowCompanyProduction(tvShowId) } throws NoInternetException()

            // When & Then
            assertFailsWith<NoInternetException> {
                tvRemoteDataSourceImpl.getTvShowCompanyProduction(tvShowId)
            }
        }

    @Test
    fun `getTvShowCompanyProduction should rethrow NetworkException from service provider`() =
        runTest {
            // Given
            val tvShowId = 1399L
            coEvery { tvShowsServiceProvider.getTvShowCompanyProduction(tvShowId) } throws NetworkException()

            // When & Then
            assertFailsWith<NetworkException> {
                tvRemoteDataSourceImpl.getTvShowCompanyProduction(tvShowId)
            }
        }

    @Test
    fun `getEpisodesBySeasonNumber should return episodes for a given season when executed`() =
        runTest {
            // Given
            val tvShowId = 1399L
            val seasonNumber = 1
            val expectedSeasonId = 3624L

            val jsonString = """
            {
              "_id": "525381f119c2956f6702d763",
              "air_date": "2011-04-17",
              "episodes": [
                {
                  "air_date": "2011-04-17",
                  "episode_number": 1,
                  "id": 63056,
                  "name": "Winter Is Coming",
                  "overview": "Ned Stark, Lord of Winterfell, is disturbed...",
                  "production_code": "",
                  "runtime": "62",
                  "season_number": 1,
                  "show_id": 1399,
                  "still_path": "/still_ep1.jpg",
                  "vote_average": "8.0",
                  "vote_count": 200
                },
                {
                  "air_date": "2011-04-24",
                  "episode_number": 2,
                  "id": 63057,
                  "name": "The Kingsroad",
                  "overview": "Bran's fate remains uncertain...",
                  "production_code": "",
                  "runtime": "56",
                  "season_number": 1,
                  "show_id": 1399,
                  "still_path": "/still_ep2.jpg",
                  "vote_average": "8.1",
                  "vote_count": 180
                }
              ],
              "name": "Season 1",
              "overview": "Trouble is brewing...",
              "id": $expectedSeasonId,
              "poster_path": "/poster_season1.jpg",
              "season_number": 1,
              "vote_average": 8.3
            }
        """.trimIndent()

            val expectedEpisodeResponse =
                jsonSerializer.decodeFromString<EpisodeResponse>(jsonString)

            coEvery {
                tvShowsServiceProvider.getEpisodesBySeasonNumber(
                    tvShowId,
                    seasonNumber
                )
            } returns expectedEpisodeResponse

            // When
            val episodes = tvRemoteDataSourceImpl.getEpisodesBySeasonNumber(tvShowId, seasonNumber)

            // Then
            coVerify(exactly = 1) {
                tvShowsServiceProvider.getEpisodesBySeasonNumber(
                    tvShowId,
                    seasonNumber
                )
            }

            assertEquals(expectedSeasonId, episodes.id)
            assertEquals(
                seasonNumber.toLong(),
                episodes.seasonNumber
            )

            assertEquals(2, episodes.episodes.size)
            assertEquals("Winter Is Coming", episodes.episodes[0].title)
            assertEquals(1, episodes.episodes[0].episodeNumber)
            assertEquals("62", episodes.episodes[0].runtime)
            assertEquals("8.0", episodes.episodes[0].voteAverage)

            assertEquals("The Kingsroad", episodes.episodes[1].title)
            assertEquals(2, episodes.episodes[1].episodeNumber)
            assertEquals("56", episodes.episodes[1].runtime)
            assertEquals("8.1", episodes.episodes[1].voteAverage)
        }

    @Test
    fun `getEpisodesBySeasonNumber should rethrow ServerErrorException from service provider`() =
        runTest {
            // Given
            val tvShowId = 1399L
            val seasonNumber = 1
            coEvery {
                tvShowsServiceProvider.getEpisodesBySeasonNumber(
                    tvShowId,
                    seasonNumber
                )
            } throws ServerErrorException()

            // When & Then
            assertFailsWith<ServerErrorException> {
                tvRemoteDataSourceImpl.getEpisodesBySeasonNumber(tvShowId, seasonNumber)
            }
        }

    @Test
    fun `getEpisodesBySeasonNumber should rethrow NoInternetException from service provider`() =
        runTest {
            // Given
            val tvShowId = 1399L
            val seasonNumber = 1
            coEvery {
                tvShowsServiceProvider.getEpisodesBySeasonNumber(
                    tvShowId,
                    seasonNumber
                )
            } throws NoInternetException()

            // When & Then
            assertFailsWith<NoInternetException> {
                tvRemoteDataSourceImpl.getEpisodesBySeasonNumber(tvShowId, seasonNumber)
            }
        }

    @Test
    fun `getEpisodesBySeasonNumber should rethrow NetworkException from service provider`() =
        runTest {
            // Given
            val tvShowId = 1399L
            val seasonNumber = 1
            coEvery {
                tvShowsServiceProvider.getEpisodesBySeasonNumber(
                    tvShowId,
                    seasonNumber
                )
            } throws NetworkException()

            // When & Then
            assertFailsWith<NetworkException> {
                tvRemoteDataSourceImpl.getEpisodesBySeasonNumber(tvShowId, seasonNumber)
            }
        }
}
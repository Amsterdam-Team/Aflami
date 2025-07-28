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
    fun `getSimilarTvShows should return similar TV shows when executed`() = runTest {
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

        coEvery { tvShowsApiService.getSimilarTvShows(tvShowId) } returns expectedSimilarTvShowResponse

        val similarTvShows = tvRemoteDataSourceImpl.getSimilarTvShows(tvShowId)

        coVerify(exactly = 1) { tvShowsApiService.getSimilarTvShows(tvShowId) }
        assertThat(similarTvShows.results).hasSize(1)
        assertThat(similarTvShows.results[0].title).isEqualTo("House of the Dragon")
        assertThat(similarTvShows.results[0].id).isEqualTo(1400)
    }


    @Test
    fun `getSimilarTvShows should rethrow NetworkException from service provider`() = runTest {
        val tvShowId = 1399L
        coEvery { tvShowsApiService.getSimilarTvShows(tvShowId) } throws NetworkException()

        assertThrows<NetworkException> {
            tvRemoteDataSourceImpl.getSimilarTvShows(tvShowId)
        }
    }

    @Test
    fun `getTvShowReviews should return reviews for a TV show when executed`() = runTest {
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

        coEvery { tvShowsApiService.getTvShowReviews(tvShowId) } returns expectedReviewsResponse

        val reviews = tvRemoteDataSourceImpl.getTvShowReviews(tvShowId)

        coVerify(exactly = 1) { tvShowsApiService.getTvShowReviews(tvShowId) }
        assertThat(reviews.id).isEqualTo(tvShowId)
        assertThat(reviews.results).hasSize(1)
        assertThat(reviews.results[0].author).isEqualTo("Reviewer A")
        assertThat(reviews.results[0].content).isEqualTo("An epic series with incredible depth.")
    }

    @Test
    fun `getTvShowReviews should rethrow NetworkException from service provider`() = runTest {
        val tvShowId = 1399L
        coEvery { tvShowsApiService.getTvShowReviews(tvShowId) } throws NetworkException()

        assertThrows<NetworkException> {
            tvRemoteDataSourceImpl.getTvShowReviews(tvShowId)
        }
    }

    @Test
    fun `getTvShowGallery should return TV show images when executed`() = runTest {
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

        coEvery { tvShowsApiService.getTvShowGallery(tvShowId) } returns expectedGalleryResponse

        val gallery = tvRemoteDataSourceImpl.getTvShowGallery(tvShowId)

        coVerify(exactly = 1) { tvShowsApiService.getTvShowGallery(tvShowId) }
        assertThat(gallery.id).isEqualTo(tvShowId)
        assertThat(gallery.backdrops).hasSize(1)
        assertThat(gallery.posters).hasSize(1)
        assertThat(gallery.backdrops?.get(0)?.filePath).isEqualTo("/got_backdrop1.jpg")
        assertThat(gallery.posters?.get(0)?.filePath).isEqualTo("/got_poster1.jpg")
    }


    @Test
    fun `getTvShowGallery should rethrow NetworkException from service provider`() = runTest {
        val tvShowId = 1399L
        coEvery { tvShowsApiService.getTvShowGallery(tvShowId) } throws NetworkException()

        assertThrows<NetworkException> {
            tvRemoteDataSourceImpl.getTvShowGallery(tvShowId)
        }
    }

    @Test
    fun `getTvShowCompanyProduction should return production company details for a TV show when executed`() =
        runTest {
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

            coEvery { tvShowsApiService.getProductionCompany(tvShowId) } returns expectedProductionCompanyResponse

            val productionCompany = tvRemoteDataSourceImpl.getTvShowCompanyProduction(tvShowId)

            coVerify(exactly = 1) { tvShowsApiService.getProductionCompany(tvShowId) }
            assertThat(productionCompany.productionCompanies).hasSize(1)
            assertThat(productionCompany.productionCompanies[0].name).isEqualTo("HBO")
            assertThat(productionCompany.productionCompanies[0].id).isEqualTo(14902)
        }

    @Test
    fun `getTvShowCompanyProduction should rethrow NetworkException from service provider`() =
        runTest {
            val tvShowId = 1399L
            coEvery { tvShowsApiService.getProductionCompany(tvShowId) } throws NetworkException()

            assertThrows<NetworkException> {
                tvRemoteDataSourceImpl.getTvShowCompanyProduction(tvShowId)
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
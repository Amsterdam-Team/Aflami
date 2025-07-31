package com.amsterdam.remotedatasource.datasource

import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.remotedatasource.api.MovieApiService
import com.amsterdam.repository.dto.remote.ProductionCompanyResponse
import com.amsterdam.repository.dto.remote.RemoteActorSearchResponse
import com.amsterdam.repository.dto.remote.RemoteCastAndCrewResponse
import com.amsterdam.repository.dto.remote.RemoteMovieResponse
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

class MovieRemoteDataSourceImplTest {

    private lateinit var movieApiService: MovieApiService
    private lateinit var movieRemoteDataSourceImpl: MovieRemoteDataSourceImpl

    private val jsonSerializer = Json { ignoreUnknownKeys = true }

    @BeforeEach
    fun setUp() {
        movieApiService = mockk()
        movieRemoteDataSourceImpl = MovieRemoteDataSourceImpl(movieApiService)
    }

    @Test
    fun `getMoviesByKeyword should return a list of movies when executed`() = runTest {
        val keyword = "Inception"
        val page = 1
        val jsonString = """
            {
              "page": 1,
              "results": [
                {
                  "adult": false,
                  "backdrop_path": "/8ZJ7O6Hh7vK0J2U1g9xQ1H0yF7c.jpg",
                  "genre_ids": [28, 878, 12],
                  "id": 27205,
                  "original_language": "en",
                  "original_title": "Inception",
                  "overview": "A thief who steals corporate secrets...",
                  "popularity": 120.0,
                  "poster_path": "/oYuEqnN3o490Jq3b9N7Wp0S5NfT.jpg",
                  "release_date": "2010-07-15",
                  "title": "Inception",
                  "video": false,
                  "vote_average": 8.3,
                  "vote_count": 30000
                }
              ],
              "total_pages": 1,
              "total_results": 1
            }
        """.trimIndent()

        val expectedMovieResponse =
            jsonSerializer.decodeFromString<RemoteMovieResponse>(jsonString)


        coEvery {
            movieApiService.getMoviesByKeyword(keyword, page)
        } returns expectedMovieResponse

        val movies = movieRemoteDataSourceImpl.getMoviesByKeyword(keyword, page)

        coVerify(exactly = 1) { movieApiService.getMoviesByKeyword(keyword, page) }


        assertThat(movies.results).hasSize(1)
        assertThat(movies.results[0].title).isEqualTo("Inception")
        assertThat(movies.results[0].id).isEqualTo(27205)
    }

    @Test
    fun `getMoviesByActorName should return movies for a given actor when executed`() = runTest {
        val actorName = "Leonardo DiCaprio"
        val page = 1
        val actorId = 6193
        val actorSearchJson = """
            {
              "page": 1,
              "results": [
                {
                  "adult": false,
                  "gender": 2,
                  "id": $actorId,
                  "known_for": [],
                  "known_for_department": "Acting",
                  "name": "Leonardo DiCaprio",
                  "original_name": "Leonardo DiCaprio",
                  "popularity": 70.0,
                  "profile_path": "/xxC9VdC8Yw40vI0yM6M8o2E5C9W.jpg"
                }
              ],
              "total_pages": 1,
              "total_results": 1
            }
        """.trimIndent()

        val discoverMovieJson = """
            {
              "page": 1,
              "results": [
                {
                  "adult": false,
                  "backdrop_path": "/sEwP7fKx0yFmO6v8YmD2r8M5X5C.jpg",
                  "genre_ids": [18, 53],
                  "id": 12345,
                  "original_language": "en",
                  "original_title": "The Revenant",
                  "overview": "A frontiersman on a fur trapping expedition...",
                  "popularity": 80.0,
                  "poster_path": "/oYuEqnN3o490Jq3b9N7Wp0S5NfT.jpg",
                  "release_date": "2015-12-25",
                  "title": "The Revenant",
                  "video": false,
                  "vote_average": 7.8,
                  "vote_count": 15000
                }
              ],
              "total_pages": 1,
              "total_results": 1
            }
        """.trimIndent()

        val expectedActorSearchResponse =
            jsonSerializer.decodeFromString<RemoteActorSearchResponse>(actorSearchJson)
        val expectedMovieResponse =
            jsonSerializer.decodeFromString<RemoteMovieResponse>(discoverMovieJson)

        coEvery {
            movieApiService.getActorIdByName(actorName, page)
        } returns expectedActorSearchResponse


        coEvery {
            movieApiService.getMoviesByActorId(actorId.toString())
        } returns expectedMovieResponse


        val movies = movieRemoteDataSourceImpl.getMoviesByActorIds(listOf(actorId), page)

        coVerify(exactly = 1) { movieApiService.getActorIdByName(actorName, page) }
        coVerify(exactly = 1) { movieApiService.getMoviesByActorId(actorId.toString()) }


        assertThat(movies.results).hasSize(1)
        assertThat(movies.results[0].title).isEqualTo("The Revenant")
        assertThat(movies.results[0].id).isEqualTo(12345)
    }

    @Test
    fun `getMoviesByCountryIsoCode should return movies from a specific country when executed`() =
        runTest {
            val countryIsoCode = "US"
            val page = 1
            val jsonString = """
            {
              "page": 1,
              "results": [
                {
                  "adult": false,
                  "backdrop_path": "/path/to/us_movie_backdrop.jpg",
                  "genre_ids": [18, 35],
                  "id": 67890,
                  "original_language": "en",
                  "original_title": "American Beauty",
                  "overview": "A film about suburban American life.",
                  "popularity": 90.0,
                  "poster_path": "/path/to/us_movie_poster.jpg",
                  "release_date": "1999-09-17",
                  "title": "American Beauty",
                  "video": false,
                  "vote_average": 8.0,
                  "vote_count": 10000
                }
              ],
              "total_pages": 1,
              "total_results": 1
            }
        """.trimIndent()

            val expectedMovieResponse =
                jsonSerializer.decodeFromString<RemoteMovieResponse>(jsonString)

            coEvery {
                movieApiService.getMoviesByCountryIsoCode(countryIsoCode, page)
            } returns expectedMovieResponse

            val movies = movieRemoteDataSourceImpl.getMoviesByCountryIsoCode(countryIsoCode, page)

            coVerify(exactly = 1) {
                movieApiService.getMoviesByCountryIsoCode(
                    countryIsoCode,
                    page
                )
            }


            assertThat(movies.results).hasSize(1)
            assertThat(movies.results[0].title).isEqualTo("American Beauty")
            assertThat(movies.results[0].id).isEqualTo(67890)
        }

    @Test
    fun `getCastByMovieId should return cast and crew for a movie when executed`() = runTest {
        val movieId = 550L
        val jsonString = """
            {
              "id": 550,
              "cast": [
                {
                  "adult": false,
                  "gender": 2,
                  "id": 819,
                  "known_for_department": "Acting",
                  "name": "Edward Norton",
                  "original_name": "Edward Norton",
                  "popularity": 20.0,
                  "profile_path": "/y2JpB4q6Xy0X7r0vU0q0w8F5B5K.jpg",
                  "cast_id": 4,
                  "character": "The Narrator",
                  "credit_id": "52fe4250c3a36847f80149f3",
                  "order": 0
                }
              ],
              "crew": [
                {
                  "adult": false,
                  "gender": 2,
                  "id": 7467,
                  "known_for_department": "Directing",
                  "name": "David Fincher",
                  "original_name": "David Fincher",
                  "popularity": 30.0,
                  "profile_path": "/path/to/fincher.jpg",
                  "credit_id": "52fe4250c3a36847f80149c9",
                  "department": "Directing",
                  "job": "Director"
                }
              ]
            }
        """.trimIndent()

        val expectedCastAndCrewResponse =
            jsonSerializer.decodeFromString<RemoteCastAndCrewResponse>(jsonString)

        coEvery {
            movieApiService.getCastByMovieId(movieId)
        } returns expectedCastAndCrewResponse

        val castAndCrew = movieRemoteDataSourceImpl.getCastByMovieId(movieId)

        coVerify(exactly = 1) { movieApiService.getCastByMovieId(movieId) }

        assertThat(castAndCrew.id).isEqualTo(movieId.toInt())
        assertThat(castAndCrew.cast).hasSize(1)
        assertThat(castAndCrew.cast[0].name).isEqualTo("Edward Norton")
        assertThat(castAndCrew.crew).hasSize(1)
        assertThat(castAndCrew.crew[0].name).isEqualTo("David Fincher")
        assertThat(castAndCrew.crew[0].job).isEqualTo("Director")
    }

    @Test
    fun `getMovieReviews should return reviews for a movie when executed`() = runTest {
        val movieId = 550L
        val jsonString = """
            {
              "id": 550,
              "page": 1,
              "results": [
                {
                  "author": "John Doe",
                  "author_details": {
                    "name": "John Doe",
                    "username": "johndoe",
                    "avatar_path": null,
                    "rating": 7.0
                  },
                  "content": "This movie was fantastic!",
                  "created_at": "2023-01-01T10:00:00.000Z",
                  "id": "abc123xyz",
                  "updated_at": "2023-01-01T10:00:00.000Z",
                  "url": "http://example.com/review/abc123xyz"
                }
              ],
              "total_pages": 1,
              "total_results": 1
            }
        """.trimIndent()

        val expectedReviewsResponse =
            jsonSerializer.decodeFromString<ReviewsResponse>(jsonString)

        coEvery {
            movieApiService.getMovieReviews(movieId)
        } returns expectedReviewsResponse

        val reviews = movieRemoteDataSourceImpl.getMovieReviews(movieId)

        coVerify(exactly = 1) { movieApiService.getMovieReviews(movieId) }

        assertThat(reviews.id).isEqualTo(movieId)
        assertThat(reviews.results).hasSize(1)
        assertThat(reviews.results[0].author).isEqualTo("John Doe")
        assertThat(reviews.results[0].content).isEqualTo("This movie was fantastic!")
    }

    @Test
    fun `getSimilarMovies should return similar movies when executed`() = runTest {
        val movieId = 550L
        val jsonString = """
            {
              "page": 1,
              "results": [
                {
                  "adult": false,
                  "backdrop_path": "/similar_movie_backdrop.jpg",
                  "genre_ids": [28, 53],
                  "id": 98765,
                  "original_language": "en",
                  "original_title": "Se7en",
                  "overview": "Two detectives track a serial killer...",
                  "popularity": 75.0,
                  "poster_path": "/similar_movie_poster.jpg",
                  "release_date": "1995-09-22",
                  "title": "Se7en",
                  "video": false,
                  "vote_average": 8.2,
                  "vote_count": 25000
                }
              ],
              "total_pages": 1,
              "total_results": 1
            }
        """.trimIndent()

        val expectedMovieResponse =
            jsonSerializer.decodeFromString<RemoteMovieResponse>(jsonString)

        coEvery {
            movieApiService.getSimilarMovies(movieId)
        } returns expectedMovieResponse

        val similarMovies = movieRemoteDataSourceImpl.getSimilarMovies(movieId)

        coVerify(exactly = 1) { movieApiService.getSimilarMovies(movieId) }

        assertThat(similarMovies.results).hasSize(1)
        assertThat(similarMovies.results[0].title).isEqualTo("Se7en")
        assertThat(similarMovies.results[0].id).isEqualTo(98765)
    }

    @Test
    fun `getMovieGallery should return movie images when executed`() = runTest {
        val movieId = 550L
        val jsonString = """
            {
              "id": 550,
              "backdrops": [
                {"aspect_ratio": 1.778, "file_path": "/backdrop1.jpg", "height": 1080, "iso_639_1": null, "vote_average": 5.0, "vote_count": 1, "width": 1920},
                {"aspect_ratio": 1.778, "file_path": "/backdrop2.jpg", "height": 1080, "iso_639_1": null, "vote_average": 5.0, "vote_count": 1, "width": 1920}
              ],
              "logos": [],
              "posters": [
                {"aspect_ratio": 0.667, "file_path": "/poster1.jpg", "height": 900, "iso_639_1": null, "vote_average": 5.0, "vote_count": 1, "width": 600}
              ]
            }
        """.trimIndent()

        val expectedGalleryResponse =
            jsonSerializer.decodeFromString<RemoteGalleryResponse>(jsonString)

        coEvery {
            movieApiService.getMovieGallery(movieId)
        } returns expectedGalleryResponse

        val gallery = movieRemoteDataSourceImpl.getMovieGallery(movieId)

        coVerify(exactly = 1) { movieApiService.getMovieGallery(movieId) }

        assertThat(gallery.id).isEqualTo(movieId)
        assertThat(gallery.backdrops).hasSize(2)
        assertThat(gallery.posters).hasSize(1)
        assertThat(gallery.backdrops?.any { it.filePath == "/backdrop1.jpg" }).isTrue()
        assertThat(gallery.posters?.any { it.filePath == "/poster1.jpg" }).isTrue()
    }

    @Test
    fun `getMoviePosters should return movie images for posters when executed`() = runTest {
        val movieId = 550L
        val jsonString = """
            {
              "id": 550,
              "backdrops": [],
              "logos": [],
              "posters": [
                {"aspect_ratio": 0.667, "file_path": "/poster1.jpg", "height": 900, "iso_639_1": null, "vote_average": 5.0, "vote_count": 1, "width": 600},
                {"aspect_ratio": 0.667, "file_path": "/poster2.jpg", "height": 900, "iso_639_1": null, "vote_average": 5.0, "vote_count": 1, "width": 600}
              ]
            }
        """.trimIndent()

        val expectedGalleryResponse =
            jsonSerializer.decodeFromString<RemoteGalleryResponse>(jsonString)

        coEvery {
            movieApiService.getMoviePosters(movieId)
        } returns expectedGalleryResponse

        val posters = movieRemoteDataSourceImpl.getMoviePosters(movieId)

        coVerify(exactly = 1) { movieApiService.getMoviePosters(movieId) }

        assertThat(posters.id).isEqualTo(movieId)
        assertThat(posters.backdrops).isEmpty()
        assertThat(posters.posters).hasSize(2)
        assertThat(posters.posters?.any { it.filePath == "/poster1.jpg" }).isTrue()
    }


    @Test
    fun `getPopularMovies should return popular movies when executed`() = runTest {
        val jsonString = """
            {
              "page": 1,
              "results": [
                {
                  "adult": false,
                  "backdrop_path": "/path/to/popular_backdrop.jpg",
                  "genre_ids": [28],
                  "id": 112233,
                  "original_language": "en",
                  "original_title": "Popular Movie",
                  "overview": "An overview of a popular movie.",
                  "popularity": 500.0,
                  "poster_path": "/path/to/popular_poster.jpg",
                  "release_date": "2024-01-01",
                  "title": "Popular Movie",
                  "video": false,
                  "vote_average": 8.5,
                  "vote_count": 50000
                }
              ],
              "total_pages": 1,
              "total_results": 1
            }
        """.trimIndent()

        val expectedMovieResponse =
            jsonSerializer.decodeFromString<RemoteMovieResponse>(jsonString)

        coEvery {
            movieApiService.getPopularMovies()
        } returns expectedMovieResponse

        val popularMovies = movieRemoteDataSourceImpl.getPopularMovies()

        coVerify(exactly = 1) { movieApiService.getPopularMovies() }

        assertThat(popularMovies.results).hasSize(1)
        assertThat(popularMovies.results[0].title).isEqualTo("Popular Movie")
        assertThat(popularMovies.results[0].id).isEqualTo(112233)
    }

    @Test
    fun `getProductionCompany should return production company details when executed`() = runTest {
        val movieId = 550L
        val jsonString = """
            {
              "adult": false,
              "backdrop_path": "/path/to/backdrop.jpg",
              "belongs_to_collection": null,
              "budget": 63000000,
              "genres": [
                {"id": 18, "name": "Drama"}
              ],
              "homepage": "http://www.foxmovies.com/movies/fight-club",
              "id": 550,
              "imdb_id": "tt0137523",
              "original_language": "en",
              "original_title": "Fight Club",
              "overview": "A depressed man...",
              "popularity": 45.0,
              "poster_path": "/path/to/poster.jpg",
              "production_companies": [
                {
                  "id": 508,
                  "logo_path": "/path/to/regency_logo.png",
                  "name": "Regency Enterprises",
                  "origin_country": "US"
                }
              ],
              "production_countries": [],
              "release_date": "1999-10-15",
              "revenue": 100853753,
              "runTimeInMinutes": 139,
              "spoken_languages": [],
              "status": "Released",
              "tagline": "Mischief. Mayhem. Soap.",
                  "title": "Fight Club",
                  "video": false,
                  "vote_average": 8.433,
                  "vote_count": 27000
            }
        """.trimIndent()

        val expectedProductionCompanyResponse =
            jsonSerializer.decodeFromString<ProductionCompanyResponse>(jsonString)

        coEvery {
            movieApiService.getProductionCompany(movieId)
        } returns expectedProductionCompanyResponse

        val productionCompany = movieRemoteDataSourceImpl.getProductionCompany(movieId)

        coVerify(exactly = 1) { movieApiService.getProductionCompany(movieId) }

        assertThat(productionCompany.productionCompanies).hasSize(1)
        assertThat(productionCompany.productionCompanies[0].name).isEqualTo("Regency Enterprises")
        assertThat(productionCompany.productionCompanies[0].id).isEqualTo(508)
    }


    @Test
    fun `getPopularMovies should rethrow NetworkException from service provider`() = runTest {
        coEvery { movieApiService.getPopularMovies() } throws NetworkException()

        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getPopularMovies()
        }
    }
    @Test
    fun `getMoviesByActorName should rethrow NetworkException if actor search fails`() = runTest {
        val actorName = "Leonardo DiCaprio"
        val actorId = 1234
        val page = 1
        coEvery { movieApiService.getActorIdByName(actorName, page) } throws NetworkException()

        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getMoviesByActorIds(listOf(actorId), page)
        }
    }


    @Test
    fun `getMoviesByActorName should rethrow NetworkException if movie discovery by actor fails`() = runTest {
        val actorName = "Leonardo DiCaprio"
        val page = 1
        val actorId = 6193
        val actorSearchJson = """
            {
              "page": 1,
              "results": [
                {
                  "adult": false,
                  "gender": 2,
                  "id": $actorId,
                  "known_for": [],
                  "known_for_department": "Acting",
                  "name": "Leonardo DiCaprio",
                  "original_name": "Leonardo DiCaprio",
                  "popularity": 70.0,
                  "profile_path": "/xxC9VdC8Yw40vI0yM6M8o2E5C9W.jpg"
                }
              ],
              "total_pages": 1,
              "total_results": 1
            }
        """.trimIndent()

        val expectedActorSearchResponse =
            jsonSerializer.decodeFromString<RemoteActorSearchResponse>(actorSearchJson)

        coEvery { movieApiService.getActorIdByName(actorName, page) } returns expectedActorSearchResponse
        coEvery { movieApiService.getMoviesByActorId(actorId.toString()) } throws NetworkException()

        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getMoviesByActorIds(listOf(actorId), page)
        }
    }

    @Test
    fun `getMoviesByKeyword should rethrow NetworkException from service provider`() = runTest {
        val keyword = "test"
        val page = 1
        coEvery { movieApiService.getMoviesByKeyword(keyword, page) } throws NetworkException()

        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getMoviesByKeyword(keyword, page)
        }
    }

    @Test
    fun `getMoviesByCountryIsoCode should rethrow NetworkException from service provider`() = runTest {
        val countryIsoCode = "US"
        val page = 1
        coEvery { movieApiService.getMoviesByCountryIsoCode(countryIsoCode, page) } throws NetworkException()

        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getMoviesByCountryIsoCode(countryIsoCode, page)
        }
    }


    @Test
    fun `getCastByMovieId should rethrow NetworkException from service provider`() = runTest {
        val movieId = 550L
        coEvery { movieApiService.getCastByMovieId(movieId) } throws NetworkException()

        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getCastByMovieId(movieId)
        }
    }


    @Test
    fun `getMovieReviews should rethrow NetworkException from service provider`() = runTest {
        val movieId = 550L
        coEvery { movieApiService.getMovieReviews(movieId) } throws NetworkException()

        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getMovieReviews(movieId)
        }
    }


    @Test
    fun `getSimilarMovies should rethrow NetworkException from service provider`() = runTest {
        val movieId = 550L
        coEvery { movieApiService.getSimilarMovies(movieId) } throws NetworkException()

        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getSimilarMovies(movieId)
        }
    }


    @Test
    fun `getMovieGallery should rethrow NetworkException from service provider`() = runTest {
        val movieId = 550L
        coEvery { movieApiService.getMovieGallery(movieId) } throws NetworkException()

        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getMovieGallery(movieId)
        }
    }


    @Test
    fun `getMovieDetailsById should rethrow NetworkException from service provider`() = runTest {
        val movieId = 550L
        coEvery { movieApiService.getMovieDetailsById(movieId) } throws NetworkException()

        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getMovieDetailsById(movieId)
        }
    }


    @Test
    fun `getMoviePosters should rethrow NetworkException from service provider`() = runTest {
        val movieId = 550L
        coEvery { movieApiService.getMoviePosters(movieId) } throws NetworkException()

        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getMoviePosters(movieId)
        }
    }


    @Test
    fun `getProductionCompany should rethrow NetworkException from service provider`() = runTest {
        val movieId = 550L
        coEvery { movieApiService.getProductionCompany(movieId) } throws NetworkException()

        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getProductionCompany(movieId)
        }
    }
}
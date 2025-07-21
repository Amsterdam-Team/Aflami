package com.example.remotedatasource

import com.example.remotedatasource.datasource.MovieRemoteDataSourceImpl
import com.example.remotedatasource.serviceProvider.MovieServiceProvider
import com.example.repository.dto.remote.ProductionCompanyResponse
import com.example.repository.dto.remote.RemoteActorSearchResponse
import com.example.repository.dto.remote.RemoteCastAndCrewResponse
import com.example.repository.dto.remote.RemoteMovieItemDto
import com.example.repository.dto.remote.RemoteMovieResponse
import com.example.repository.dto.remote.movieGallery.RemoteMovieGalleryResponse
import com.example.repository.dto.remote.review.ReviewsResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MovieRemoteDataSourceImplTest {

    private lateinit var movieServiceProvider: MovieServiceProvider
    private lateinit var movieRemoteDataSourceImpl: MovieRemoteDataSourceImpl

    private val jsonSerializer = Json { ignoreUnknownKeys = true }

    @Before
    fun setUp() {
        movieServiceProvider = mockk() // CHANGED
        movieRemoteDataSourceImpl = MovieRemoteDataSourceImpl(movieServiceProvider)
    }

    @Test
    fun `getMoviesByKeyword should return a list of movies when executed`() = runTest {
        // Given
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
            movieServiceProvider.getMoviesByKeyword(keyword, page)
        } returns expectedMovieResponse

        // When
        val movies = movieRemoteDataSourceImpl.getMoviesByKeyword(keyword, page)

        // Then
        coVerify(exactly = 1) { movieServiceProvider.getMoviesByKeyword(keyword, page) }


        assertEquals(1, movies.results.size)
        assertEquals("Inception", movies.results[0].title)
        assertEquals(27205, movies.results[0].id)
    }

    @Test
    fun `getMoviesByActorName should return movies for a given actor when executed`() = runTest {
        // Given
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
            movieServiceProvider.getActorIdByName(actorName, page)
        } returns expectedActorSearchResponse


        coEvery {
            movieServiceProvider.getMoviesByActorId(actorId.toString())
        } returns expectedMovieResponse

        // When
        val movies = movieRemoteDataSourceImpl.getMoviesByActorName(actorName, page)

        // Then
        coVerify(exactly = 1) { movieServiceProvider.getActorIdByName(actorName, page) }
        coVerify(exactly = 1) { movieServiceProvider.getMoviesByActorId(actorId.toString()) }


        assertEquals(1, movies.results.size)
        assertEquals("The Revenant", movies.results[0].title)
        assertEquals(12345, movies.results[0].id)
    }

    @Test
    fun `getMoviesByCountryIsoCode should return movies from a specific country when executed`() =
        runTest {
            // Given
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
                movieServiceProvider.getMoviesByCountryIsoCode(countryIsoCode, page)
            } returns expectedMovieResponse

            // When
            val movies = movieRemoteDataSourceImpl.getMoviesByCountryIsoCode(countryIsoCode, page)

            // Then
            coVerify(exactly = 1) {
                movieServiceProvider.getMoviesByCountryIsoCode(
                    countryIsoCode,
                    page
                )
            }


            assertEquals(1, movies.results.size)
            assertEquals("American Beauty", movies.results[0].title)
            assertEquals(67890, movies.results[0].id)
        }

    @Test
    fun `getCastByMovieId should return cast and crew for a movie when executed`() = runTest {
        // Given
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
            movieServiceProvider.getCastByMovieId(movieId)
        } returns expectedCastAndCrewResponse

        // When
        val castAndCrew = movieRemoteDataSourceImpl.getCastByMovieId(movieId)

        // Then
        coVerify(exactly = 1) { movieServiceProvider.getCastByMovieId(movieId) }

        assertEquals(movieId.toInt(), castAndCrew.id)
        assertEquals(1, castAndCrew.cast.size)
        assertEquals("Edward Norton", castAndCrew.cast[0].name)
        assertEquals(1, castAndCrew.crew.size)
        assertEquals("David Fincher", castAndCrew.crew[0].name)
        assertEquals("Director", castAndCrew.crew[0].job)
    }

    @Test
    fun `getMovieReviews should return reviews for a movie when executed`() = runTest {
        // Given
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
            movieServiceProvider.getMovieReviews(movieId)
        } returns expectedReviewsResponse

        // When
        val reviews = movieRemoteDataSourceImpl.getMovieReviews(movieId)

        // Then
        coVerify(exactly = 1) { movieServiceProvider.getMovieReviews(movieId) }

        assertEquals(movieId, reviews.id)
        assertEquals(1, reviews.results.size)
        assertEquals("John Doe", reviews.results[0].author)
        assertEquals("This movie was fantastic!", reviews.results[0].content)
    }

    @Test
    fun `getSimilarMovies should return similar movies when executed`() = runTest {
        // Given
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
            movieServiceProvider.getSimilarMovies(movieId)
        } returns expectedMovieResponse

        // When
        val similarMovies = movieRemoteDataSourceImpl.getSimilarMovies(movieId)

        // Then
        coVerify(exactly = 1) { movieServiceProvider.getSimilarMovies(movieId) }

        assertEquals(1, similarMovies.results.size)
        assertEquals("Se7en", similarMovies.results[0].title)
        assertEquals(98765, similarMovies.results[0].id)
    }

    @Test
    fun `getMovieGallery should return movie images when executed`() = runTest {
        // Given
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
            jsonSerializer.decodeFromString<RemoteMovieGalleryResponse>(jsonString)

        coEvery {
            movieServiceProvider.getMovieGallery(movieId)
        } returns expectedGalleryResponse

        // When
        val gallery = movieRemoteDataSourceImpl.getMovieGallery(movieId)

        // Then
        coVerify(exactly = 1) { movieServiceProvider.getMovieGallery(movieId) }

        assertEquals(movieId, gallery.id)
        assertEquals(2, gallery.backdrops?.size)
        assertEquals(1, gallery.posters?.size)
        assertTrue(gallery.backdrops?.any { it.filePath == "/backdrop1.jpg" } ?: false)
        assertTrue(gallery.posters?.any { it.filePath == "/poster1.jpg" } ?: false)
    }

    @Test
    fun `getMovieDetailsById should return detailed movie information when executed`() = runTest {
        // Given
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
              "production_companies": [],
              "production_countries": [],
              "release_date": "1999-10-15",
              "revenue": 100853753,
              "runtime": 139,
              "spoken_languages": [],
              "status": "Released",
              "tagline": "Mischief. Mayhem. Soap.",
              "title": "Fight Club",
              "video": false,
              "vote_average": 8.433,
              "vote_count": 27000
            }
        """.trimIndent()

        val expectedMovieItemDto =
            jsonSerializer.decodeFromString<RemoteMovieItemDto>(jsonString)

        coEvery {
            movieServiceProvider.getMovieDetailsById(movieId)
        } returns expectedMovieItemDto

        // When
        val movieDetails = movieRemoteDataSourceImpl.getMovieDetailsById(movieId)

        // Then
        coVerify(exactly = 1) { movieServiceProvider.getMovieDetailsById(movieId) }

        assertEquals(movieId, movieDetails.id)
        assertEquals("Fight Club", movieDetails.title)
        assertEquals("A depressed man...", movieDetails.overview)
        assertEquals(139, movieDetails.runtime)
    }

    @Test
    fun `getMoviePosters should return movie images for posters when executed`() = runTest {
        // Given
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
            jsonSerializer.decodeFromString<RemoteMovieGalleryResponse>(jsonString)

        coEvery {
            movieServiceProvider.getMoviePosters(movieId)
        } returns expectedGalleryResponse

        // When
        val posters = movieRemoteDataSourceImpl.getMoviePosters(movieId)

        // Then
        coVerify(exactly = 1) { movieServiceProvider.getMoviePosters(movieId) }

        assertEquals(movieId, posters.id)
        assertEquals(0, posters.backdrops?.size)
        assertEquals(2, posters.posters?.size)
        assertTrue(posters.posters?.any { it.filePath == "/poster1.jpg" } ?: false)
    }


    @Test
    fun `getPopularMovies should return popular movies when executed`() = runTest {
        // Given
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
            movieServiceProvider.getPopularMovies()
        } returns expectedMovieResponse

        // When
        val popularMovies = movieRemoteDataSourceImpl.getPopularMovies()

        // Then
        coVerify(exactly = 1) { movieServiceProvider.getPopularMovies() }

        assertEquals(1, popularMovies.results.size)
        assertEquals("Popular Movie", popularMovies.results[0].title)
        assertEquals(112233, popularMovies.results[0].id)
    }

    @Test
    fun `getProductionCompany should return production company details when executed`() = runTest {
        // Given
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
              "runtime": 139,
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
            movieServiceProvider.getProductionCompany(movieId)
        } returns expectedProductionCompanyResponse

        // When
        val productionCompany = movieRemoteDataSourceImpl.getProductionCompany(movieId)

        // Then
        coVerify(exactly = 1) { movieServiceProvider.getProductionCompany(movieId) }

        assertEquals(1, productionCompany.productionCompanies.size)
        assertEquals("Regency Enterprises", productionCompany.productionCompanies[0].name)
        assertEquals(508, productionCompany.productionCompanies[0].id)
    }
}
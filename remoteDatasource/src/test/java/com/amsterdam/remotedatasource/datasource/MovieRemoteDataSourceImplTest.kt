package com.amsterdam.remotedatasource.datasource

import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.remotedatasource.api.MovieApiService
import com.amsterdam.repository.dto.remote.ActorSearchItemDto
import com.amsterdam.repository.dto.remote.RemoteActorSearchResponse
import com.amsterdam.repository.dto.remote.RemoteCastAndCrewResponse
import com.amsterdam.repository.dto.remote.RemoteMovieDetailsResponse
import com.amsterdam.repository.dto.remote.RemoteMovieItemDto
import com.amsterdam.repository.dto.remote.RemoteMovieResponse
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

class MovieRemoteDataSourceImplTest {

    private lateinit var movieApiService: MovieApiService
    private lateinit var movieRemoteDataSourceImpl: MovieRemoteDataSourceImpl

    @BeforeEach
    fun setUp() {
        movieApiService = mockk()
        movieRemoteDataSourceImpl = MovieRemoteDataSourceImpl(movieApiService)
    }

    @Test
    fun `getMoviesByKeyword should return a list of movies when successful`() = runTest {
        // Given
        val keyword = "Inception"
        val page = 1
        val expectedResponse = RemoteMovieResponse(
            page = 1,
            results = listOf(
                RemoteMovieItemDto(
                    id = 27205,
                    title = "Inception",
                    adult = false,
                    backdropPath = "/path/to/backdrop.jpg",
                    originalLanguage = "en",
                    originalTitle = "Inception",
                    overview = "A thief...",
                    popularity = 120.0,
                    posterPath = "/path/to/poster.jpg",
                    releaseDate = "2010-07-15",
                    video = false,
                    voteAverage = 8.3,
                    voteCount = 30000
                )
            ),
            totalPages = 1,
            totalResults = 1
        )
        coEvery { movieApiService.getMoviesByKeyword(keyword, page) } returns expectedResponse

        // When
        val movies = movieRemoteDataSourceImpl.getMoviesByKeyword(keyword, page)

        // Then
        assertThat(movies).isEqualTo(expectedResponse)
        coVerify(exactly = 1) { movieApiService.getMoviesByKeyword(keyword, page) }
    }

    @Test
    fun `getMoviesByKeyword should throw NetworkException when api call fails`() = runTest {
        // Given
        val keyword = "test"
        val page = 1
        coEvery { movieApiService.getMoviesByKeyword(keyword, page) } throws NetworkException()

        // When & Then
        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getMoviesByKeyword(keyword, page)
        }
    }

    @Test
    fun `getMoviesByActorIds should return movies for a given actor when successful`() = runTest {
        // Given
        val actorIds = listOf(6193)
        val expectedResponse = RemoteMovieResponse(
            page = 1,
            results = listOf(
                RemoteMovieItemDto(
                    id = 12345,
                    title = "The Revenant",
                    adult = false,
                    backdropPath = "/path/to/backdrop.jpg",
                    originalLanguage = "en",
                    originalTitle = "The Revenant",
                    overview = "A frontiersman...",
                    popularity = 80.0,
                    posterPath = "/path/to/poster.jpg",
                    releaseDate = "2015-12-25",
                    video = false,
                    voteAverage = 7.8,
                    voteCount = 15000
                )
            ),
            totalPages = 1,
            totalResults = 1
        )
        coEvery { movieApiService.getMoviesByActorId("6193") } returns expectedResponse

        // When
        val movies = movieRemoteDataSourceImpl.getMoviesByActorIds(actorIds, 1)

        // Then
        assertThat(movies).isEqualTo(expectedResponse)
        coVerify(exactly = 1) { movieApiService.getMoviesByActorId("6193") }
    }

    @Test
    fun `getMoviesByActorIds should throw NetworkException when api call fails`() = runTest {
        // Given
        val actorIds = listOf(6193)
        val page = 1
        coEvery { movieApiService.getMoviesByActorId("6193") } throws NetworkException()

        // When & Then
        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getMoviesByActorIds(actorIds, page)
        }
    }

    @Test
    fun `getActorIdsByName should return actor IDs when successful`() = runTest {
        // Given
        val name = "Leonardo DiCaprio"
        val page = 1
        val expectedResponse = RemoteActorSearchResponse(
            page = 1,
            totalPages = 1,
            totalResults = 1,
            actors = listOf(
                ActorSearchItemDto(
                    id = 6193,
                    name = name,
                    adult = false,
                    gender = 2,
                    knownFor = emptyList(),
                    originalName = name,
                    popularity = 70.0,
                    profilePath = null
                )
            )
        )
        coEvery { movieApiService.getActorIdByName(name, page) } returns expectedResponse

        // When
        val actorIds = movieRemoteDataSourceImpl.getActorIdsByName(name, page)

        // Then
        assertThat(actorIds).containsExactly(6193)
        coVerify(exactly = 1) { movieApiService.getActorIdByName(name, page) }
    }

    @Test
    fun `getActorIdsByName should return an empty list when no actors are found`() = runTest {
        // Given
        val name = "No Actor"
        val page = 1
        val expectedResponse = RemoteActorSearchResponse(
            page = 1,
            totalPages = 1,
            totalResults = 0,
            actors = emptyList()
        )
        coEvery { movieApiService.getActorIdByName(name, page) } returns expectedResponse

        // When
        val actorIds = movieRemoteDataSourceImpl.getActorIdsByName(name, page)

        // Then
        assertThat(actorIds).isEmpty()
    }

    @Test
    fun `getActorIdsByName should throw NetworkException when api call fails`() = runTest {
        // Given
        val name = "test"
        val page = 1
        coEvery { movieApiService.getActorIdByName(name, page) } throws NetworkException()

        // When & Then
        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getActorIdsByName(name, page)
        }
    }

    @Test
    fun `getMoviesByCountryIsoCode should return movies from a specific country when successful`() =
        runTest {
            // Given
            val countryIsoCode = "US"
            val page = 1
            val expectedResponse = RemoteMovieResponse(
                page = 1,
                results = listOf(
                    RemoteMovieItemDto(
                        id = 67890,
                        title = "American Beauty",
                        adult = false,
                        backdropPath = "/path/to/backdrop.jpg",
                        originalLanguage = "en",
                        originalTitle = "American Beauty",
                        overview = "A film about...",
                        popularity = 90.0,
                        posterPath = "/path/to/poster.jpg",
                        releaseDate = "1999-09-17",
                        video = false,
                        voteAverage = 8.0,
                        voteCount = 10000
                    )
                ),
                totalPages = 1,
                totalResults = 1
            )
            coEvery {
                movieApiService.getMoviesByCountryIsoCode(
                    countryIsoCode,
                    page
                )
            } returns expectedResponse

            // When
            val movies = movieRemoteDataSourceImpl.getMoviesByCountryIsoCode(countryIsoCode, page)

            // Then
            assertThat(movies).isEqualTo(expectedResponse)
            coVerify(exactly = 1) {
                movieApiService.getMoviesByCountryIsoCode(
                    countryIsoCode,
                    page
                )
            }
        }

    @Test
    fun `getMoviesByCountryIsoCode should throw NetworkException when api call fails`() = runTest {
        // Given
        val countryIsoCode = "US"
        val page = 1
        coEvery {
            movieApiService.getMoviesByCountryIsoCode(
                countryIsoCode,
                page
            )
        } throws NetworkException()

        // When & Then
        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getMoviesByCountryIsoCode(countryIsoCode, page)
        }
    }

    @Test
    fun `getCastByMovieId should return cast and crew for a movie when successful`() = runTest {
        // Given
        val movieId = 550L
        val expectedResponse = RemoteCastAndCrewResponse(
            id = movieId,
            cast = emptyList(),
            crew = emptyList()
        )
        coEvery { movieApiService.getCastByMovieId(movieId) } returns expectedResponse

        // When
        val castAndCrew = movieRemoteDataSourceImpl.getCastByMovieId(movieId)

        // Then
        assertThat(castAndCrew).isEqualTo(expectedResponse)
        coVerify(exactly = 1) { movieApiService.getCastByMovieId(movieId) }
    }

    @Test
    fun `getCastByMovieId should throw NetworkException when api call fails`() = runTest {
        // Given
        val movieId = 550L
        coEvery { movieApiService.getCastByMovieId(movieId) } throws NetworkException()

        // When & Then
        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getCastByMovieId(movieId)
        }
    }

    @Test
    fun `getMovieDetailsById should return movie details including nested data when successful`() =
        runTest {
            // Given
            val movieId = 550L
            val expectedResponse = RemoteMovieDetailsResponse(
                id = movieId,
                adult = false,
                backdropPath = null,
                originalLanguage = "en",
                originalTitle = "Test Movie",
                overview = "Overview",
                popularity = 1.0,
                posterPath = null,
                releaseDate = "2023-01-01",
                title = "Test Movie",
                video = false,
                voteAverage = 5.0,
                voteCount = 1,
                reviews = ReviewsResponse(
                    id = movieId,
                    page = 1,
                    results = emptyList(),
                    totalPages = 1,
                    totalResults = 0
                ),
                credits = RemoteCastAndCrewResponse(
                    id = movieId,
                    cast = emptyList(),
                    crew = emptyList()
                ),
                similar = RemoteMovieResponse(
                    page = 1,
                    results = emptyList(),
                    totalPages = 1,
                    totalResults = 0
                ),
                images = RemoteGalleryResponse(
                    id = movieId,
                    backdrops = emptyList(),
                    logos = emptyList(),
                    posters = emptyList()
                ),
                genreIds = emptyList(),
                productionCompanies = emptyList(),
                originCountry = emptyList(),
                runtime = 0,
                genres = emptyList()
            )
            coEvery { movieApiService.getMovieDetailsById(movieId) } returns expectedResponse

            // When
            val details = movieRemoteDataSourceImpl.getMovieDetailsById(movieId)

            // Then
            assertThat(details).isEqualTo(expectedResponse)
            coVerify(exactly = 1) { movieApiService.getMovieDetailsById(movieId) }
        }

    @Test
    fun `getMovieDetailsById should throw NetworkException when api call fails`() = runTest {
        // Given
        val movieId = 550L
        coEvery { movieApiService.getMovieDetailsById(movieId) } throws NetworkException()

        // When & Then
        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getMovieDetailsById(movieId)
        }
    }

    @Test
    fun `getPopularMovies should return popular movies when successful`() = runTest {
        // Given
        val expectedResponse = RemoteMovieResponse(
            page = 1,
            results = listOf(
                RemoteMovieItemDto(
                    id = 112233,
                    title = "Popular Movie",
                    adult = false,
                    backdropPath = null,
                    originalLanguage = "en",
                    originalTitle = "Popular Movie",
                    overview = "An overview...",
                    popularity = 500.0,
                    posterPath = null,
                    releaseDate = "2024-01-01",
                    video = false,
                    voteAverage = 8.5,
                    voteCount = 50000
                )
            ),
            totalPages = 1,
            totalResults = 1
        )
        coEvery { movieApiService.getPopularMovies() } returns expectedResponse

        // When
        val popularMovies = movieRemoteDataSourceImpl.getPopularMovies()

        // Then
        assertThat(popularMovies).isEqualTo(expectedResponse)
        coVerify(exactly = 1) { movieApiService.getPopularMovies() }
    }

    @Test
    fun `getPopularMovies should throw NetworkException when api call fails`() = runTest {
        // Given
        coEvery { movieApiService.getPopularMovies() } throws NetworkException()

        // When & Then
        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getPopularMovies()
        }
    }

    @Test
    fun `getUpcomingMovies should return upcoming movies when successful`() = runTest {
        // Given
        val expectedResponse = RemoteMovieResponse(
            page = 1,
            results = listOf(
                RemoteMovieItemDto(
                    id = 445566,
                    title = "Upcoming Movie",
                    adult = false,
                    backdropPath = null,
                    originalLanguage = "en",
                    originalTitle = "Upcoming Movie",
                    overview = "An overview...",
                    popularity = 50.0,
                    posterPath = null,
                    releaseDate = "2025-01-01",
                    video = false,
                    voteAverage = 7.0,
                    voteCount = 1000
                )
            ),
            totalPages = 1,
            totalResults = 1
        )
        coEvery { movieApiService.getUpcomingMovies() } returns expectedResponse

        // When
        val upcomingMovies = movieRemoteDataSourceImpl.getUpcomingMovies()

        // Then
        assertThat(upcomingMovies).isEqualTo(expectedResponse)
        coVerify(exactly = 1) { movieApiService.getUpcomingMovies() }
    }

    @Test
    fun `getUpcomingMovies should throw NetworkException when api call fails`() = runTest {
        // Given
        coEvery { movieApiService.getUpcomingMovies() } throws NetworkException()

        // When & Then
        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getUpcomingMovies()
        }
    }

    @Test
    fun `getTopRatedMovies should return top rated movies when successful`() = runTest {
        // Given
        val page = 1
        val expectedResponse = RemoteMovieResponse(
            page = page,
            results = listOf(
                RemoteMovieItemDto(
                    id = 778899,
                    title = "Top Rated Movie",
                    adult = false,
                    backdropPath = null,
                    originalLanguage = "en",
                    originalTitle = "Top Rated Movie",
                    overview = "An overview...",
                    popularity = 600.0,
                    posterPath = null,
                    releaseDate = "2023-01-01",
                    video = false,
                    voteAverage = 9.0,
                    voteCount = 500000
                )
            ),
            totalPages = 1,
            totalResults = 1
        )
        coEvery { movieApiService.getTopRatedMovies(page) } returns expectedResponse

        // When
        val topRatedMovies = movieRemoteDataSourceImpl.getTopRatedMovies(page)

        // Then
        assertThat(topRatedMovies).isEqualTo(expectedResponse)
        coVerify(exactly = 1) { movieApiService.getTopRatedMovies(page) }
    }

    @Test
    fun `getTopRatedMovies should throw NetworkException when api call fails`() = runTest {
        // Given
        val page = 1
        coEvery { movieApiService.getTopRatedMovies(page) } throws NetworkException()

        // When & Then
        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getTopRatedMovies(page)
        }
    }

    @Test
    fun `getMoviesByGenreIds should return movies for given genre IDs when successful`() = runTest {
        // Given
        val genreIds = listOf(28L, 53L)
        val expectedResponse = RemoteMovieResponse(
            page = 1,
            results = listOf(
                RemoteMovieItemDto(
                    id = 112233,
                    title = "Action Thriller",
                    adult = false,
                    backdropPath = null,
                    originalLanguage = "en",
                    originalTitle = "Action Thriller",
                    overview = "An overview...",
                    popularity = 500.0,
                    posterPath = null,
                    releaseDate = "2022-01-01",
                    video = false,
                    voteAverage = 8.0,
                    voteCount = 20000
                )
            ),
            totalPages = 1,
            totalResults = 1
        )
        coEvery { movieApiService.getMoviesByGenreIds(genreIds) } returns expectedResponse

        // When
        val movies = movieRemoteDataSourceImpl.getMoviesByGenreIds(genreIds)

        // Then
        assertThat(movies).isEqualTo(expectedResponse)
        coVerify(exactly = 1) { movieApiService.getMoviesByGenreIds(genreIds) }
    }

    @Test
    fun `getMoviesByGenreIds should throw NetworkException when api call fails`() = runTest {
        // Given
        val genreIds = listOf(28L)
        coEvery { movieApiService.getMoviesByGenreIds(genreIds) } throws NetworkException()

        // When & Then
        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getMoviesByGenreIds(genreIds)
        }
    }
}
package com.amsterdam.remotedatasource.datasource

import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.remotedatasource.api.MovieApiService
import com.amsterdam.remotedatasource.util.actorSearchItemDto
import com.amsterdam.remotedatasource.util.remoteMovieDetailsResponse
import com.amsterdam.remotedatasource.util.remoteMovieItemDto
import com.amsterdam.repository.dto.remote.RatingRemoteResponse
import com.amsterdam.repository.dto.remote.ActorSearchRemoteResponse
import com.amsterdam.repository.dto.remote.CastAndCrewRemoteResponse
import com.amsterdam.repository.dto.remote.MovieRemoteResponse
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
    private lateinit var movieRemoteDataSourceImpl: MovieRemoteDataDataSourceImpl

    @BeforeEach
    fun setUp() {
        movieApiService = mockk()
        movieRemoteDataSourceImpl = MovieRemoteDataDataSourceImpl(movieApiService)
    }

    @Test
    fun `getMoviesByKeyword should return a list of movies when successful`() = runTest {
        // Given
        val keyword = "Inception"
        val page = 1
        val expectedResponse = MovieRemoteResponse(
            page = 1,
            results = listOf(
                remoteMovieItemDto
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
        val expectedResponse = MovieRemoteResponse(
            page = 1,
            results = listOf(
                remoteMovieItemDto
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
        val expectedResponse = ActorSearchRemoteResponse(
            page = 1,
            totalPages = 1,
            totalResults = 1,
            actors = listOf(
                actorSearchItemDto
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
        val expectedResponse = ActorSearchRemoteResponse(
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
            val expectedResponse = MovieRemoteResponse(
                page = 1,
                results = listOf(
                    remoteMovieItemDto

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
        val expectedResponse = CastAndCrewRemoteResponse(
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
            val expectedResponse = remoteMovieDetailsResponse
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
        val expectedResponse = MovieRemoteResponse(
            page = 1,
            results = listOf(remoteMovieItemDto),
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
        val expectedResponse = MovieRemoteResponse(
            page = 1,
            results = listOf(remoteMovieItemDto),
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
        val expectedResponse = MovieRemoteResponse(
            page = page,
            results = listOf(remoteMovieItemDto),
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
        val expectedResponse = MovieRemoteResponse(
            page = 1,
            results = listOf(
                remoteMovieItemDto
            ),
            totalPages = 1,
            totalResults = 1
        )
        coEvery { movieApiService.getMoviesByGenreIds(genreIds, 1) } returns expectedResponse

        // When
        val movies = movieRemoteDataSourceImpl.getMoviesByGenreIds(genreIds, 1)

        // Then
        assertThat(movies).isEqualTo(expectedResponse)
        coVerify(exactly = 1) { movieApiService.getMoviesByGenreIds(genreIds, 1) }
    }

    @Test
    fun `getMoviesByGenreIds should throw NetworkException when api call fails`() = runTest {
        // Given
        val genreIds = listOf(28L)
        coEvery { movieApiService.getMoviesByGenreIds(genreIds, 1) } throws NetworkException()

        // When & Then
        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getMoviesByGenreIds(genreIds, 1)
        }
    }

    @Test
    fun `setMovieRate should return RatingResponse when successful`() = runTest {
        // Given
        val movieId = 550L
        val rating = 5.0f
        val expectedResponse = RatingRemoteResponse(statusCode = 12, statusMessage = "success")

        coEvery {
            movieApiService.postMovieRating(
                movieId,
                rating,
            )
        } returns expectedResponse

        // When
        val result = movieRemoteDataSourceImpl.setMovieRate(rating, movieId)

        // Then
        assertThat(result).isEqualTo(expectedResponse)
        coVerify(exactly = 1) { movieApiService.postMovieRating(movieId, rating) }


    }

    @Test
    fun `getRatedMovie should return Unit when successful`() = runTest {
        //Given
        val expectedResponse = MovieRemoteResponse(
            page = 1,
            results = listOf(remoteMovieItemDto),
            totalPages = 1,
            totalResults = 1
        )
        coEvery { movieApiService.getRatedMovies(0) } returns expectedResponse
        //When
        val result = movieRemoteDataSourceImpl.getRatedMovies()
        //Then
        assertThat(result).isEqualTo(expectedResponse)
        coVerify(exactly = 1) { movieApiService.getRatedMovies(0) }
    }

    @Test
    fun `getRatedMovie should throw NetworkException when api call fails`() = runTest {
        //Given
        coEvery { movieApiService.getRatedMovies(0) } throws NetworkException()
        //When & Then
        assertThrows<NetworkException> {
            movieRemoteDataSourceImpl.getRatedMovies()
        }
    }

    @Test
    fun `deleteMovieRate should call deleteMovieRate in API with correct parameters`() = runTest {
        // Given
        val movieId = 1399L
        coEvery {
            movieApiService.deleteMovieRate(movieId)
        } returns Unit

        // When
        movieRemoteDataSourceImpl.deleteMovieRate(movieId)

        // Then
        coVerify(exactly = 1) {
            movieApiService.deleteMovieRate(movieId)
        }
    }
}






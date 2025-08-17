package com.amsterdam.remotedatasource.datasource

import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.remotedatasource.api.MovieApiService
import com.amsterdam.remotedatasource.util.MOVIE_TEST_ACTOR_ID
import com.amsterdam.remotedatasource.util.MOVIE_TEST_ACTOR_NAME
import com.amsterdam.remotedatasource.util.MOVIE_TEST_COUNTRY_ISO_CODE
import com.amsterdam.remotedatasource.util.MOVIE_TEST_GENRE_ID
import com.amsterdam.remotedatasource.util.MOVIE_TEST_ID
import com.amsterdam.remotedatasource.util.MOVIE_TEST_KEYWORD
import com.amsterdam.remotedatasource.util.MOVIE_TEST_PAGE
import com.amsterdam.remotedatasource.util.MOVIE_TEST_RATING
import com.amsterdam.remotedatasource.util.actorSearchItemRemoteDto
import com.amsterdam.remotedatasource.util.movieDetailsRemoteResponse
import com.amsterdam.remotedatasource.util.movieItemRemoteDto
import com.amsterdam.remotedatasource.util.movieItemRemoteDtoWithNullDate
import com.amsterdam.remotedatasource.util.movieItemRemoteDtoWithNullPoster
import com.amsterdam.repository.dto.remote.ActorSearchRemoteResponse
import com.amsterdam.repository.dto.remote.CastAndCrewRemoteResponse
import com.amsterdam.repository.dto.remote.MovieRemoteResponse
import com.amsterdam.repository.dto.remote.RatingRemoteResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class MovieRemoteDataSourceImplTest {

    private val movieApiService: MovieApiService = mockk()
    private val movieRemoteDataSourceImpl: MovieRemoteDataSourceImpl =
        MovieRemoteDataSourceImpl(movieApiService)

    @Test
    fun `getMoviesByKeyword should return a list of movies and call the API exactly once on a successful call`() =
        runTest {
            coEvery {
                movieApiService.getMoviesByKeyword(
                    MOVIE_TEST_KEYWORD,
                    MOVIE_TEST_PAGE
                )
            } returns movieResponse

            val movies =
                movieRemoteDataSourceImpl.getMoviesByKeyword(MOVIE_TEST_KEYWORD, MOVIE_TEST_PAGE)

            assertThat(movies).isEqualTo(movieResponse)
            coVerify(exactly = 1) {
                movieApiService.getMoviesByKeyword(
                    MOVIE_TEST_KEYWORD,
                    MOVIE_TEST_PAGE
                )
            }
        }

    @Test
    fun `getMoviesByKeyword should throw NetworkException and call the API exactly once when the API call fails`() =
        runTest {
            coEvery {
                movieApiService.getMoviesByKeyword(
                    MOVIE_TEST_KEYWORD,
                    MOVIE_TEST_PAGE
                )
            } throws networkException

            assertThrows<NetworkException> {
                movieRemoteDataSourceImpl.getMoviesByKeyword(MOVIE_TEST_KEYWORD, MOVIE_TEST_PAGE)
            }
            coVerify(exactly = 1) {
                movieApiService.getMoviesByKeyword(
                    MOVIE_TEST_KEYWORD,
                    MOVIE_TEST_PAGE
                )
            }
        }

    @Test
    fun `getMoviesByActorIds should return movies for a given actor and call the API exactly once when the API call is successful`() =
        runTest {
            coEvery { movieApiService.getMoviesByActorId(MOVIE_TEST_ACTOR_ID.toString()) } returns movieResponse

            val movies = movieRemoteDataSourceImpl.getMoviesByActorIds(actorIdsList, MOVIE_TEST_PAGE)

            assertThat(movies).isEqualTo(movieResponse)
            coVerify(exactly = 1) { movieApiService.getMoviesByActorId(MOVIE_TEST_ACTOR_ID.toString()) }
        }

    @Test
    fun `getMoviesByActorIds should throw NetworkException and call the API exactly once when the API call fails`() =
        runTest {
            coEvery { movieApiService.getMoviesByActorId(MOVIE_TEST_ACTOR_ID.toString()) } throws networkException

            assertThrows<NetworkException> {
                movieRemoteDataSourceImpl.getMoviesByActorIds(actorIdsList, MOVIE_TEST_PAGE)
            }
            coVerify(exactly = 1) { movieApiService.getMoviesByActorId(MOVIE_TEST_ACTOR_ID.toString()) }
        }

    @Test
    fun `getActorIdsByName should return actor IDs and call the API exactly once when the API call is successful`() =
        runTest {
            coEvery {
                movieApiService.getActorIdByName(
                    MOVIE_TEST_ACTOR_NAME,
                    MOVIE_TEST_PAGE
                )
            } returns actorSearchResponse

            val actorIds =
                movieRemoteDataSourceImpl.getActorIdsByName(MOVIE_TEST_ACTOR_NAME, MOVIE_TEST_PAGE)

            assertThat(actorIds).containsExactly(MOVIE_TEST_ACTOR_ID)
            coVerify(exactly = 1) {
                movieApiService.getActorIdByName(
                    MOVIE_TEST_ACTOR_NAME,
                    MOVIE_TEST_PAGE
                )
            }
        }

    @Test
    fun `getActorIdsByName should return an empty list and call the API exactly once when no actors are found`() =
        runTest {
            coEvery { movieApiService.getActorIdByName(noActorName, MOVIE_TEST_PAGE) } returns emptyActorSearchResponse

            val actorIds = movieRemoteDataSourceImpl.getActorIdsByName(noActorName, MOVIE_TEST_PAGE)

            assertThat(actorIds).isEmpty()
            coVerify(exactly = 1) { movieApiService.getActorIdByName(noActorName, MOVIE_TEST_PAGE) }
        }

    @Test
    fun `getActorIdsByName should throw NetworkException and call the API exactly once when the API call fails`() =
        runTest {
            coEvery {
                movieApiService.getActorIdByName(
                    testActorName,
                    MOVIE_TEST_PAGE
                )
            } throws networkException

            assertThrows<NetworkException> {
                movieRemoteDataSourceImpl.getActorIdsByName(testActorName, MOVIE_TEST_PAGE)
            }
            coVerify(exactly = 1) { movieApiService.getActorIdByName(testActorName, MOVIE_TEST_PAGE) }
        }

    @Test
    fun `getMoviesByCountryIsoCode should return movies from a specific country and call the API exactly once when the API call is successful`() =
        runTest {
            coEvery {
                movieApiService.getMoviesByCountryIsoCode(
                    MOVIE_TEST_COUNTRY_ISO_CODE,
                    MOVIE_TEST_PAGE
                )
            } returns movieResponse

            val movies = movieRemoteDataSourceImpl.getMoviesByCountryIsoCode(
                MOVIE_TEST_COUNTRY_ISO_CODE,
                MOVIE_TEST_PAGE
            )

            assertThat(movies).isEqualTo(movieResponse)
            coVerify(exactly = 1) {
                movieApiService.getMoviesByCountryIsoCode(
                    MOVIE_TEST_COUNTRY_ISO_CODE,
                    MOVIE_TEST_PAGE
                )
            }
        }

    @Test
    fun `getMoviesByCountryIsoCode should throw NetworkException and call the API exactly once when the API call fails`() =
        runTest {
            coEvery {
                movieApiService.getMoviesByCountryIsoCode(
                    MOVIE_TEST_COUNTRY_ISO_CODE,
                    MOVIE_TEST_PAGE
                )
            } throws networkException

            assertThrows<NetworkException> {
                movieRemoteDataSourceImpl.getMoviesByCountryIsoCode(
                    MOVIE_TEST_COUNTRY_ISO_CODE,
                    MOVIE_TEST_PAGE
                )
            }
            coVerify(exactly = 1) {
                movieApiService.getMoviesByCountryIsoCode(
                    MOVIE_TEST_COUNTRY_ISO_CODE,
                    MOVIE_TEST_PAGE
                )
            }
        }

    @Test
    fun `getCastByMovieId should return cast and crew and call the API exactly once when the API call is successful`() =
        runTest {
            coEvery { movieApiService.getCastByMovieId(MOVIE_TEST_ID) } returns emptyCastAndCrewResponse

            val castAndCrew = movieRemoteDataSourceImpl.getCastByMovieId(MOVIE_TEST_ID)

            assertThat(castAndCrew).isEqualTo(emptyCastAndCrewResponse)
            coVerify(exactly = 1) { movieApiService.getCastByMovieId(MOVIE_TEST_ID) }
        }

    @Test
    fun `getCastByMovieId should throw NetworkException and call the API exactly once when the API call fails`() =
        runTest {
            coEvery { movieApiService.getCastByMovieId(MOVIE_TEST_ID) } throws networkException

            assertThrows<NetworkException> {
                movieRemoteDataSourceImpl.getCastByMovieId(MOVIE_TEST_ID)
            }
            coVerify(exactly = 1) { movieApiService.getCastByMovieId(MOVIE_TEST_ID) }
        }

    @Test
    fun `getMovieDetailsById should return movie details and call the API exactly once when the API call is successful`() =
        runTest {
            coEvery { movieApiService.getMovieDetailsById(MOVIE_TEST_ID) } returns movieDetailsRemoteResponse

            val details = movieRemoteDataSourceImpl.getMovieDetailsById(MOVIE_TEST_ID)

            assertThat(details).isEqualTo(movieDetailsRemoteResponse)
            coVerify(exactly = 1) { movieApiService.getMovieDetailsById(MOVIE_TEST_ID) }
        }

    @Test
    fun `getMovieDetailsById should throw NetworkException and call the API exactly once when the API call fails`() =
        runTest {
            coEvery { movieApiService.getMovieDetailsById(MOVIE_TEST_ID) } throws networkException

            assertThrows<NetworkException> {
                movieRemoteDataSourceImpl.getMovieDetailsById(MOVIE_TEST_ID)
            }
            coVerify(exactly = 1) { movieApiService.getMovieDetailsById(MOVIE_TEST_ID) }
        }

    @Test
    fun `getPopularMovies should return popular movies and call the API exactly once when the API call is successful`() =
        runTest {
            coEvery { movieApiService.getPopularMovies(MOVIE_TEST_PAGE) } returns movieResponse

            val popularMovies = movieRemoteDataSourceImpl.getPopularMovies(MOVIE_TEST_PAGE)

            assertThat(popularMovies).isEqualTo(movieResponse)
            coVerify(exactly = 1) { movieApiService.getPopularMovies(MOVIE_TEST_PAGE) }
        }

    @Test
    fun `getPopularMovies should throw NetworkException and call the API exactly once when the API call fails`() =
        runTest {
            coEvery { movieApiService.getPopularMovies(MOVIE_TEST_PAGE) } throws networkException

            assertThrows<NetworkException> {
                movieRemoteDataSourceImpl.getPopularMovies(MOVIE_TEST_PAGE)
            }
            coVerify(exactly = 1) { movieApiService.getPopularMovies(MOVIE_TEST_PAGE) }
        }

    @Test
    fun `getUpcomingMovies should return upcoming movies and call the API exactly once when the API call is successful`() =
        runTest {
            coEvery { movieApiService.getUpcomingMovies() } returns movieResponse

            val upcomingMovies = movieRemoteDataSourceImpl.getUpcomingMovies()

            assertThat(upcomingMovies).isEqualTo(movieResponse)
            coVerify(exactly = 1) { movieApiService.getUpcomingMovies() }
        }

    @Test
    fun `getUpcomingMovies should throw NetworkException and call the API exactly once when the API call fails`() =
        runTest {
            coEvery { movieApiService.getUpcomingMovies() } throws networkException

            assertThrows<NetworkException> {
                movieRemoteDataSourceImpl.getUpcomingMovies()
            }
            coVerify(exactly = 1) { movieApiService.getUpcomingMovies() }
        }

    @Test
    fun `getTopRatedMovies should return top rated movies and call the API exactly once when the API call is successful`() =
        runTest {
            coEvery { movieApiService.getTopRatedMovies(MOVIE_TEST_PAGE) } returns movieResponse

            val topRatedMovies = movieRemoteDataSourceImpl.getTopRatedMovies(MOVIE_TEST_PAGE)

            assertThat(topRatedMovies).isEqualTo(movieResponse)
            coVerify(exactly = 1) { movieApiService.getTopRatedMovies(MOVIE_TEST_PAGE) }
        }

    @Test
    fun `getTopRatedMovies should throw NetworkException and call the API exactly once when the API call fails`() =
        runTest {
            coEvery { movieApiService.getTopRatedMovies(MOVIE_TEST_PAGE) } throws networkException

            assertThrows<NetworkException> {
                movieRemoteDataSourceImpl.getTopRatedMovies(MOVIE_TEST_PAGE)
            }
            coVerify(exactly = 1) { movieApiService.getTopRatedMovies(MOVIE_TEST_PAGE) }
        }

    @Test
    fun `getMoviesByGenreIds should return movies for given genre IDs and call the API exactly once when the API call is successful`() =
        runTest {
            coEvery {
                movieApiService.getMoviesByGenreIds(
                    genreIdsList,
                    MOVIE_TEST_PAGE
                )
            } returns movieResponse

            val movies = movieRemoteDataSourceImpl.getMoviesByGenreIds(genreIdsList, MOVIE_TEST_PAGE)

            assertThat(movies).isEqualTo(movieResponse)
            coVerify(exactly = 1) { movieApiService.getMoviesByGenreIds(genreIdsList, MOVIE_TEST_PAGE) }
        }

    @Test
    fun `getMoviesByGenreIds should throw NetworkException and call the API exactly once when the API call fails`() =
        runTest {
            coEvery {
                movieApiService.getMoviesByGenreIds(
                    genreIdsList,
                    MOVIE_TEST_PAGE
                )
            } throws networkException

            assertThrows<NetworkException> {
                movieRemoteDataSourceImpl.getMoviesByGenreIds(genreIdsList, MOVIE_TEST_PAGE)
            }
            coVerify(exactly = 1) { movieApiService.getMoviesByGenreIds(genreIdsList, MOVIE_TEST_PAGE) }
        }

    @Test
    fun `getMoviesByGenreId should call the API exactly once with a single genre ID`() = runTest {
        coEvery {
            movieApiService.getMoviesByGenreIds(
                listOf(MOVIE_TEST_GENRE_ID),
                MOVIE_TEST_PAGE
            )
        } returns movieResponse

        val movies = movieRemoteDataSourceImpl.getMoviesByGenreId(MOVIE_TEST_GENRE_ID, MOVIE_TEST_PAGE)

        assertThat(movies).isEqualTo(movieResponse)
        coVerify(exactly = 1) {
            movieApiService.getMoviesByGenreIds(
                listOf(MOVIE_TEST_GENRE_ID),
                MOVIE_TEST_PAGE
            )
        }
    }

    @Test
    fun `setMovieRate should return a RatingResponse and call the API exactly once when the API call is successful`() =
        runTest {
            coEvery {
                movieApiService.postMovieRating(
                    MOVIE_TEST_ID,
                    MOVIE_TEST_RATING
                )
            } returns ratingResponse

            val result = movieRemoteDataSourceImpl.setMovieRate(MOVIE_TEST_RATING, MOVIE_TEST_ID)

            assertThat(result).isEqualTo(ratingResponse)
            coVerify(exactly = 1) { movieApiService.postMovieRating(MOVIE_TEST_ID, MOVIE_TEST_RATING) }
        }

    @Test
    fun `getRatedMovies should return movies with user ratings and call the API exactly once when the API call is successful`() =
        runTest {
            coEvery { movieApiService.getRatedMovies() } returns movieResponse

            val result = movieRemoteDataSourceImpl.getRatedMovies()

            assertThat(result).isEqualTo(movieResponse)
            coVerify(exactly = 1) { movieApiService.getRatedMovies() }
        }

    @Test
    fun `getRatedMovies should throw NetworkException and call the API exactly once when the API call fails`() =
        runTest {
            coEvery { movieApiService.getRatedMovies() } throws networkException

            assertThrows<NetworkException> {
                movieRemoteDataSourceImpl.getRatedMovies()
            }
            coVerify(exactly = 1) { movieApiService.getRatedMovies() }
        }

    @Test
    fun `deleteMovieRate should call the deleteMovieRate API method exactly once with the correct movie ID`() =
        runTest {
            coEvery { movieApiService.deleteMovieRate(MOVIE_TEST_ID) } returns Unit

            movieRemoteDataSourceImpl.deleteMovieRate(MOVIE_TEST_ID)

            coVerify(exactly = 1) { movieApiService.deleteMovieRate(MOVIE_TEST_ID) }
        }

    @Test
    fun `getRandomMoviesWithReleaseDate should return the required number of movies, handling null dates and duplicates, and call the API at least twice`() =
        runTest {
            coEvery { movieApiService.getPopularMovies(any()) } returns moviesWithoutDateResponse andThen moviesWithDateResponse

            val result =
                movieRemoteDataSourceImpl.getRandomMoviesWithReleaseDate(3)

            assertThat(result.size).isEqualTo(3)
            assertThat(result).containsNoDuplicates()
            coVerify(atLeast = 2) { movieApiService.getPopularMovies(any()) }
        }

    @Test
    fun `getRandomMoviesWithReleaseDate should break the loop and call the API exactly once when enough movies are collected`() =
        runTest {
            coEvery { movieApiService.getPopularMovies(any()) } returns moviesWithDateResponse

            val result =
                movieRemoteDataSourceImpl.getRandomMoviesWithReleaseDate(2)

            assertThat(result.size).isEqualTo(2)
            coVerify(exactly = 1) { movieApiService.getPopularMovies(any()) }
        }

    @Test
    fun `getRandomMoviesWithReleaseDate should handle duplicates correctly`() = runTest {
        coEvery { movieApiService.getPopularMovies(any()) } returns responseWithDuplicates

        val result = movieRemoteDataSourceImpl.getRandomMoviesWithReleaseDate(2)

        assertThat(result).containsNoDuplicates()
    }

    @Test
    fun `getRandomMoviesWithPoster should return the required number of movies, handling null posters, and call the API at least twice`() =
        runTest {
            coEvery { movieApiService.getPopularMovies(any()) } returns moviesWithoutPosterResponse andThen moviesWithPosterResponse

            val result =
                movieRemoteDataSourceImpl.getRandomMoviesWithPoster(3)

            assertThat(result.size).isEqualTo(3)
            coVerify(atLeast = 2) { movieApiService.getPopularMovies(any()) }
        }

    @Test
    fun `getRandomMoviesWithPoster should break the loop and call the API exactly once when enough movies are collected`() =
        runTest {
            coEvery { movieApiService.getPopularMovies(any()) } returns moviesWithPosterResponse

            val result =
                movieRemoteDataSourceImpl.getRandomMoviesWithPoster(2)

            assertThat(result.size).isEqualTo(2)
            coVerify(exactly = 1) { movieApiService.getPopularMovies(any()) }
        }

    @Test
    fun `getRandomMoviesWithPoster should handle duplicates correctly`() = runTest {
        coEvery { movieApiService.getPopularMovies(any()) } returns responseWithDuplicates

        val result =
            movieRemoteDataSourceImpl.getRandomMoviesWithPoster(2)

        assertThat(result).containsNoDuplicates()
    }

    private val networkException = NetworkException()

    private val movieResponse = MovieRemoteResponse(
        page = MOVIE_TEST_PAGE,
        results = listOf(movieItemRemoteDto),
        totalPages = 1,
        totalResults = 1
    )

    private val emptyMovieResponse = MovieRemoteResponse(
        page = MOVIE_TEST_PAGE,
        results = emptyList(),
        totalPages = 0,
        totalResults = 0
    )

    private val actorSearchResponse = ActorSearchRemoteResponse(
        page = MOVIE_TEST_PAGE,
        totalPages = 1,
        totalResults = 1,
        actors = listOf(actorSearchItemRemoteDto)
    )

    private val emptyActorSearchResponse = ActorSearchRemoteResponse(
        page = MOVIE_TEST_PAGE,
        totalPages = 1,
        totalResults = 0,
        actors = emptyList()
    )

    private val emptyCastAndCrewResponse = CastAndCrewRemoteResponse(
        id = MOVIE_TEST_ID,
        cast = emptyList(),
        crew = emptyList()
    )

    private val ratingResponse = RatingRemoteResponse(
        statusCode = 12,
        statusMessage = "success"
    )

    private val moviesWithDate = listOf(
        movieItemRemoteDto,
        movieItemRemoteDto.copy(id = 2),
        movieItemRemoteDto.copy(id = 3),
        movieItemRemoteDto.copy(id = 4)
    )

    private val moviesWithoutDate = listOf(movieItemRemoteDtoWithNullDate)

    private val moviesWithPoster = listOf(
        movieItemRemoteDto,
        movieItemRemoteDto.copy(id = 2),
        movieItemRemoteDto.copy(id = 3),
        movieItemRemoteDto.copy(id = 4)
    )

    private val moviesWithoutPoster = listOf(movieItemRemoteDtoWithNullPoster)

    private val moviesWithDuplicates = listOf(
        movieItemRemoteDto,
        movieItemRemoteDto.copy(id = 2),
        movieItemRemoteDto.copy(id = 2)
    )

    private val responseWithDuplicates = MovieRemoteResponse(1, moviesWithDuplicates, 1, 10)

    private val moviesWithDateResponse = MovieRemoteResponse(2, moviesWithDate, 2, 10)
    private val moviesWithoutDateResponse = MovieRemoteResponse(1, moviesWithoutDate, 1, 10)
    private val moviesWithPosterResponse = MovieRemoteResponse(2, moviesWithPoster, 2, 10)
    private val moviesWithoutPosterResponse = MovieRemoteResponse(1, moviesWithoutPoster, 1, 10)

    private val noActorName = "No Actor"
    private val testActorName = "test"
    private val actorIdsList = listOf(MOVIE_TEST_ACTOR_ID)
    private val genreIdsList = listOf(MOVIE_TEST_GENRE_ID)

}
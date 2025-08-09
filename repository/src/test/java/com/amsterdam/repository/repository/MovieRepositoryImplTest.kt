package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.entity.Actor
import com.amsterdam.entity.Country
import com.amsterdam.entity.Gender
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.repository.datasource.local.AppPreferences
import com.amsterdam.repository.datasource.local.AuthenticationLocalDataSource
import com.amsterdam.repository.datasource.local.CategoryLocalDataSource
import com.amsterdam.repository.datasource.local.MovieLocalDataSource
import com.amsterdam.repository.datasource.remote.CategoryRemoteSource
import com.amsterdam.repository.datasource.remote.MovieRemoteSource
import com.amsterdam.repository.dto.remote.RatingResponse
import com.amsterdam.repository.dto.remote.RemoteMovieResponse
import com.amsterdam.repository.mapper.remote.toEntity
import com.amsterdam.repository.mapper.remote.toMovieEntityList
import com.amsterdam.repository.security.CryptoData
import com.amsterdam.repository.utils.remoteCastAndCrewResponse
import com.amsterdam.repository.utils.remoteMovieItemDto
import com.amsterdam.repository.utils.remoteMovieResponse
import com.amsterdam.repository.utils.remoteUserRatedMovie
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class MovieRepositoryImplTest {

    private lateinit var movieRepository: MovieRepositoryImpl

    private val categoryLocalDataSource: CategoryLocalDataSource = mockk()
    private val movieLocalDataSource: MovieLocalDataSource = mockk()
    private val categoryRemoteSource: CategoryRemoteSource = mockk()
    private val movieRemoteDataSource: MovieRemoteSource = mockk()
    private val authenticationLocalDataSource: AuthenticationLocalDataSource = mockk()
    private val preferences: AppPreferences = mockk()
    private val cryptoData: CryptoData = mockk()

    @BeforeEach
    fun setUp() {
        movieRepository = MovieRepositoryImpl(
            categoryLocalDataSource,
            movieLocalDataSource,
            categoryRemoteSource,
            movieRemoteDataSource,
            authenticationLocalDataSource,
            preferences,
            cryptoData
        )
    }

    @Test
    fun `getMoviesByKeyword should return list of movies`() = runTest {
        // Given
        val keyword = "keyword"
        val page = 1
        val moviesPerPage = 20
        val expectedMovies = listOf(
            remoteMovieItemDto
        )
        coEvery {
            movieRemoteDataSource.getMoviesByKeyword(keyword, page)
        } returns RemoteMovieResponse(
            page = 1, results = expectedMovies, totalPages = 1, totalResults = 1
        )
        // When
        val result = movieRepository.getMoviesByKeyword(keyword, page, moviesPerPage)
        // Then
        assertThat(result).isEqualTo(expectedMovies.toMovieEntityList())
    }

    @Test
    fun `getMoviesByActor should return movies list from remote`() = runTest {
        // Given
        val actorName = "Tom Hanks"
        val actorIds = listOf(1, 2)
        val page = 1
        val perPage = 20
        val movie11 = remoteMovieItemDto.toEntity()
        val expectedMovies = listOf(movie11)


        coEvery {
            movieRemoteDataSource.getActorIdsByName(actorName, page)
        } returns actorIds


        coEvery {
            movieRemoteDataSource.getMoviesByActorIds(actorIds, page)
        } returns remoteMovieResponse

        // When
        val result = movieRepository.getMoviesByActor(actorName, page, perPage)

        // Then
        assertThat(result).isEqualTo(expectedMovies)
        coVerify {
            movieRemoteDataSource.getActorIdsByName(actorName, page)
            movieRemoteDataSource.getMoviesByActorIds(actorIds, page)
        }
    }

    @Test
    fun `getMoviesByCountry returns expected movies`() = runTest {
        // Given
        val country = Country("EG", "Egypt")
        val page = 1
        val moviesPerPage = 10
        val expectedMovies = listOf(
            remoteMovieItemDto.toEntity()
        )

        coEvery {
            movieRemoteDataSource.getMoviesByCountryIsoCode(any(), any())
        } returns remoteMovieResponse

        // When
        val result = movieRepository.getMoviesByCountry(country, page, moviesPerPage)

        // Then
        assertThat(result).isEqualTo(expectedMovies)
        coVerify(exactly = 1) {
            movieRemoteDataSource.getMoviesByCountryIsoCode(any(), any())
        }
    }

    @Test
    fun `getActorsByMovieId should return mapped actors from remote`() = runTest {
        // Given
        val movieId = 123L
        val remoteActors = listOf(
            Actor(
                id = 1,
                name = "Actor One",
                imageUrl = "https://image.tmdb.org/t/p/w500/img1.jpg",
                popularity =100.0,
                gender = Gender.Female
            )
        )

        coEvery { movieRemoteDataSource.getCastByMovieId(movieId) } returns remoteCastAndCrewResponse

        // When
        val result = movieRepository.getActorsByMovieId(movieId)

        // Then
        assertThat(result).isEqualTo(remoteActors)

        coVerify(exactly = 1) { movieRemoteDataSource.getCastByMovieId(movieId) }
    }

    @Test
    fun `setMovieRate should call remoteDataSource with decrypted sessionId`() = runTest {
        // Given
        val movieId = 101L
        val rate = 8
        val encryptedSession = "encrypted-session"
        val decryptedSession = "decrypted-session"
        val expectedResult = RatingResponse(
            statusCode = 200,
            statusMessage = "Success"
        )

        coEvery { authenticationLocalDataSource.getCachedSessionId() } returns encryptedSession
        coEvery { cryptoData.decryptString(encryptedSession) } returns decryptedSession
        coEvery { movieRemoteDataSource.setMovieRate(rate.toFloat(), movieId, decryptedSession) } returns expectedResult

        // When
        val result =  movieRepository.setMovieRate(rate, movieId)

        // Then
        coVerify(exactly = 1) {
            movieRemoteDataSource.setMovieRate(rate.toFloat(), movieId, decryptedSession)
        }
    }

    @Test
    fun `getUserRatedMovies should return user rated movies from remote`() = runTest {
        // Given
        val encryptedSession = "encrypted-session"
        val decryptedSession = "decrypted-session"
        val remoteMovies = listOf(
            remoteUserRatedMovie
        )

        coEvery { authenticationLocalDataSource.getCachedSessionId() } returns encryptedSession
        coEvery { cryptoData.decryptString(encryptedSession) } returns decryptedSession
        coEvery { movieRemoteDataSource.getRatedMovies(decryptedSession) } returns remoteMovieResponse

        // When
        val result = movieRepository.getUserRatedMovies()

        // Then
        assertThat(result).isEqualTo(remoteMovies)
        coVerify(exactly = 1) {
            movieRemoteDataSource.getRatedMovies(decryptedSession)
        }
    }

    @Test
    fun `deleteMovieRate should call remoteDataSource with correct sessionId`() = runTest {
        // Given
        val movieId = 999L
        val encryptedSession = "encrypted-session"
        val decryptedSession = "decrypted-session"

        coEvery { authenticationLocalDataSource.getCachedSessionId() } returns encryptedSession
        coEvery { cryptoData.decryptString(encryptedSession) } returns decryptedSession
        coEvery { movieRemoteDataSource.deleteMovieRate(movieId, decryptedSession) } just Runs

        // When
        movieRepository.deleteMovieRate(movieId)

        // Then
        coVerify(exactly = 1) {
            movieRemoteDataSource.deleteMovieRate(movieId, decryptedSession)
        }
    }

    @Test
    fun `getMoviesByGenres should return list of movies for given genres`() = runTest {
        // Given
        val genres = listOf(
            MovieGenre.ALL,
            MovieGenre.ACTION,
            MovieGenre.ADVENTURE
        )
        val page = 1

        val expectedDtoGenres = listOf(
            35L,
            28L,
            12L
        )

        coEvery {
            movieRemoteDataSource.getMoviesByGenreIds(expectedDtoGenres, page)
        } returns remoteMovieResponse

        // When
        val result = movieRepository.getMoviesByGenres(genres, page)

        // Then
        assertThat(result).isEqualTo(remoteMovieResponse.results.toMovieEntityList())
        coVerify {
            movieRemoteDataSource.getMoviesByGenreIds(expectedDtoGenres, page)
        }
    }
}
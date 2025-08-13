package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.entity.Actor
import com.amsterdam.entity.Country
import com.amsterdam.entity.Gender
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.repository.datasource.remote.MovieRemoteSource
import com.amsterdam.repository.dto.remote.RemoteMovieResponse
import com.amsterdam.repository.mapper.remote.toMovieEntityList
import com.amsterdam.repository.utils.remoteCastAndCrewResponse
import com.amsterdam.repository.utils.remoteMovieItemDto
import com.amsterdam.repository.utils.remoteUserRatedMovie
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class MovieRepositoryImplTest {
    private val movieRemoteDataSource: MovieRemoteSource = mockk()

    val movieRepository: MovieRepository by lazy {
        MovieRepositoryImpl(
            categoryLocalDataSource = mockk(),
            movieLocalDataSource = mockk(),
            categoryRemoteSource = mockk(),
            movieRemoteDataSource = movieRemoteDataSource,
            preferences = mockk()
        )
    }

    @Test
    fun `getMoviesByKeyword should return movies when remote returns results`() = runTest {
        coEvery { movieRemoteDataSource.getMoviesByKeyword(keyword, page) } returns remoteMovieResponse

        val result = movieRepository.getMoviesByKeyword(keyword, page, moviesPerPage)

        assertThat(result).isEqualTo(expectedEntityMovies)
    }

    @Test
    fun `getMoviesByActor should return movies when actor exists`() = runTest {
        coEvery { movieRemoteDataSource.getActorIdsByName(actorName, page) } returns actorIds
        coEvery { movieRemoteDataSource.getMoviesByActorIds(actorIds, page) } returns remoteMovieResponse

        val result = movieRepository.getMoviesByActor(actorName, page, moviesPerPage)

        assertThat(result).isEqualTo(expectedEntityMovies)
    }

    @Test
    fun `getMoviesByActor should return empty list when no actors found`() = runTest {
        coEvery { movieRemoteDataSource.getActorIdsByName(actorName, page) } returns emptyList()

        val result = movieRepository.getMoviesByActor(actorName, page, moviesPerPage)

        assertThat(result).isEmpty()
    }

    @Test
    fun `getMoviesByCountry should return movies when remote returns results`() = runTest {
        coEvery { movieRemoteDataSource.getMoviesByCountryIsoCode(country.countryIsoCode, page) } returns remoteMovieResponse

        val result = movieRepository.getMoviesByCountry(country, page, moviesPerPage)

        assertThat(result).isEqualTo(expectedEntityMovies)
    }

    @Test
    fun `getActorsByMovieId should return mapped actors when remote returns cast`() = runTest {
        coEvery { movieRemoteDataSource.getCastByMovieId(movieId) } returns remoteCastAndCrewResponse

        val result = movieRepository.getActorsByMovieId(movieId)

        assertThat(result).isEqualTo(remoteActors)
    }

    @Test
    fun `getUserRatedMovies should return rated movies when remote returns results`() = runTest {
        coEvery { movieRemoteDataSource.getRatedMovies() } returns remoteMovieResponse

        val result = movieRepository.getUserRatedMovies()

        assertThat(result).isEqualTo(remoteMovies)
    }

    @Test
    fun `deleteMovieRate should call remote once`() = runTest {
        coEvery { movieRemoteDataSource.deleteMovieRate(movieId) } just Runs

        movieRepository.deleteMovieRate(movieId)

        coVerify(exactly = 1) { movieRemoteDataSource.deleteMovieRate(movieId) }
    }


    @Test
    fun `getMoviesByGenres should return movies when remote returns results`() = runTest {
        coEvery { movieRemoteDataSource.getMoviesByGenreIds(expectedDtoGenres, page)
        } returns remoteMovieResponse

        val result = movieRepository.getMoviesByGenres(genres, page)

        assertThat(result).isEqualTo(remoteMovieResponse.results.toMovieEntityList())
    }

    private val expectedRemoteMovies = listOf(remoteMovieItemDto)

    private val remoteMovieResponse = RemoteMovieResponse(
        page = 1,
        results = expectedRemoteMovies,
        totalPages = 1,
        totalResults = 1
    )
    private val keyword = "keyword"
    private val page = 1
    private val moviesPerPage = 20
    private val actorName = "Tom Hanks"
    private val actorIds = listOf(1, 2)
    private val country = Country("EG", "Egypt")
    private val expectedEntityMovies = expectedRemoteMovies.toMovieEntityList()
    private val movieId = 123L
    val remoteActors = listOf(
        Actor(
            id = 1,
            name = "Actor One",
            imageUrl = "https://image.tmdb.org/t/p/w500/img1.jpg",
            popularity = 100.0,
            gender = Gender.Female
        )
    )

    val remoteMovies = listOf(remoteUserRatedMovie)

    val genres = listOf(MovieGenre.ALL, MovieGenre.ACTION, MovieGenre.ADVENTURE)
    val expectedDtoGenres = listOf(35L, 28L, 12L)
}
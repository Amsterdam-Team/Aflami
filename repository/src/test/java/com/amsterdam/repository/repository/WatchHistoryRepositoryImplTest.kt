package com.amsterdam.repository.repository

import com.amsterdam.entity.Movie
import com.amsterdam.entity.WatchHistory
import com.amsterdam.repository.datasource.local.WatchHistoryLocalDataSource
import com.amsterdam.repository.dto.local.LocalMovieDto
import com.amsterdam.repository.dto.local.WatchHistoryDto
import com.amsterdam.repository.mapper.local.MovieLocalMapper
import com.amsterdam.repository.mapper.local.WatchHistoryMapper
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class WatchHistoryRepositoryImplTest {

    private lateinit var repository: WatchHistoryRepositoryImpl

    private val watchHistoryLocalDataSource: WatchHistoryLocalDataSource = mockk()
    private val movieLocalMapper: MovieLocalMapper = mockk()
    private val watchHistoryMapper: WatchHistoryMapper = mockk()

    @BeforeEach
    fun setup() {
        clearAllMocks()
        repository = WatchHistoryRepositoryImpl(
            watchHistoryLocalDataSource = watchHistoryLocalDataSource,
            movieLocalMapper = movieLocalMapper,
            watchHistoryMapper = watchHistoryMapper
        )
    }

    @Test
    fun `addToWatchHistory should map and save item to local data source`() = runTest {
        val lastWatchedInstant = Instant.fromEpochMilliseconds(System.currentTimeMillis())

        val watchHistoryItem = WatchHistory(
            movieId = 1L,
            lastWatchedTime = lastWatchedInstant
        )
        val watchHistoryDto = WatchHistoryDto(
            movieId = 1L,
            storedLanguage = "mocked_language",
            lastWatchedTime = lastWatchedInstant
        )

        every { watchHistoryMapper.toDto(watchHistoryItem, emptyList()) } returns watchHistoryDto

        coJustRun { watchHistoryLocalDataSource.addToWatchHistory(watchHistoryDto) }

        repository.addToWatchHistory(watchHistoryItem)

        coVerify(exactly = 1) { watchHistoryLocalDataSource.addToWatchHistory(watchHistoryDto) }
        verify(exactly = 1) { watchHistoryMapper.toDto(watchHistoryItem, emptyList()) }
    }

    @Test
    fun `addToWatchHistory should throw exception if local data source fails`() = runTest {
        val lastWatchedInstant = Instant.fromEpochMilliseconds(System.currentTimeMillis())

        val watchHistoryItem = WatchHistory(
            movieId = 1L,
            lastWatchedTime = lastWatchedInstant
        )
        val watchHistoryDto = WatchHistoryDto(
            movieId = 1L,
            storedLanguage = "mocked_language",
            lastWatchedTime = lastWatchedInstant
        )
        val expectedException = RuntimeException("Database write error!")

        every { watchHistoryMapper.toDto(watchHistoryItem, emptyList()) } returns watchHistoryDto

        coEvery { watchHistoryLocalDataSource.addToWatchHistory(watchHistoryDto) } throws expectedException

        val thrownException = assertThrows<RuntimeException> {
            repository.addToWatchHistory(watchHistoryItem)
        }

        assertThat(thrownException).isEqualTo(expectedException)
        verify(exactly = 1) { watchHistoryMapper.toDto(watchHistoryItem, emptyList()) }
        coVerify(exactly = 1) { watchHistoryLocalDataSource.addToWatchHistory(watchHistoryDto) }
    }

    @Test
    fun `getContinueWatchingMovies should return mapped movies from local data source`() = runTest {
        val localMovieDto1 = LocalMovieDto(
            movieId = 1,
            storedLanguage = "en",
            name = "Movie A",
            description = "Overview A",
            poster = "poster1.jpg",
            releaseDate = LocalDate(2022, 1, 1),
            popularity = 75.5,
            rating = 8.0f,
            originCountry = "USA",
            movieLength = 120,
            hasVideo = true
        )
        val localMovieDto2 = LocalMovieDto(
            movieId = 2,
            storedLanguage = "en",
            name = "Movie B",
            description = "Overview B",
            poster = "poster2.jpg",
            releaseDate = LocalDate(2022, 2, 1),
            popularity = 80.0,
            rating = 7.0f,
            originCountry = "UK",
            movieLength = 100,
            hasVideo = false
        )
        val localMovies = listOf(localMovieDto1, localMovieDto2)

        val movie1 = Movie(
            id = localMovieDto1.movieId,
            name = localMovieDto1.name,
            description = localMovieDto1.description,
            posterUrl = localMovieDto1.poster,
            releaseDate = localMovieDto1.releaseDate,
            categories = emptyList(),
            rating = localMovieDto1.rating,
            popularity = localMovieDto1.popularity,
            originCountry = localMovieDto1.originCountry,
            runTimeInMinutes = localMovieDto1.movieLength,
            hasVideo = localMovieDto1.hasVideo
        )
        val movie2 = Movie(
            id = localMovieDto2.movieId,
            name = localMovieDto2.name,
            description = localMovieDto2.description,
            posterUrl = localMovieDto2.poster,
            releaseDate = localMovieDto2.releaseDate,
            categories = emptyList(),
            rating = localMovieDto2.rating,
            popularity = localMovieDto2.popularity,
            originCountry = localMovieDto2.originCountry,
            runTimeInMinutes = localMovieDto2.movieLength,
            hasVideo = localMovieDto2.hasVideo
        )
        val mappedMovies = listOf(movie1, movie2)

        coEvery { watchHistoryLocalDataSource.getContinueWatching() } returns flowOf(localMovies)
        every { movieLocalMapper.toEntityList(localMovies) } returns mappedMovies

        repository.getContinueWatchingMovies().collect { result ->
            assertThat(result).isEqualTo(mappedMovies)
        }

        coVerify(exactly = 1) { watchHistoryLocalDataSource.getContinueWatching() }
        verify(exactly = 1) { movieLocalMapper.toEntityList(localMovies) }
    }

    @Test
    fun `getContinueWatchingMovies should handle empty list from local data source`() = runTest {
        val localMovies = emptyList<LocalMovieDto>()
        val mappedMovies = emptyList<Movie>()

        coEvery { watchHistoryLocalDataSource.getContinueWatching() } returns flowOf(localMovies)
        every { movieLocalMapper.toEntityList(localMovies) } returns mappedMovies

        repository.getContinueWatchingMovies().collect { result ->
            assertThat(result).isEmpty()
        }

        coVerify(exactly = 1) { watchHistoryLocalDataSource.getContinueWatching() }
        verify(exactly = 1) { movieLocalMapper.toEntityList(localMovies) }
    }

    @Test
    fun `getContinueWatchingMovies should throw exception if local data source fails`() = runTest {
        val expectedException = RuntimeException("Flow error!")

        coEvery { watchHistoryLocalDataSource.getContinueWatching() } returns flowOf(emptyList())
        every { movieLocalMapper.toEntityList(any()) } throws expectedException

        val thrownException = assertThrows<RuntimeException> {
            repository.getContinueWatchingMovies().collect { }
        }

        assertThat(thrownException).isEqualTo(expectedException)
        coVerify(exactly = 1) { watchHistoryLocalDataSource.getContinueWatching() }
        verify(exactly = 1) { movieLocalMapper.toEntityList(any()) }
    }
}
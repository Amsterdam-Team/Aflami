package com.amsterdam.localdatasource.roomDataBase.datasource

import com.amsterdam.localdatasource.roomDataBase.daos.MovieCategoryInterestDao
import com.amsterdam.localdatasource.roomDataBase.daos.MovieDao
import com.amsterdam.localdatasource.utils.createMovie
import com.amsterdam.repository.dto.local.MovieCategoryCrossRefDto
import com.amsterdam.repository.dto.local.relation.MovieWithCategories
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MovieLocalDataSourceImplTest {

    private lateinit var movieDao: MovieDao
    private lateinit var interestDao: MovieCategoryInterestDao
    private lateinit var dataSource: MovieLocalDataSourceImpl

    @BeforeEach
    fun setUp() {
        movieDao = mockk(relaxed = true)
        interestDao = mockk(relaxed = true)
        dataSource = MovieLocalDataSourceImpl(movieDao, interestDao)
    }

    @Test
    fun `getMovieById should return the correct movie`() = runTest {
        //Given
        val storedLanguage = "en"
        val movie = createMovie(movieId = 42, storedLanguage = storedLanguage)
        coEvery { movieDao.getMovieById(42, storedLanguage) } returns movie
        //When
        val result = dataSource.getMovieById(42, storedLanguage)
        //Then
        assertThat(result).isEqualTo(movie)
    }

    @Test
    fun `incrementGenreInterest should call incrementInterest in the interestDao with correct genre`() =
        runTest {
            //Given
            val categoryId = 1L
            //When
            dataSource.incrementGenreInterest(categoryId)
            //Then
            coVerify(exactly = 1) { interestDao.incrementInterest(categoryId) }
        }

    @Test
    fun `addMovieWithCategories should insert the movie and corresponding category cross-references`() =
        runTest {
            // Given
            val movie = createMovie(movieId = 1L, storedLanguage = "en")
            val categories = listOf(1L)
            val expectedCrossRefs = listOf(
                MovieCategoryCrossRefDto(
                    movieId = movie.movieId,
                    categoryId = categories.first(),
                    storedLanguage = "en"
                )
            )

            // When
            dataSource.upsertMovieWithCategories(movie, categories, "en")

            // Then
            coVerify(exactly = 1) { movieDao.upsertMovie(movie) }
            coVerify(exactly = 1) { movieDao.upsertMovieCategoryCrossRefs(expectedCrossRefs) }
        }

    @Test
    fun `insertMovie should call insertMovie in the movieDao with provided movie`() = runTest {
        //Given
        val storedLanguage = "en"
        val movie = createMovie(movieId = 42, storedLanguage = storedLanguage)
        //When
        dataSource.upsertMovie(movie)
        // Then
        coVerify(exactly = 1) { movieDao.upsertMovie(movie) }
    }

    @Test
    fun `getPopularMovies should return non empty list of movie`() = runTest {
        //Given
        val storedLanguage = "en"
        coEvery { movieDao.getPopularMovies(storedLanguage) } returns moviesWithCategories
        //When
        val result = dataSource.getPopularMovies(storedLanguage)
        //Then
        assertThat(result).isEqualTo(moviesWithCategories)
    }

    @Test
    fun `getUpcomingMovies should return non empty list of movie`() = runTest {
        //Given
        val storedLanguage = "en"
        coEvery { movieDao.getUpcomingMovies(storedLanguage) } returns moviesWithCategories
        //When
        val result = dataSource.getUpcomingMovies(storedLanguage)
        //Then
        assertThat(result).isEqualTo(moviesWithCategories)
    }

    @Test
    fun `getTopRatedMovies should return non empty list of movie`() = runTest {
        //Given
        val storedLanguage = "en"
        val localMovies = listOf(createMovie(movieId = 42, storedLanguage = storedLanguage))
        coEvery { movieDao.getTopRatedMovies(storedLanguage) } returns localMovies
        //When
        val result = dataSource.getTopRatedMovies(storedLanguage)
        //Then
        assertThat(result).isEqualTo(localMovies)
    }

    @Test
    fun `addPopularMovies should call insertPopularMovies in the movieDao`() = runTest {
        //Given
        val localMovies = listOf(createMovie(movieId = 42, storedLanguage = "en"))
        //When
        dataSource.upsertPopularMovies(localMovies)
        // Then
        coVerify(exactly = 1) {
            movieDao.upsertPopularMovies(
                match { list ->
                    list.size == 1 &&
                        list[0].movieId == 42L &&
                        list[0].storedLanguage == "en"
            }
                    )
        }
    }

    @Test
    fun `addTopRatedMovies should call insertTopRatedMovies in the movieDao`() = runTest {
        //Given
        val localMovies = listOf(createMovie(movieId = 42, storedLanguage = "en"))
        //When
        dataSource.upsertTopRatedMovies(localMovies)
        // Then
        coVerify(exactly = 1) {
            movieDao.upsertTopRatedMovies(match { list ->
                list.size == 1 &&
                        list[0].movieId == 42L &&
                        list[0].storedLanguage == "en"
            })
        }
    }

    @Test
    fun `addUpcomingMovies should call insertUpcomingMovies in the movieDao`() = runTest {
        //Given
        val localMovies = listOf(createMovie(movieId = 42, storedLanguage = "en"))
        //When
        dataSource.upsertUpcomingMovies(localMovies)
        // Then
        coVerify(exactly = 1) {
            movieDao.upsertUpcomingMovies(match { list ->
                list.size == 1 &&
                        list[0].movieId == 42L &&
                        list[0].storedLanguage == "en"
            })
        }
    }

    @Test
    fun `deleteExpiredPopularMovies should call deleteExpiredPopularMovies`() = runTest {
        //Given
        val expirationTime = Instant.parse("2023-01-01T00:00:00Z")
        val storedLanguage = "en"
        //When
        dataSource.deleteExpiredPopularMovies(expirationTime, storedLanguage)
        //Then
        coVerify(exactly = 1) {
            movieDao.deleteExpiredPopularMovies(
                expirationTime,
                storedLanguage
            )
        }
    }

    @Test
    fun `deleteAllExpiredTopRatedMovies should call deleteAllExpiredTopRatedMovies`() = runTest {
        //Given
        val expirationTime = Instant.parse("2023-01-01T00:00:00Z")
        val storedLanguage = "en"
        //When
        dataSource.deleteAllExpiredTopRatedMovies(expirationTime, storedLanguage)
        //Then
        coVerify(exactly = 1) {
            movieDao.deleteAllExpiredTopRatedMovies(
                expirationTime,
                storedLanguage
            )
        }
    }

    @Test
    fun `deleteExpiredUpcomingMovies should call deleteExpiredUpcomingMovies`() = runTest {
        //Given
        val expirationTime = Instant.parse("2023-01-01T00:00:00Z")
        val storedLanguage = "en"
        //When
        dataSource.deleteExpiredUpcomingMovies(expirationTime, storedLanguage)
        //Then
        coVerify(exactly = 1) {
            movieDao.deleteExpiredUpcomingMovies(
                expirationTime,
                storedLanguage
            )
        }
    }

    val moviesWithCategories = listOf(
        MovieWithCategories(
            movie = createMovie(movieId = 42, storedLanguage = "en"),
            categories = emptyList()
        )
    )

}
package com.amsterdam.localdatasource.roomDataBase.datasource

import com.amsterdam.localdatasource.roomDataBase.daos.MovieCategoryInterestDao
import com.amsterdam.localdatasource.roomDataBase.daos.MovieDao
import com.amsterdam.repository.dto.local.MovieCategoryCrossRefDto
import com.amsterdam.repository.dto.local.MovieLocalDto
import com.amsterdam.repository.dto.local.relation.MovieWithCategories
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class MovieLocalDataSourceImplTest {
    private val movieDao by lazy { mockk<MovieDao>(relaxed = true) }
    private val interestDao by lazy { mockk<MovieCategoryInterestDao>(relaxed = true) }
    private val dataSource by lazy { MovieLocalDataSourceImpl(movieDao, interestDao) }

    @Test
    fun `getMovieById should return the correct movie when there is date returned`() = runTest {
        coEvery { movieDao.getMovieById(movie.movieId, movie.storedLanguage) } returns movie

        val result = dataSource.getMovieById(movie.movieId, movie.storedLanguage)

        assertThat(result).isEqualTo(movie)
    }

    @Test
    fun `getMovieById should call getMovieById from the movieDao with correct data when called`() =
        runTest {
            dataSource.getMovieById(movie.movieId, movie.storedLanguage)

            coVerify(exactly = 1) { movieDao.getMovieById(movie.movieId, movie.storedLanguage) }
        }

    @Test
    fun `incrementGenreInterest should call incrementInterest in the interestDao with correct data`() =
        runTest {
            dataSource.incrementGenreInterest(categoryId)

            coVerify(exactly = 1) { interestDao.incrementInterest(categoryId) }
        }

    @Test
    fun `upsertMovieWithCategories should call upsertMovie and upsertMovieCategoryCrossRefs in the movieDao with correct data`() =
        runTest {
            dataSource.upsertMovieWithCategories(movie, categories, storedLanguage)

            coVerify(exactly = 1) { movieDao.upsertMovie(movie) }
            coVerify(exactly = 1) { movieDao.upsertMovieCategoryCrossRefs(movieCategoryCrossRefs) }
        }

    @Test
    fun `upsertMovie should call upsertMovie in the movieDao with provided movie`() = runTest {
        dataSource.upsertMovie(movie)

        coVerify(exactly = 1) { movieDao.upsertMovie(movie) }
    }

    @Test
    fun `getPopularMovies should return moviesWithCategories list when data returned`() = runTest {
        coEvery { movieDao.getPopularMovies(storedLanguage) } returns moviesWithCategories

        val result = dataSource.getPopularMovies(storedLanguage)

        assertThat(result).isEqualTo(moviesWithCategories)
    }

    @Test
    fun `getUpcomingMovies should return moviesWithCategories list when data returned`() = runTest {
        coEvery { movieDao.getUpcomingMovies(storedLanguage) } returns moviesWithCategories

        val result = dataSource.getUpcomingMovies(storedLanguage)

        assertThat(result).isEqualTo(moviesWithCategories)
    }

    @Test
    fun `getTopRatedMovies should return MovieLocalDto list when data returned`() = runTest {
        coEvery { movieDao.getTopRatedMovies(storedLanguage) } returns localMovies

        val result = dataSource.getTopRatedMovies(storedLanguage)

        assertThat(result).isEqualTo(localMovies)
    }

    @Test
    fun `upsertPopularMovies should call upsertMovies and upsertPopularMovies in the movieDao with correct data`() =
        runTest {
            dataSource.upsertPopularMovies(localMovies)

            coVerify(exactly = 1) { movieDao.upsertMovies(localMovies) }
            coVerify(exactly = 1) {
                movieDao.upsertPopularMovies(match { list ->
                    list.size == localMovies.size &&
                            list[0].movieId == localMovies[0].movieId &&
                            list[0].storedLanguage == localMovies[0].storedLanguage
                })
            }
        }

    @Test
    fun `upsertTopRatedMovies should call upsertTopRatedMovies in the movieDao`() = runTest {
        dataSource.upsertTopRatedMovies(localMovies)

        coVerify(exactly = 1) { movieDao.upsertMovies(localMovies) }
        coVerify(exactly = 1) {
            movieDao.upsertTopRatedMovies(match { list ->
                list.size == localMovies.size &&
                        list[0].movieId == localMovies[0].movieId &&
                        list[0].storedLanguage == localMovies[0].storedLanguage
            })
        }
    }

    @Test
    fun `upsertUpcomingMovies should call insertUpcomingMovies in the movieDao`() = runTest {
        dataSource.upsertUpcomingMovies(localMovies)

        coVerify(exactly = 1) { movieDao.upsertMovies(localMovies) }
        coVerify(exactly = 1) {
            movieDao.upsertUpcomingMovies(match { list ->
                list.size == localMovies.size &&
                        list[0].movieId == localMovies[0].movieId &&
                        list[0].storedLanguage == localMovies[0].storedLanguage
            })
        }
    }

    @Test
    fun `deleteExpiredPopularMovies should call deleteExpiredPopularMovies in the movieDao`() =
        runTest {
            dataSource.deleteExpiredPopularMovies(expirationTime, storedLanguage)

            coVerify(exactly = 1) {
                movieDao.deleteExpiredPopularMovies(
                    expirationTime,
                    storedLanguage
                )
            }
        }

    @Test
    fun `deleteAllExpiredTopRatedMovies should call deleteAllExpiredTopRatedMovies`() = runTest {
        dataSource.deleteAllExpiredTopRatedMovies(expirationTime, storedLanguage)

        coVerify(exactly = 1) {
            movieDao.deleteAllExpiredTopRatedMovies(
                expirationTime,
                storedLanguage
            )
        }
    }

    @Test
    fun `deleteExpiredUpcomingMovies should call deleteExpiredUpcomingMovies`() = runTest {
        dataSource.deleteExpiredUpcomingMovies(expirationTime, storedLanguage)

        coVerify(exactly = 1) {
            movieDao.deleteExpiredUpcomingMovies(
                expirationTime,
                storedLanguage
            )
        }
    }

}

private const val storedLanguage = "en"

private val movie = createMovie(movieId = 42, storedLanguage = storedLanguage)

private const val categoryId = 1L

private val categories = listOf(1L)

private val movieCategoryCrossRefs = listOf(
    MovieCategoryCrossRefDto(
        movieId = movie.movieId,
        categoryId = categories.first(),
        storedLanguage = storedLanguage
    )
)

private val moviesWithCategories = listOf(
    MovieWithCategories(
        movie = createMovie(movieId = 42, storedLanguage = "en"),
        categories = emptyList()
    )
)

private val localMovies = listOf(createMovie(movieId = 42, storedLanguage = storedLanguage))

private val expirationTime = Instant.parse("2023-01-01T00:00:00Z")

private fun createMovie(
    movieId: Long,
    storedLanguage: String,
    name: String = "Sample Movie"
): MovieLocalDto {
    return MovieLocalDto(
        movieId = movieId,
        storedLanguage = storedLanguage,
        name = name,
        description = "Test description",
        poster = "poster.jpg",
        releaseDate = LocalDate.parse("2020-01-01"),
        popularity = 9.5,
        rating = 4.3f,
        originCountry = "US",
        movieLength = 120,
        isAdult = false
    )
}
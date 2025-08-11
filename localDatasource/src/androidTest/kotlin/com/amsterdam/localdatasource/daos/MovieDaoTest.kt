package com.amsterdam.localdatasource.daos

import com.amsterdam.localdatasource.roomDataBase.daos.CategoryDao
import com.amsterdam.localdatasource.roomDataBase.daos.MovieDao
import com.amsterdam.repository.dto.local.LocalMovieCategoryDto
import com.amsterdam.repository.dto.local.LocalMovieDto
import com.amsterdam.repository.dto.local.MovieCategoryCrossRefDto
import com.amsterdam.repository.dto.local.PopularMovieDto
import com.amsterdam.repository.dto.local.TopRatedMovieDto
import com.amsterdam.repository.dto.local.UpcomingMovieDto
import com.amsterdam.repository.dto.local.relation.MovieWithCategories
import com.amsterdam.repository.dto.local.utils.DatabaseConstants
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.days

class MovieDaoTest : BaseDaoTest() {
    private lateinit var movieDao: MovieDao
    private lateinit var categoryDao: CategoryDao

    @BeforeEach
    fun setup() {
        movieDao = aflamiDatabase.movieDao()
        categoryDao = aflamiDatabase.categoryDao()
    }

    @Test
    fun upsertMovie_shouldInsertMovie() = runTest {
        movieDao.upsertMovie(movie)
        val result = movieDao.getMovieById(movie.movieId, movie.storedLanguage)

        assertThat(result).isEqualTo(movie)
    }

    @Test
    fun upsertMovie_shouldUpdateMovieWithoutDuplication_whenMovieAlreadyStored() = runTest {
        movieDao.upsertMovie(movie)

        movieDao.upsertMovie(updatedMovie)
        val result = movieDao.getMovieById(movie.movieId, movie.storedLanguage)

        assertThat(result?.name).isEqualTo(updatedMovie.name)
    }

    @Test
    fun upsertMovies_shouldInsertMovies() = runTest {
        movieDao.upsertMovies(listOf(movie))
        val result = movieDao.getMovieById(movie.movieId, movie.storedLanguage)

        assertThat(result).isEqualTo(movie)
    }

    @Test
    fun upsertMovies_shouldUpdateMovieWithoutDuplication_whenMovieAlreadyStored() = runTest {
        movieDao.upsertMovies(listOf(movie))

        movieDao.upsertMovies(listOf(updatedMovie))
        val result = movieDao.getMovieById(movie.movieId, movie.storedLanguage)

        assertThat(result?.name).isEqualTo(updatedMovie.name)
    }

    @Test
    fun getMovieById_shouldGetMovie_whenMovieIsExists() = runTest {
        movieDao.upsertMovie(movie)

        val result = movieDao.getMovieById(movie.movieId, movie.storedLanguage)

        assertThat(result).isEqualTo(movie)
    }

    @Test
    fun getMovieById_shouldReturnNull_whenMovieIsNotExists() = runTest {
        val result = movieDao.getMovieById(1L, "en")

        assertThat(result).isNull()
    }

    @Test
    fun upsertMovieCategoryCrossRefs_shouldInsertsNewCrossRefs_usingRawQuery() = runTest {
        categoryDao.upsertAllMovieCategories(moviesCategories)
        movieDao.upsertMovie(movie)

        movieDao.upsertMovieCategoryCrossRefs(movieCategoryCrossRefs)
        val cursor = aflamiDatabase.query(
            "SELECT * FROM ${DatabaseConstants.MOVIE_CATEGORY_CROSS_REF_TABLE}",
            null
        )
        val retrievedCrossRefs = mutableListOf<MovieCategoryCrossRefDto>()
        cursor.use {
            if (it.moveToFirst()) {
                do {
                    val movieId = it.getLong(it.getColumnIndexOrThrow("movieId"))
                    val categoryId = it.getLong(it.getColumnIndexOrThrow("categoryId"))
                    val storedLanguage = it.getString(it.getColumnIndexOrThrow("storedLanguage"))
                    retrievedCrossRefs.add(
                        MovieCategoryCrossRefDto(
                            movieId,
                            categoryId,
                            storedLanguage
                        )
                    )
                } while (it.moveToNext())
            }
        }

        assertThat(retrievedCrossRefs).containsExactlyElementsIn(movieCategoryCrossRefs)
    }

    @Test
    fun getPopularMovies_shouldReturnPopularMovies_whenMoviesStored() = runTest {
        movieDao.upsertMovie(movie)
        movieDao.upsertPopularMovies(popularMovies)

        val result = movieDao.getPopularMovies("en")

        assertThat(result).isEqualTo(moviesWithCategories)
    }

    @Test
    fun getPopularMovies_shouldReturnEmptyList_whenNoMoviesStored() = runTest {
        val result = movieDao.getPopularMovies("en")

        assertThat(result).isEmpty()
    }

    @Test
    fun getTopRatedMovies_shouldReturnTopRatedMovies_whenMoviesStored() = runTest {
        movieDao.upsertMovie(movie)
        movieDao.upsertTopRatedMovies(topRatedMovies)

        val result = movieDao.getTopRatedMovies("en")

        assertThat(result).isEqualTo(listOf(movie))
    }

    @Test
    fun getTopRatedMovies_shouldReturnEmptyList_whenNoMoviesStored() = runTest {
        val result = movieDao.getTopRatedMovies("en")

        assertThat(result).isEmpty()
    }

    @Test
    fun getUpcomingMovies_shouldReturnUpcomingMovies_whenMoviesStored() = runTest {
        movieDao.upsertMovie(movie)
        movieDao.upsertUpcomingMovies(upcomingMovies)

        val result = movieDao.getUpcomingMovies("en")

        assertThat(result).isEqualTo(moviesWithCategories)
    }

    @Test
    fun getUpcomingMovies_shouldReturnEmptyList_whenNoMoviesStored() = runTest {
        val result = movieDao.getUpcomingMovies("en")

        assertThat(result).isEmpty()
    }

    @Test
    fun upsertPopularMovies_shouldInsertPopularMovies_whenMoviesNotExist() = runTest {
        movieDao.upsertMovie(movie)

        movieDao.upsertPopularMovies(popularMovies)
        val result = movieDao.getPopularMovies("en")

        assertThat(result).isEqualTo(moviesWithCategories)
    }

    @Test
    fun upsertTopRatedMovies_shouldInsertTopRatedMovies_whenMoviesNotExist() = runTest {
        movieDao.upsertMovie(movie)

        movieDao.upsertTopRatedMovies(topRatedMovies)
        val result = movieDao.getTopRatedMovies("en")

        assertThat(result).isEqualTo(listOf(movie))
    }

    @Test
    fun upsertUpcomingMovies_shouldInsertComingMovies_whenMoviesNotExist() = runTest {
        movieDao.upsertMovie(movie)

        movieDao.upsertUpcomingMovies(upcomingMovies)
        val result = movieDao.getUpcomingMovies("en")

        assertThat(result).isEqualTo(moviesWithCategories)
    }

    @Test
    fun deleteExpiredPopularMovies_shouldDeleteExpiredPopularMovies() = runTest {
        movieDao.upsertMovie(movie)
        movieDao.upsertPopularMovies(popularMovies)

        movieDao.deleteExpiredPopularMovies(popularMovies[0].dateAdded + 5.days, "en")
        val result = movieDao.getPopularMovies("en")

        assertThat(result).isEmpty()
    }

    @Test
    fun deleteExpiredTopRatedMovies_shouldDeleteExpiredTopRatedMovies() = runTest {
        movieDao.upsertMovie(movie)
        movieDao.upsertTopRatedMovies(topRatedMovies)

        movieDao.deleteAllExpiredTopRatedMovies(topRatedMovies[0].dateAdded + 5.days, "en")
        val result = movieDao.getTopRatedMovies("en")

        assertThat(result).isEmpty()
    }

    @Test
    fun deleteExpiredUpcomingMovies_shouldDeleteExpiredUpcomingMovies() = runTest {
        movieDao.upsertMovie(movie)
        movieDao.upsertUpcomingMovies(upcomingMovies)

        movieDao.deleteExpiredUpcomingMovies(upcomingMovies[0].dateAdded + 5.days, "en")
        val result = movieDao.getUpcomingMovies("en")

        assertThat(result).isEmpty()
    }
}

private val topRatedMovies = listOf(TopRatedMovieDto(1L, "en"))

private val popularMovies = listOf(PopularMovieDto(1L, "en"))

private val upcomingMovies = listOf(UpcomingMovieDto(1L, "en"))

private val moviesCategories = listOf(
    LocalMovieCategoryDto(1),
    LocalMovieCategoryDto(2)
)

private val movie = createMovie()

private val updatedMovie = movie.copy(name = "Updated")

private val moviesWithCategories = listOf(
    MovieWithCategories(
        movie = movie,
        categories = emptyList()
    )
)

private val movieCategoryCrossRefs = listOf(
    MovieCategoryCrossRefDto(
        movieId = 1, categoryId = 1,
        storedLanguage = "en"
    ),
    MovieCategoryCrossRefDto(
        movieId = 1, categoryId = 2,
        storedLanguage = "en"
    ),
)

private fun createMovie(
    movieId: Long = 1L,
    storedLanguage: String = "en",
    name: String = "Original"
): LocalMovieDto {
    return LocalMovieDto(
        movieId = movieId,
        storedLanguage = storedLanguage,
        name = name,
        description = "Test description",
        poster = "poster.jpg",
        releaseDate = LocalDate.parse("2020-01-01"),
        popularity = 9.5,
        rating = 4.3f,
        originCountry = "US",
        movieLength = 120
    )
}

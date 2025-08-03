package com.amsterdam.localdatasource.daos

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.amsterdam.localdatasource.roomDataBase.AflamiDatabase
import com.amsterdam.localdatasource.roomDataBase.daos.MovieDao
import com.amsterdam.localdatasource.utils.createMovie
import com.amsterdam.repository.dto.local.utils.SearchType
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MovieDaoTest {

    private lateinit var database: AflamiDatabase
    private lateinit var dao: MovieDao

    @BeforeEach
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AflamiDatabase::class.java).build()
        dao = database.movieDao()
    }

    @AfterEach
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertMovies_shouldInsertMovies() = runTest {
        // Given
        val movie = createMovie(movieId = 1L, storedLanguage = "en")

        // Act
        dao.insertMovies(listOf(movie))
        // Then
        val result = dao.getMovieById(1L)
        assertThat(result).isEqualTo(movie)
    }

    @Test
    fun insertMovies_shouldUpdateMovieWithoutDuplication_WhenMovieAlreadyStored() = runTest {
        // Given
        val originalMovie = createMovie(movieId = 1L, storedLanguage = "en", name = "Original")
        dao.insertMovies(listOf(originalMovie))

        // Act
        val updatedMovie = originalMovie.copy(name = "Updated")
        dao.insertMovies(listOf(updatedMovie))

        // Then
        val result = dao.getMovieById(1L)
        assertThat(result.name).isEqualTo("Updated")
    }


    @Test
    fun insertSearchEntries_shouldAssociateMovieWithKeyword() = runTest {
        // Given
        val movie = createMovie(movieId = 1L, storedLanguage = "en")
        val crossRef = SearchMovieCrossRefDto(
            searchKeyword = "superman",
            searchType = SearchType.BY_KEYWORD,
            movieId = movie.movieId,
            storedLanguage = movie.storedLanguage
        )
        dao.insertMovies(listOf(movie))

        // Act
        dao.insertSearchEntries(listOf(crossRef))

        // Then
        val result = dao.getMoviesByKeywordAndSearchType(
            keyword = "superman",
            searchType = SearchType.BY_KEYWORD,
            storedLanguage = "en",
            limit = 10,
            offset = 0
        )
        assertThat(result).hasSize(1)
        assertThat(result.first().movie).isEqualTo(movie)
    }

    @Test
    fun getMoviesByKeywordAndSearchType_shouldReturnEmpty_whenNoMatch() = runTest {
        // Given
        val movie = createMovie(movieId = 1L, storedLanguage = "en")
        val crossRef = SearchMovieCrossRefDto(
            searchKeyword = "batman",
            searchType = SearchType.BY_KEYWORD,
            movieId = movie.movieId,
            storedLanguage = movie.storedLanguage
        )
        dao.insertMovies(listOf(movie))
        dao.insertSearchEntries(listOf(crossRef))

        // When
        val result = dao.getMoviesByKeywordAndSearchType(
            keyword = "superman",
            searchType = SearchType.BY_KEYWORD,
            storedLanguage = "en",
            limit = 10,
            offset = 0
        )

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun getMoviesByKeywordAndSearchType_shouldRespectPagination() = runTest {
        // Given
        val movie1 = createMovie(movieId = 1L, storedLanguage = "en", name = "Movie 1")
        val movie2 = createMovie(movieId = 2L, storedLanguage = "en", name = "Movie 2")
        val movie3 = createMovie(movieId = 3L, storedLanguage = "en", name = "Movie 3")
        val keyword = "test"
        val searchType = SearchType.BY_KEYWORD

        dao.insertMovies(listOf(movie1, movie2, movie3))
        dao.insertSearchEntries(
            listOf(
                SearchMovieCrossRefDto(keyword, searchType, movie1.movieId, movie1.storedLanguage),
                SearchMovieCrossRefDto(keyword, searchType, movie2.movieId, movie2.storedLanguage),
                SearchMovieCrossRefDto(keyword, searchType, movie3.movieId, movie3.storedLanguage)
            )
        )

        // When
        val page1 = dao.getMoviesByKeywordAndSearchType(keyword, searchType, "en", limit = 2, offset = 0)
        val page2 = dao.getMoviesByKeywordAndSearchType(keyword, searchType, "en", limit = 2, offset = 2)

        // Then
        assertThat(page1).hasSize(2)
        assertThat(page2).hasSize(1)
        assertThat((page1 + page2).map { it.movie }).isEqualTo(listOf(movie1, movie2, movie3))
    }
}

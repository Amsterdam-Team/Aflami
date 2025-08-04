package com.amsterdam.localdatasource.roomDataBase.datasource

import com.amsterdam.localdatasource.roomDataBase.daos.MovieCategoryInterestDao
import com.amsterdam.localdatasource.roomDataBase.daos.MovieDao
import com.amsterdam.localdatasource.utils.createLocalMovieCategoryDto
import com.amsterdam.localdatasource.utils.createMovie
import com.amsterdam.repository.dto.local.relation.MovieWithCategories
import com.amsterdam.repository.dto.local.utils.SearchType
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
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
    fun `getMoviesByKeywordAndSearchType should return correct list`() = runTest {
        //Given
        val expected = listOf(mockk<MovieWithCategories>())
        coEvery {
            movieDao.getMoviesBySearchKeywordSortedByInterest(
                "action",
                SearchType.BY_KEYWORD,
                "en",
                10,
                0
            )
        } returns expected
        //When
        val result =
            dataSource.getMoviesByKeywordAndSearchType("action", SearchType.BY_KEYWORD, "en", 10, 0)
        //Then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `addMoviesBySearchData should insert movies and search entries`() = runTest {
        //Given
        val movies = listOf(
            createMovie(movieId = 1, storedLanguage = "en")
        )
        val keyword = "action"
        val searchType = SearchType.BY_KEYWORD
        //When
        dataSource.addMoviesBySearchData(movies, keyword, searchType)
        //Then
        coVerify(exactly = 1) { movieDao.insertMovies(movies) }

        val expectedCrossRefs = movies.map {
            SearchMovieCrossRefDto(
                searchKeyword = keyword,
                searchType = searchType,
                movieId = it.movieId,
                storedLanguage = it.storedLanguage
            )
        }

        coVerify(exactly = 1) { movieDao.insertSearchEntries(expectedCrossRefs) }
    }

    @Test
    fun `getMovieById should return the correct movie`() = runTest {
        //Given
        val movie = createMovie(movieId = 42, storedLanguage = "en")
        coEvery { movieDao.getMovieById(42) } returns movie
        //When
        val result = dataSource.getMovieById(42)
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
            val categories = listOf(createLocalMovieCategoryDto(categoryId = 1L))
            val expectedCrossRefs = listOf(
                MovieCategoryCrossRefDto(
                    movieId = movie.movieId,
                    categoryId = categories.first().categoryId,
                    storedLanguage = "en"
                )
            )

            // When
            dataSource.addMovieWithCategories(movie, categories, "en")

            // Then
            coVerify(exactly = 1) { movieDao.insertMovie(movie) }
            coVerify(exactly = 1) { movieDao.insertMovieCategoryCrossRefs(expectedCrossRefs) }

        }
}
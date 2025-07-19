package com.example.localdatasource

import com.example.localdatasource.roomDataBase.daos.MovieDao
import com.example.localdatasource.roomDataBase.datasource.MovieLocalDataSourceImpl
import com.example.repository.dto.local.LocalMovieCategoryDto
import com.example.repository.dto.local.LocalMovieDto
import com.example.repository.dto.local.SearchMovieCrossRefDto
import com.example.repository.dto.local.relation.MovieWithCategories
import com.example.repository.dto.local.utils.SearchType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals

class MovieLocalDataSourceImplTest {
    private lateinit var dao: MovieDao
    private lateinit var movieLocalDataSourceImpl: MovieLocalDataSourceImpl


    @Before
    fun setUp() {
        dao = mockk(relaxed = true)
        movieLocalDataSourceImpl = MovieLocalDataSourceImpl(dao)
    }

    @Test
    fun `getMoviesByKeywordAndSearchType should return movie list when DAO returns data`() =
        runTest {
            // Given
            val keyword = "action"
            val searchType = SearchType.BY_KEYWORD

            coEvery { dao.getMoviesByKeywordAndSearchType(keyword, searchType) } returns expectedMovieWithCategories

            // When
            val result =
                movieLocalDataSourceImpl.getMoviesByKeywordAndSearchType(keyword, searchType)

            // Then
            coVerify { dao.getMoviesByKeywordAndSearchType(keyword, searchType) }
            assertEquals(expectedMovieWithCategories, result)
        }

    @Test
    fun `getMoviesByKeywordAndSearchType should return empty list when DAO returns empty`() =
        runTest {
            // Given
            val keyword = "unknown"
            val searchType = SearchType.BY_KEYWORD
            coEvery { dao.getMoviesByKeywordAndSearchType(keyword, searchType) } returns emptyList()

            // When
            val result =
                movieLocalDataSourceImpl.getMoviesByKeywordAndSearchType(keyword, searchType)

            // Then
            coVerify { dao.getMoviesByKeywordAndSearchType(keyword, searchType) }
            assertEquals(emptyList(), result)
        }

    @Test
    fun `getMoviesByKeywordAndSearchType should handle empty keyword`() = runTest {
        // Given
        val keyword = ""
        val searchType = SearchType.BY_KEYWORD
        val expected = emptyList<MovieWithCategories>()
        coEvery { dao.getMoviesByKeywordAndSearchType(keyword, searchType) } returns expected

        // When
        val result = movieLocalDataSourceImpl.getMoviesByKeywordAndSearchType(keyword, searchType)

        // Then
        coVerify { dao.getMoviesByKeywordAndSearchType(keyword, searchType) }
        assertEquals(expected, result)
    }

    @Test
    fun `addMoviesBySearchData should call dao with correct data`() = runTest {
        //Given
        val keyword = "action"
        val searchType = SearchType.BY_KEYWORD

        val expireDate = Instant.fromEpochMilliseconds(2000)
        //When
        movieLocalDataSourceImpl.addMoviesBySearchData(expectedMovieDto, keyword, searchType, expireDate)
        //Then
        coVerify { dao.insertMovies(expectedMovieDto) }
        coVerify { dao.insertSearchEntries(any()) }

    }

    @Test
    fun `getSearchMovieCrossRefs should call dao with correct data`() = runTest {
        //Given
        val keyword = "action"
        val searchType = SearchType.BY_KEYWORD
        val expected = listOf(
            SearchMovieCrossRefDto(
                searchKeyword = keyword,
                searchType = searchType,
                movieId = 1
            )
        )
        coEvery { dao.getSearchMoviesCrossRef(keyword, searchType) } returns expected
        //When
       val result= movieLocalDataSourceImpl.getSearchMovieCrossRefs(keyword, searchType)
        //Then
        coVerify { dao.getSearchMoviesCrossRef(keyword, searchType) }
        assertEquals(expected, result)
    }

}
package com.example.localdatasource

import com.example.localdatasource.roomDataBase.daos.RecentSearchDao
import com.example.localdatasource.roomDataBase.datasource.RecentSearchLocalDataSourceImpl
import com.example.repository.dto.local.LocalSearchDto
import com.example.repository.dto.local.utils.SearchType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals

class RecentSearchLocalDataSourceImplTest {
    private lateinit var dao: RecentSearchDao
    private lateinit var recentSearchLocalDataSourceImpl: RecentSearchLocalDataSourceImpl

    @Before
    fun setUp(){
        dao = mockk(relaxed = true)
        recentSearchLocalDataSourceImpl = RecentSearchLocalDataSourceImpl(dao)
    }

    @Test
    fun `upsertRecentSearch should call dao with correct data`()=runTest{
        //Given
        val recentSearch = LocalSearchDto(
            searchKeyword = "action",
            searchType = SearchType.BY_KEYWORD,
            expireDate =Instant.fromEpochMilliseconds(2000),
        )
        //When
       recentSearchLocalDataSourceImpl.upsertRecentSearch(recentSearch)
        //Then
        coVerify { dao.upsertRecentSearch(recentSearch) }

    }
    @Test
    fun `getRecentSearches should call dao with correct data`()=runTest {
        //Given
        val expected = listOf(
            LocalSearchDto(
                searchKeyword = "action",
                searchType = SearchType.BY_KEYWORD,
                expireDate = Instant.fromEpochMilliseconds(2000),
            )
        )
        coEvery { dao.getRecentSearches() } returns expected
        //When
        val result = recentSearchLocalDataSourceImpl.getRecentSearches()
        //Then
        coVerify { dao.getRecentSearches() }
        assertEquals(expected, result)
    }
    @Test
    fun `getSearchByKeywordAndType should call dao with correct data`()=runTest {
        //Given
        val keyword = "action"
        val searchType = SearchType.BY_KEYWORD
        val expected = LocalSearchDto(
            searchKeyword = "action",
            searchType = SearchType.BY_KEYWORD,
            expireDate = Instant.fromEpochMilliseconds(2000),
        )
        coEvery { dao.getSearchByKeywordAndType(keyword) } returns expected
        //When
        val result = recentSearchLocalDataSourceImpl.getSearchByKeywordAndType(keyword, searchType)
        //Then
        coVerify { dao.getSearchByKeywordAndType(keyword) }
        assertEquals(expected, result)
    }
    @Test
    fun `deleteRecentSearches should call dao with correct data`()=runTest {
        //When
        recentSearchLocalDataSourceImpl.deleteRecentSearches()
        //Then
        coVerify { dao.deleteAllSearches() }
    }
    @Test
    fun `deleteRecentSearchByKeyword should call dao with correct data`()=runTest {
        //Given
        val keyword = "action"
        //When
        recentSearchLocalDataSourceImpl.deleteRecentSearchByKeyword(keyword)
        //Then
        coVerify { dao.deleteSearchByKeyword(keyword) }
        coVerify { dao.deleteSearchMovieCrossRefByKeyword(keyword) }
    }

    @Test
    fun `deleteRecentSearchByKeywordAndType should call dao with correct data`()=runTest {
        //Given
        val keyword = "action"
        val searchType = SearchType.BY_KEYWORD

        //When
        recentSearchLocalDataSourceImpl.deleteRecentSearchByKeywordAndType(keyword, searchType)
        //Then
        coVerify { dao.deleteSearchByKeyword(keyword, searchType) }
        coVerify { dao.deleteSearchMovieCrossRefByKeyword(keyword, searchType) }
    }
    @Test
    fun `deleteExpiredRecentSearches should call dao with correct data`()=runTest {
        //Given
        val date = Instant.fromEpochMilliseconds(2000)
        //When
        recentSearchLocalDataSourceImpl.deleteExpiredRecentSearches(date)
        //Then
        coVerify { dao.deleteAllExpiredSearches(date) }
    }

    

}
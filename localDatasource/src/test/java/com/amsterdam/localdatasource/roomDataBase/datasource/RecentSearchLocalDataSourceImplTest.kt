package com.amsterdam.localdatasource.roomDataBase.datasource

import com.amsterdam.localdatasource.roomDataBase.daos.RecentSearchDao
import com.amsterdam.repository.dto.local.SearchLocalDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RecentSearchLocalDataSourceImplTest {
    private lateinit var dao: RecentSearchDao
    private lateinit var recentSearchLocalDataSourceImpl: RecentSearchLocalDataDataSourceImpl

    @BeforeEach
    fun setUp(){
        dao = mockk(relaxed = true)
        recentSearchLocalDataSourceImpl = RecentSearchLocalDataDataSourceImpl(dao)
    }

    @Test
    fun `upsertRecentSearch should call dao with correct data`()=runTest{
        //Given
        val recentSearch = SearchLocalDto(
            searchKeyword = "action"
        )
        //When
       recentSearchLocalDataSourceImpl.upsertRecentSearch(recentSearch)
        //Then
        coVerify (exactly = 1) { dao.upsertRecentSearch(recentSearch) }

    }
    @Test
    fun `getRecentSearches should return list of LocalSearchDto`()=runTest {
        //Given
        val expected = listOf(
            SearchLocalDto(
                searchKeyword = "action"
            )
        )
        coEvery { dao.getRecentSearches() } returns expected
        //When
        val result = recentSearchLocalDataSourceImpl.getRecentSearches()
        //Then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getRecentSearches should return empty list when dao return empty list`()=runTest {
        coEvery { dao.getRecentSearches() } returns emptyList()
        //When
        val result = recentSearchLocalDataSourceImpl.getRecentSearches()
        //Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `deleteRecentSearches should call delete function in the dao`()=runTest {
        //When
        recentSearchLocalDataSourceImpl.deleteRecentSearches()
        //Then
        coVerify { dao.deleteAllSearches() }
    }

    @Test
    fun `deleteRecentSearchByKeywordAndType should delete search entry and its cross-referenced movies`() = runTest {
        // Given
        val keyword = "action"
        // When
        recentSearchLocalDataSourceImpl.deleteRecentSearchByKeyword(keyword)
        // Then
        coVerify { dao.deleteSearchByKeyword(keyword) }
    }

}

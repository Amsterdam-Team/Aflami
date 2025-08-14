package com.amsterdam.localdatasource.roomDataBase.datasource

import com.amsterdam.localdatasource.roomDataBase.daos.RecentSearchDao
import com.amsterdam.repository.dto.local.SearchLocalDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class RecentSearchLocalDataSourceImplTest {
    private val dao by lazy { mockk<RecentSearchDao>(relaxed = true) }
    private val recentSearchLocalDataSourceImpl by lazy { RecentSearchLocalDataSourceImpl(dao) }

    @Test
    fun `upsertRecentSearch should call upsertRecentSearch in dao with correct data`() = runTest {
        recentSearchLocalDataSourceImpl.upsertRecentSearch(recentSearch)

        coVerify(exactly = 1) { dao.upsertRecentSearch(recentSearch) }
    }

    @Test
    fun `getRecentSearches should return list of LocalSearchDto when dao return data`() = runTest {
        coEvery { dao.getRecentSearches() } returns recentSearches

        val result = recentSearchLocalDataSourceImpl.getRecentSearches()

        assertThat(result).isEqualTo(recentSearches)
    }

    @Test
    fun `getRecentSearches should return empty list when dao return empty list`() = runTest {
        coEvery { dao.getRecentSearches() } returns emptyList()

        val result = recentSearchLocalDataSourceImpl.getRecentSearches()

        assertThat(result).isEmpty()
    }

    @Test
    fun `deleteRecentSearches should call deleteAllSearches in the dao`() = runTest {
        recentSearchLocalDataSourceImpl.deleteRecentSearches()

        coVerify(exactly = 1) { dao.deleteAllSearches() }
    }

    @Test
    fun `deleteRecentSearchByKeyword should call deleteSearchByKeyword in the dao`() = runTest {
        recentSearchLocalDataSourceImpl.deleteRecentSearchByKeyword(keyword)

        coVerify { dao.deleteSearchByKeyword(keyword) }
    }
}

private const val keyword = "action"

private val recentSearch = SearchLocalDto(
    searchKeyword = keyword
)

private val recentSearches = listOf(
    SearchLocalDto(
        searchKeyword = keyword
    )
)
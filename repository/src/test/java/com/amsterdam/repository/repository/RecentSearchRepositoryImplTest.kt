package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.RecentSearchRepository
import com.amsterdam.repository.datasource.local.RecentSearchLocalSource
import com.amsterdam.repository.dto.local.LocalSearchDto
import com.amsterdam.repository.mapper.local.toEntityList
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class RecentSearchRepositoryImplTest {

    private lateinit var repository: RecentSearchRepository

    private val recentSearchLocalSource: RecentSearchLocalSource = mockk()

    @BeforeEach
    fun setup() {
        clearAllMocks()
        repository = RecentSearchRepositoryImpl(recentSearchLocalSource = recentSearchLocalSource)
    }

    @Test
    fun `addRecentSearch should upsert the new search keyword`() = runTest {
        // Arrange
        val keyword = "test movie"

        coJustRun {
            recentSearchLocalSource.upsertRecentSearch(
                match { it.searchKeyword == keyword }
            )
        }

        // Act
        repository.addRecentSearch(keyword)

        // Assert
        coVerify(exactly = 1) {
            recentSearchLocalSource.upsertRecentSearch(
                match { it.searchKeyword == keyword }
            )
        }
    }

    @Test
    fun `addRecentSearch should propagate exception if local source fails`() = runTest {
        // Arrange
        val keyword = "failing add"
        val expectedException = RuntimeException("Upsert failed!")
        coEvery { recentSearchLocalSource.upsertRecentSearch(any()) } throws expectedException

        // Act & Assert
        val thrownException = assertThrows<RuntimeException> {
            repository.addRecentSearch(keyword)
        }

        assertThat(thrownException).isEqualTo(expectedException)
        coVerify(exactly = 1) { recentSearchLocalSource.upsertRecentSearch(any()) }
    }

    @Test
    fun `getAllRecentSearches should return mapped searches from local source`() = runTest {
        // Arrange
        val localSearchDto1 = LocalSearchDto("movie1")
        val localSearchDto2 = LocalSearchDto("movie2")
        val localSearches = listOf(localSearchDto1, localSearchDto2)
        val expectedKeywords = localSearches.toEntityList()

        coEvery { recentSearchLocalSource.getRecentSearches() } returns localSearches

        // Act
        val result = repository.getAllRecentSearches()

        // Assert
        assertThat(result).isEqualTo(expectedKeywords)
        coVerify(exactly = 1) { recentSearchLocalSource.getRecentSearches() }
    }

    @Test
    fun `getAllRecentSearches should return empty list if no searches exist locally`() = runTest {
        // Arrange
        coEvery { recentSearchLocalSource.getRecentSearches() } returns emptyList()
        val expectedKeywords = emptyList<LocalSearchDto>().toEntityList()

        // Act
        val result = repository.getAllRecentSearches()

        // Assert
        assertThat(result).isEmpty()
        coVerify(exactly = 1) { recentSearchLocalSource.getRecentSearches() }
    }

    @Test
    fun `getAllRecentSearches should propagate exception if local source fails`() = runTest {
        // Arrange
        val expectedException = RuntimeException("DB read error!")
        coEvery { recentSearchLocalSource.getRecentSearches() } throws expectedException

        // Act & Assert
        val thrownException = assertThrows<RuntimeException> {
            repository.getAllRecentSearches()
        }

        assertThat(thrownException).isEqualTo(expectedException)
        coVerify(exactly = 1) { recentSearchLocalSource.getRecentSearches() }
    }

    @Test
    fun `deleteAllRecentSearches should call local source to delete all searches`() = runTest {
        // Arrange
        coJustRun { recentSearchLocalSource.deleteRecentSearches() }

        // Act
        repository.deleteAllRecentSearches()

        // Assert
        coVerify(exactly = 1) { recentSearchLocalSource.deleteRecentSearches() }
    }

    @Test
    fun `deleteAllRecentSearches should propagate exception if local source fails`() = runTest {
        // Arrange
        val expectedException = RuntimeException("DB delete error!")
        coEvery { recentSearchLocalSource.deleteRecentSearches() } throws expectedException

        // Act & Assert
        val thrownException = assertThrows<RuntimeException> {
            repository.deleteAllRecentSearches()
        }

        assertThat(thrownException).isEqualTo(expectedException)
        coVerify(exactly = 1) { recentSearchLocalSource.deleteRecentSearches() }
    }

    @Test
    fun `deleteRecentSearch should call local source to delete specific search`() = runTest {
        // Arrange
        val keyword = "old movie"
        coJustRun { recentSearchLocalSource.deleteRecentSearchByKeyword(keyword) }

        // Act
        repository.deleteRecentSearch(keyword)

        // Assert
        coVerify(exactly = 1) { recentSearchLocalSource.deleteRecentSearchByKeyword(keyword) }
    }

    @Test
    fun `deleteRecentSearch should propagate exception if local source fails`() = runTest {
        // Arrange
        val keyword = "failing search"
        val expectedException = RuntimeException("Delete failed!")
        coEvery { recentSearchLocalSource.deleteRecentSearchByKeyword(any()) } throws expectedException

        // Act & Assert
        val thrownException = assertThrows<RuntimeException> {
            repository.deleteRecentSearch(keyword)
        }

        assertThat(thrownException).isEqualTo(expectedException)
        coVerify(exactly = 1) { recentSearchLocalSource.deleteRecentSearchByKeyword(keyword) }
    }
}
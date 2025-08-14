package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.RecentSearchRepository
import com.amsterdam.repository.datasource.local.RecentSearchLocalDataSource
import com.amsterdam.repository.dto.local.SearchLocalDto
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class RecentSearchRepositoryImplTest {

    private lateinit var repository: RecentSearchRepository

    private val recentSearchLocalDataSource: RecentSearchLocalDataSource = mockk()
    private val fixedCurrentTime = Instant.parse("2025-07-27T10:00:00Z")

    @BeforeEach
    fun setup() {
        clearAllMocks()

        mockkObject(Clock.System)
        every { Clock.System.now() } returns fixedCurrentTime

        repository = RecentSearchRepositoryImpl(
            recentSearchLocalDataSource = recentSearchLocalDataSource,
        )

    }


    @Test
    fun `getAllRecentSearches should return mapped BY_KEYWORD searches from local source`() =
        runTest {
            // Given
            val searchLocalDto1 = SearchLocalDto("movie1")
            val searchLocalDto2 = SearchLocalDto("movie2")
            val localSearches = listOf(searchLocalDto1, searchLocalDto2)
            val expectedKeywords = listOf("movie1", "movie2")
            coEvery { recentSearchLocalDataSource.getRecentSearches() } returns localSearches

            // When
            val result = repository.getAllRecentSearches()

            // Then
            assertThat(result).isEqualTo(expectedKeywords)
            coVerify(exactly = 1) { recentSearchLocalDataSource.getRecentSearches() }
        }

    @Test
    fun `getAllRecentSearches should return empty list if no BY_KEYWORD searches exist locally`() =
        runTest {
            // Given
            coEvery { recentSearchLocalDataSource.getRecentSearches() } returns emptyList()

            // When
            val result = repository.getAllRecentSearches()

            // Then
            assertThat(result).isEmpty()
            coVerify(exactly = 1) { recentSearchLocalDataSource.getRecentSearches() }
        }

    @Test
    fun `deleteAllRecentSearches should call local source to delete all searches`() = runTest {
        // Given
        coJustRun { recentSearchLocalDataSource.deleteRecentSearches() }

        // When
        repository.deleteAllRecentSearches()

        // Then
        coVerify(exactly = 1) { recentSearchLocalDataSource.deleteRecentSearches() }
    }

    @Test
    fun `getAllRecentSearches should propagate exception if local source fails`() = runTest {
        // Given
        val expectedException = RuntimeException("DB read error!")
        coEvery { recentSearchLocalDataSource.getRecentSearches() } throws expectedException

        // When
        val thrownException = assertThrows<RuntimeException> {
            repository.getAllRecentSearches()
        }

        // Then
        assertThat(thrownException).isEqualTo(expectedException)
        coVerify(exactly = 1) { recentSearchLocalDataSource.getRecentSearches() }
    }

    @Test
    fun `deleteAllRecentSearches should propagate exception if local source fails`() = runTest {
        // Given
        val expectedException = RuntimeException("DB delete error!")
        coEvery { recentSearchLocalDataSource.deleteRecentSearches() } throws expectedException

        // When
        val thrownException = assertThrows<RuntimeException> {
            repository.deleteAllRecentSearches()
        }

        // Then
        assertThat(thrownException).isEqualTo(expectedException)
        coVerify(exactly = 1) { recentSearchLocalDataSource.deleteRecentSearches() }
    }

    @Test
    fun `addRecentSearch should propagate exception if local source upsert fails`() = runTest {
        // Given
        val keyword = "failing add"
        val expectedException = RuntimeException("Upsert failed!")
        coEvery { recentSearchLocalDataSource.upsertRecentSearch(any()) } throws expectedException

        // When
        val thrownException = assertThrows<RuntimeException> {
            repository.addRecentSearch(keyword)
        }

        // Then
        assertThat(thrownException).isEqualTo(expectedException)
        coVerify(exactly = 1) { recentSearchLocalDataSource.upsertRecentSearch(any()) }
    }
}
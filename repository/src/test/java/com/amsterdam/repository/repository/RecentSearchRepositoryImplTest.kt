package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.RecentSearchRepository
import com.amsterdam.repository.datasource.local.RecentSearchLocalSource
import com.amsterdam.repository.dto.local.LocalSearchDto
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

    private val recentSearchLocalSource: RecentSearchLocalSource = mockk()
    private val fixedCurrentTime = Instant.parse("2025-07-27T10:00:00Z")

    @BeforeEach
    fun setup() {
        clearAllMocks()

        mockkObject(Clock.System)
        every { Clock.System.now() } returns fixedCurrentTime

        repository = RecentSearchRepositoryImpl(
            recentSearchLocalSource = recentSearchLocalSource,
        )

    }


    @Test
    fun `getAllRecentSearches should return mapped BY_KEYWORD searches from local source`() =
        runTest {
            // Given
            val localSearchDto1 = LocalSearchDto("movie1")
            val localSearchDto2 = LocalSearchDto("movie2")
            val localSearches = listOf(localSearchDto1, localSearchDto2)
            val expectedKeywords = listOf("movie1", "movie2")
            coEvery { recentSearchLocalSource.getRecentSearches() } returns localSearches

            // When
            val result = repository.getAllRecentSearches()

            // Then
            assertThat(result).isEqualTo(expectedKeywords)
            coVerify(exactly = 1) { recentSearchLocalSource.getRecentSearches() }
        }

    @Test
    fun `getAllRecentSearches should return empty list if no BY_KEYWORD searches exist locally`() =
        runTest {
            // Given
            coEvery { recentSearchLocalSource.getRecentSearches() } returns emptyList()

            // When
            val result = repository.getAllRecentSearches()

            // Then
            assertThat(result).isEmpty()
            coVerify(exactly = 1) { recentSearchLocalSource.getRecentSearches() }
        }

    @Test
    fun `deleteAllRecentSearches should call local source to delete all searches`() = runTest {
        // Given
        coJustRun { recentSearchLocalSource.deleteRecentSearches() }

        // When
        repository.deleteAllRecentSearches()

        // Then
        coVerify(exactly = 1) { recentSearchLocalSource.deleteRecentSearches() }
    }

    @Test
    fun `deleteRecentSearch should call local source to delete specific BY_KEYWORD search`() =
        runTest {
            // Given
            val keyword = "old movie"
            coJustRun { recentSearchLocalSource.deleteRecentSearchByKeywordAndType(keyword) }

            // When
            repository.deleteRecentSearch(keyword)

            // Then
            coVerify(exactly = 1) {
                recentSearchLocalSource.deleteRecentSearchByKeywordAndType(keyword)
            }
        }

    @Test
    fun `getAllRecentSearches should propagate exception if local source fails`() = runTest {
        // Given
        val expectedException = RuntimeException("DB read error!")
        coEvery { recentSearchLocalSource.getRecentSearches() } throws expectedException

        // When
        val thrownException = assertThrows<RuntimeException> {
            repository.getAllRecentSearches()
        }

        // Then
        assertThat(thrownException).isEqualTo(expectedException)
        coVerify(exactly = 1) { recentSearchLocalSource.getRecentSearches() }
    }

    @Test
    fun `deleteAllRecentSearches should propagate exception if local source fails`() = runTest {
        // Given
        val expectedException = RuntimeException("DB delete error!")
        coEvery { recentSearchLocalSource.deleteRecentSearches() } throws expectedException

        // When
        val thrownException = assertThrows<RuntimeException> {
            repository.deleteAllRecentSearches()
        }

        // Then
        assertThat(thrownException).isEqualTo(expectedException)
        coVerify(exactly = 1) { recentSearchLocalSource.deleteRecentSearches() }
    }

    @Test
    fun `deleteRecentSearch should propagate exception if local source fails`() = runTest {
        // Given
        val keyword = "failing search"
        val expectedException = RuntimeException("Delete failed!")
        coEvery {
            recentSearchLocalSource.deleteRecentSearchByKeywordAndType(any())
        } throws expectedException

        // When
        val thrownException = assertThrows<RuntimeException> {
            repository.deleteRecentSearch(keyword)
        }

        // Then
        assertThat(thrownException).isEqualTo(expectedException)
        coVerify(exactly = 1) {
            recentSearchLocalSource.deleteRecentSearchByKeywordAndType(keyword)
        }
    }

    @Test
    fun `addRecentSearch should propagate exception if local source upsert fails`() = runTest {
        // Given
        val keyword = "failing add"
        val expectedException = RuntimeException("Upsert failed!")
        coEvery { recentSearchLocalSource.upsertRecentSearch(any()) } throws expectedException

        // When
        val thrownException = assertThrows<RuntimeException> {
            repository.addRecentSearch(keyword)
        }

        // Then
        assertThat(thrownException).isEqualTo(expectedException)
        coVerify(exactly = 1) { recentSearchLocalSource.upsertRecentSearch(any()) }
    }
}
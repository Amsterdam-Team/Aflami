package com.example.repository.repository

import com.example.repository.datasource.local.RecentSearchLocalSource
import com.example.repository.dto.local.LocalSearchDto
import com.example.repository.dto.local.utils.SearchType
import com.example.repository.mapper.local.RecentSearchMapper
import com.example.domain.repository.RecentSearchRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecentSearchRepositoryImplTest {

    private lateinit var repository: RecentSearchRepository

    private val recentSearchLocalSource: RecentSearchLocalSource = mockk()
    private val recentSearchMapper: RecentSearchMapper = mockk()
    private val now: Instant = Clock.System.now()

    private val testKeyword = "Action"
    private val testDto = LocalSearchDto(testKeyword, SearchType.BY_KEYWORD, now)

    @BeforeEach
    fun setup() {
        repository = RecentSearchRepositoryImpl(recentSearchLocalSource, recentSearchMapper)
    }

    @Test
    fun `should upsert keyword search`() = runTest {
        coEvery { recentSearchLocalSource.upsertRecentSearch(any()) } just Runs

        repository.upsertRecentSearch(testKeyword)

        coVerify {
            recentSearchLocalSource.upsertRecentSearch(
                withArg {
                    assertThat(it.searchKeyword).isEqualTo(testKeyword)
                    assertThat(it.searchType).isEqualTo(SearchType.BY_KEYWORD)
                }
            )
        }
    }

    @Test
    fun `should upsert actor search`() = runTest {
        coEvery { recentSearchLocalSource.upsertRecentSearch(any()) } just Runs

        repository.upsertRecentSearchForActor(testKeyword)

        coVerify {
            recentSearchLocalSource.upsertRecentSearch(
                withArg {
                    assertThat(it.searchKeyword).isEqualTo(testKeyword)
                    assertThat(it.searchType).isEqualTo(SearchType.BY_ACTOR)
                }
            )
        }
    }

    @Test
    fun `should upsert country search`() = runTest {
        coEvery { recentSearchLocalSource.upsertRecentSearch(any()) } just Runs

        repository.upsertRecentSearchForCountry(testKeyword)

        coVerify {
            recentSearchLocalSource.upsertRecentSearch(
                withArg {
                    assertThat(it.searchKeyword).isEqualTo(testKeyword)
                    assertThat(it.searchType).isEqualTo(SearchType.BY_COUNTRY)
                }
            )
        }
    }

    @Test
    fun `should get all recent searches`() = runTest {
        val dtos = listOf(testDto)
        val expected = listOf("Action")

        coEvery { recentSearchLocalSource.getRecentSearches() } returns dtos
        every { recentSearchMapper.toDomainList(dtos) } returns expected

        val result = repository.getAllRecentSearches()

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `should delete all recent searches`() = runTest {
        coEvery { recentSearchLocalSource.deleteRecentSearches() } just Runs

        repository.deleteAllRecentSearches()

        coVerify { recentSearchLocalSource.deleteRecentSearches() }
    }

    @Test
    fun `should delete specific recent search`() = runTest {
        coEvery { recentSearchLocalSource.deleteRecentSearchByKeyword(testKeyword) } just Runs

        repository.deleteRecentSearch(testKeyword)

        coVerify { recentSearchLocalSource.deleteRecentSearchByKeyword(testKeyword) }
    }
}

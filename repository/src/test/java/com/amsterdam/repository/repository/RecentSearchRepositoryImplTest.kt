package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.RecentSearchRepository
import com.amsterdam.repository.datasource.local.RecentSearchLocalSource
import com.amsterdam.repository.dto.local.LocalSearchDto
import com.amsterdam.repository.dto.local.utils.SearchType
import com.amsterdam.repository.mapper.local.RecentSearchLocalMapper
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.time.Duration.Companion.hours

class RecentSearchRepositoryImplTest {

    private lateinit var repository: RecentSearchRepository

    private val recentSearchLocalSource: RecentSearchLocalSource = mockk()
    private val recentSearchMapper: RecentSearchLocalMapper = mockk()

    private val testLanguage = "en"
    private val fixedCurrentTime = Instant.parse("2025-07-27T10:00:00Z")
    private val expectedExpiryTime = fixedCurrentTime.plus(1.hours)

    @BeforeEach
    fun setup() {
        clearAllMocks()

        mockkObject(Clock.System)
        every { Clock.System.now() } returns fixedCurrentTime

        repository = RecentSearchRepositoryImpl(
            recentSearchLocalSource = recentSearchLocalSource,
            recentSearchMapper = recentSearchMapper
        )

        every { recentSearchMapper.toEntityList(emptyList()) } returns emptyList()
    }

    @Test
    fun `addRecentSearch should upsert BY_KEYWORD search with correct details`() = runTest {
        val keyword = "test movie"
        val expectedLocalSearchDto = LocalSearchDto(
            searchKeyword = keyword,
            searchType = SearchType.BY_KEYWORD,
            storedLanguage = testLanguage,
            expireDate = expectedExpiryTime
        )

        coJustRun { recentSearchLocalSource.upsertRecentSearch(expectedLocalSearchDto) }

        repository.addRecentSearch(keyword)

        coVerify(exactly = 1) { recentSearchLocalSource.upsertRecentSearch(expectedLocalSearchDto) }
        verify(exactly = 1) { Clock.System.now() }
    }

    @Test
    fun `addRecentSearchForCountry should upsert BY_COUNTRY search with correct details`() =
        runTest {
            val keyword = "USA"
            val expectedLocalSearchDto = LocalSearchDto(
                searchKeyword = keyword,
                searchType = SearchType.BY_COUNTRY,
                storedLanguage = testLanguage,
                expireDate = expectedExpiryTime
            )

            coJustRun { recentSearchLocalSource.upsertRecentSearch(expectedLocalSearchDto) }

            repository.addRecentSearchForCountry(keyword)

            coVerify(exactly = 1) {
                recentSearchLocalSource.upsertRecentSearch(
                    expectedLocalSearchDto
                )
            }
            verify(exactly = 1) { Clock.System.now() }
        }

    @Test
    fun `addRecentSearchForActor should upsert BY_ACTOR search with correct details`() = runTest {
        val keyword = "Tom Hanks"
        val expectedLocalSearchDto = LocalSearchDto(
            searchKeyword = keyword,
            searchType = SearchType.BY_ACTOR,
            storedLanguage = testLanguage,
            expireDate = expectedExpiryTime
        )

        coJustRun { recentSearchLocalSource.upsertRecentSearch(expectedLocalSearchDto) }

        repository.addRecentSearchForActor(keyword)

        coVerify(exactly = 1) { recentSearchLocalSource.upsertRecentSearch(expectedLocalSearchDto) }
        verify(exactly = 1) { Clock.System.now() }
    }

    @Test
    fun `getAllRecentSearches should return mapped BY_KEYWORD searches from local source`() =
        runTest {
            val localSearchDto1 = LocalSearchDto(
                "movie1",
                SearchType.BY_KEYWORD,
                testLanguage,
                Instant.DISTANT_FUTURE
            )
            val localSearchDto2 = LocalSearchDto(
                "movie2",
                SearchType.BY_KEYWORD,
                testLanguage,
                Instant.DISTANT_FUTURE
            )
            val localSearches = listOf(localSearchDto1, localSearchDto2)
            val expectedKeywords = listOf("movie1", "movie2")

            coEvery { recentSearchLocalSource.getRecentSearches(SearchType.BY_KEYWORD) } returns localSearches
            every { recentSearchMapper.toEntityList(localSearches) } returns expectedKeywords

            val result = repository.getAllRecentSearches()

            assertThat(result).isEqualTo(expectedKeywords)
            coVerify(exactly = 1) { recentSearchLocalSource.getRecentSearches(SearchType.BY_KEYWORD) }
            verify(exactly = 1) { recentSearchMapper.toEntityList(localSearches) }
        }

    @Test
    fun `getAllRecentSearches should return empty list if no BY_KEYWORD searches exist locally`() =
        runTest {
            coEvery { recentSearchLocalSource.getRecentSearches(SearchType.BY_KEYWORD) } returns emptyList()

            val result = repository.getAllRecentSearches()

            assertThat(result).isEmpty()
            coVerify(exactly = 1) { recentSearchLocalSource.getRecentSearches(SearchType.BY_KEYWORD) }
            verify(exactly = 1) { recentSearchMapper.toEntityList(emptyList()) }
        }

    @Test
    fun `deleteAllRecentSearches should call local source to delete all searches`() = runTest {
        coJustRun { recentSearchLocalSource.deleteRecentSearches() }

        repository.deleteAllRecentSearches()

        coVerify(exactly = 1) { recentSearchLocalSource.deleteRecentSearches() }
    }

    @Test
    fun `deleteRecentSearch should call local source to delete specific BY_KEYWORD search`() =
        runTest {
            val keyword = "old movie"

            coJustRun {
                recentSearchLocalSource.deleteRecentSearchByKeywordAndType(
                    keyword,
                    SearchType.BY_KEYWORD,
                    testLanguage
                )
            }

            repository.deleteRecentSearch(keyword)

            coVerify(exactly = 1) {
                recentSearchLocalSource.deleteRecentSearchByKeywordAndType(
                    keyword, SearchType.BY_KEYWORD, testLanguage
                )
            }
        }

    @Test
    fun `getAllRecentSearches should propagate exception if local source fails`() = runTest {
        val expectedException = RuntimeException("DB read error!")
        coEvery { recentSearchLocalSource.getRecentSearches(any()) } throws expectedException

        val thrownException = assertThrows<RuntimeException> {
            repository.getAllRecentSearches()
        }

        assertThat(thrownException).isEqualTo(expectedException)
        coVerify(exactly = 1) { recentSearchLocalSource.getRecentSearches(SearchType.BY_KEYWORD) }
        verify(exactly = 0) { recentSearchMapper.toEntityList(any()) }
    }

    @Test
    fun `deleteAllRecentSearches should propagate exception if local source fails`() = runTest {
        val expectedException = RuntimeException("DB delete error!")
        coEvery { recentSearchLocalSource.deleteRecentSearches() } throws expectedException

        val thrownException = assertThrows<RuntimeException> {
            repository.deleteAllRecentSearches()
        }

        assertThat(thrownException).isEqualTo(expectedException)
        coVerify(exactly = 1) { recentSearchLocalSource.deleteRecentSearches() }
    }

    @Test
    fun `deleteRecentSearch should propagate exception if local source fails`() = runTest {
        val keyword = "failing search"
        val expectedException = RuntimeException("Delete failed!")
        coEvery {
            recentSearchLocalSource.deleteRecentSearchByKeywordAndType(
                any(),
                any(),
                any()
            )
        } throws expectedException

        val thrownException = assertThrows<RuntimeException> {
            repository.deleteRecentSearch(keyword)
        }

        assertThat(thrownException).isEqualTo(expectedException)
        coVerify(exactly = 1) {
            recentSearchLocalSource.deleteRecentSearchByKeywordAndType(
                keyword, SearchType.BY_KEYWORD, testLanguage
            )
        }
    }

    @Test
    fun `addRecentSearch should propagate exception if local source upsert fails`() = runTest {
        val keyword = "failing add"
        val expectedException = RuntimeException("Upsert failed!")
        coEvery { recentSearchLocalSource.upsertRecentSearch(any()) } throws expectedException

        val thrownException = assertThrows<RuntimeException> {
            repository.addRecentSearch(keyword)
        }

        assertThat(thrownException).isEqualTo(expectedException)
        coVerify(exactly = 1) { recentSearchLocalSource.upsertRecentSearch(any()) }
    }
}
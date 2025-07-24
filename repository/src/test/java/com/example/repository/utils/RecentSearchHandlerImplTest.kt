package com.example.repository.utils

import com.example.repository.datasource.local.RecentSearchLocalSource
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.minutes

class RecentSearchHandlerImplTest {

    private lateinit var recentSearchHandler: RecentSearchHandler
    private val recentSearchLocalSource: RecentSearchLocalSource = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        recentSearchHandler = RecentSearchHandlerImpl(recentSearchLocalSource)
    }


    @Test
    fun `isRecentSearchExpired should return true when recent search keyword does not exist`() = runTest {
        val recentSearch = createLocalSearchDto()

        coEvery {
            recentSearchLocalSource.getSearchByKeywordAndType(
                any(),
                any(),
                any()
            )
        } returns null

        val result = recentSearchHandler.isRecentSearchExpired(
            keyword = recentSearch.searchKeyword,
            searchType = recentSearch.searchType,
            storedLanguage = recentSearch.storedLanguage
        )

        assertThat(result).isTrue()
        coVerify(exactly = 1) {
            recentSearchLocalSource.getSearchByKeywordAndType(
                any(),
                any(),
                any()
            )
        }
    }


    @Test
    fun `isRecentSearchExpired should return true when recent search keyword is expired`() = runTest {
        val recentSearch = createLocalSearchDto(
            searchKeyword = "New",
            expireDate = Clock.System.now().minus(1.minutes),
        )

        coEvery {
            recentSearchLocalSource.getSearchByKeywordAndType(
                any(),
                any(),
                any()
            )
        } returns recentSearch

        val result = recentSearchHandler.isRecentSearchExpired(
            keyword = recentSearch.searchKeyword,
            searchType = recentSearch.searchType,
            storedLanguage = recentSearch.storedLanguage
        )

        assertThat(result).isTrue()
        coVerify(exactly = 1) {
            recentSearchLocalSource.getSearchByKeywordAndType(
                any(),
                any(),
                any()
            )
        }
    }


    @Test
    fun `getSearchByKeywordAndType should return false when recent search keyword is not expired`() =
        runTest {
            val recentSearch = createLocalSearchDto(
                searchKeyword = "New",
                expireDate = Clock.System.now().plus(1.minutes),
            )

            coEvery {
                recentSearchLocalSource.getSearchByKeywordAndType(
                    any(),
                    any(),
                    any()
                )
            } returns recentSearch

            val result = recentSearchHandler.isRecentSearchExpired(
                keyword = recentSearch.searchKeyword,
                searchType = recentSearch.searchType,
                storedLanguage = recentSearch.storedLanguage
            )

            assertThat(result).isFalse()
        }


    @Test
    fun `deleteRecentSearch should call delete search when recent search keyword is expired`() =
        runTest {
            val recentSearch = createLocalSearchDto(
                searchKeyword = "New",
                expireDate = Clock.System.now(),
            )

            coEvery {
                recentSearchLocalSource.getSearchByKeywordAndType(
                    any(),
                    any(),
                    any()
                )
            } returns recentSearch

            recentSearchHandler.deleteRecentSearch(
                keyword = recentSearch.searchKeyword,
                searchType = recentSearch.searchType,
                storedLanguage = recentSearch.storedLanguage
            )

            coVerify(exactly = 1) {
                recentSearchLocalSource.deleteRecentSearchByKeywordAndType(
                    any(),
                    any(),
                    any()
                )
            }
        }


    @Test
    fun `deleteRecentSearch should not call delete search when recent search keyword is not expired`() =
        runTest {
            val recentSearch = createLocalSearchDto(
                searchKeyword = "New",
                expireDate = Clock.System.now().plus(1.minutes),
            )

            coEvery {
                recentSearchLocalSource.getSearchByKeywordAndType(
                    any(),
                    any(),
                    any()
                )
            } returns recentSearch

            recentSearchHandler.deleteRecentSearch(
                keyword = recentSearch.searchKeyword,
                searchType = recentSearch.searchType,
                storedLanguage = recentSearch.storedLanguage
            )

            coVerify(exactly = 0) {
                recentSearchLocalSource.deleteRecentSearchByKeywordAndType(
                    any(),
                    any(),
                    any()
                )
            }
        }
}
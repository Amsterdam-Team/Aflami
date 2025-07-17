package com.example.domain.useCase

import com.example.domain.repository.RecentSearchRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RecentSearchesUsaCaseTest() {
    private lateinit var recentSearchRepository: RecentSearchRepository
    private lateinit var recentSearchesUsaCase: RecentSearchesUsaCase

    @BeforeEach
    fun setUp() {
        recentSearchRepository = mockk(relaxed = true)
        recentSearchesUsaCase = RecentSearchesUsaCase(recentSearchRepository)
    }

    @Test
    fun `should call upsertRecentSearch when keyword is valid`() = runTest {
        recentSearchesUsaCase.addRecentSearch("keyword")
        coVerify { recentSearchRepository.addRecentSearch(any()) }
    }

    @Test
    fun `should not call upsertRecentSearch when keyword is empty`() = runTest {
        recentSearchesUsaCase.addRecentSearch("")
        coVerify(exactly = 0) { recentSearchRepository.addRecentSearch(any()) }
    }

    @Test
    fun `should call upsertRecentSearch from recentSearchRepository when keyword is blank but not empty`() =
        runTest {
            recentSearchesUsaCase.addRecentSearch("   ")
            coVerify(exactly = 1) { recentSearchRepository.addRecentSearch("   ") }
        }

    @Test
    fun `should call deleteAllRecentSearches when executed`() = runTest {
        recentSearchesUsaCase.deleteRecentSearches()
        coVerify { recentSearchRepository.deleteRecentSearches() }
    }

    @Test
    fun `should call deleteRecentSearch when a keyword is provided`() = runTest {
        recentSearchesUsaCase.deleteRecentSearch("searchKeyword")
        coVerify { recentSearchRepository.deleteRecentSearch(any()) }
    }

    @Test
    fun `should call getAllRecentSearches when executed`() = runTest {
        recentSearchesUsaCase.getRecentSearches()
        coVerify { recentSearchRepository.getRecentSearches() }
    }

}
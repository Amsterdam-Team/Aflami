package com.example.domain.useCase

import com.example.domain.repository.RecentSearchRepository
import com.example.entity.Country
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RecentSearchesUsaCaseTest {
    private lateinit var recentSearchRepository: RecentSearchRepository
    private lateinit var recentSearchesUsaCase: RecentSearchesUsaCase
    private lateinit var country: Country

    @BeforeEach
    fun setUp() {
        recentSearchRepository = mockk(relaxed = true)
        country = Country("EGYPT", "EG")
        recentSearchesUsaCase = RecentSearchesUsaCase(recentSearchRepository)
    }

    @Test
    fun `should call addRecentSearch when keyword is valid`() = runTest {
        recentSearchesUsaCase.addRecentSearch("keyword")
        coVerify { recentSearchRepository.addRecentSearch(any()) }
    }

    @Test
    fun `should not call addRecentSearch when keyword is empty`() = runTest {
        recentSearchesUsaCase.addRecentSearch("")
        coVerify(exactly = 0) { recentSearchRepository.addRecentSearch(any()) }
    }

    @Test
    fun `should not call addRecentSearch from recentSearchRepository when keyword is blank but not empty`() =
        runTest {
            recentSearchesUsaCase.addRecentSearch("   ")
            coVerify(exactly = 0) { recentSearchRepository.addRecentSearch(any()) }
        }

    @Test
    fun `should call addRecentSearchForCountry when keyword is valid`() = runTest {
        recentSearchesUsaCase.addRecentSearchForCountry(country)
        coVerify { recentSearchRepository.addRecentSearchForCountry(any()) }
    }

    @Test
    fun `should not call addRecentSearchForCountry when keyword is empty`() = runTest {
        val country = country.copy(countryIsoCode = "")
        recentSearchesUsaCase.addRecentSearchForCountry(country)
        coVerify(exactly = 0) { recentSearchRepository.addRecentSearchForCountry(any()) }
    }

    @Test
    fun `should call addRecentSearchForActor when keyword is valid`() = runTest {
        recentSearchesUsaCase.addRecentSearchForActor("keyword")
        coVerify { recentSearchRepository.addRecentSearchForActor(any()) }
    }

    @Test
    fun `should not call addRecentSearchForActor when keyword is empty`() = runTest {
        recentSearchesUsaCase.addRecentSearchForActor("")
        coVerify(exactly = 0) { recentSearchRepository.addRecentSearchForActor(any()) }
    }

    @Test
    fun `should not call addRecentSearchForActor from recentSearchRepository when keyword is blank but not empty`() =
        runTest {
            recentSearchesUsaCase.addRecentSearchForActor("   ")
            coVerify(exactly = 0) { recentSearchRepository.addRecentSearchForActor(any()) }
        }

    @Test
    fun `should not call addRecentSearchForCountry from recentSearchRepository when keyword is blank but not empty`() =
        runTest {
            val country = country.copy(countryIsoCode = "  ")
            recentSearchesUsaCase.addRecentSearchForCountry(country)
            coVerify(exactly = 0) { recentSearchRepository.addRecentSearchForCountry(any()) }
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
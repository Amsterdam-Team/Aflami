package com.example.domain.useCase

import com.example.domain.exceptions.AflamiException
import com.example.domain.repository.RecentSearchRepository
import com.example.entity.Country
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class RecentSearchesUseCaseTest {
    private lateinit var recentSearchRepository: RecentSearchRepository
    private lateinit var recentSearchesUseCase: RecentSearchesUseCase
    private lateinit var country: Country

    @BeforeEach
    fun setUp() {
        recentSearchRepository = mockk(relaxed = true)
        country = Country("EGYPT", "EG")
        recentSearchesUseCase = RecentSearchesUseCase(recentSearchRepository)
    }

    @Test
    fun `recentSearchesUsaCase should call addRecentSearch one time when keyword is valid`() = runTest {
        recentSearchesUseCase.addRecentSearch("keyword")
        coVerify(exactly = 1) { recentSearchRepository.addRecentSearch(any()) }
    }

    @Test
    fun `recentSearchesUsaCase should not call addRecentSearch when keyword is empty`() = runTest {
        recentSearchesUseCase.addRecentSearch("")
        coVerify(exactly = 0) { recentSearchRepository.addRecentSearch("") }
    }

    @Test
    fun `recentSearchesUsaCase should not call addRecentSearch from recentSearchRepository when keyword is blank but not empty`() = runTest {
            recentSearchesUseCase.addRecentSearch("   ")
            coVerify(exactly = 0) { recentSearchRepository.addRecentSearch("   ") }
        }

    @Test
    fun `recentSearchesUsaCase should call addRecentSearchForCountry one time when called`() = runTest {
        recentSearchesUseCase.addRecentSearchForCountry(country)
        coVerify(exactly = 1) { recentSearchRepository.addRecentSearchForCountry(any()) }
    }

    @Test
    fun `recentSearchesUsaCase should call addRecentSearchForActor one time when called`() = runTest {
        recentSearchesUseCase.addRecentSearchForActor("keyword")
        coVerify(exactly = 1) { recentSearchRepository.addRecentSearchForActor(any()) }
    }

    @Test
    fun `recentSearchesUsaCase should not call addRecentSearchForActor when keyword is empty`() = runTest {
        recentSearchesUseCase.addRecentSearchForActor("")
        coVerify(exactly = 0) { recentSearchRepository.addRecentSearchForActor(any()) }
    }

    @Test
    fun `recentSearchesUsaCase should not call addRecentSearchForActor from recentSearchRepository when keyword is blank but not empty`() =
        runTest {
            recentSearchesUseCase.addRecentSearchForActor("   ")
            coVerify(exactly = 0) { recentSearchRepository.addRecentSearchForActor(any()) }
        }

    @Test
    fun `recentSearchesUsaCase should call deleteAllRecentSearches one time when called`() = runTest {
        recentSearchesUseCase.deleteRecentSearches()
        coVerify(exactly = 1) { recentSearchRepository.deleteRecentSearches() }
    }

    @Test
    fun `recentSearchesUsaCase should call deleteRecentSearch one time when called`() = runTest {
        recentSearchesUseCase.deleteRecentSearch("searchKeyword")
        coVerify(exactly = 1) { recentSearchRepository.deleteRecentSearch(any()) }
    }

    @Test
    fun `recentSearchesUsaCase should call getAllRecentSearches one time when called`() = runTest {
        recentSearchesUseCase.getRecentSearches()
        coVerify(exactly = 1) { recentSearchRepository.getRecentSearches() }
    }

    @Test
    fun `recentSearchesUsaCase should return Recent search when data returned`() = runTest {
        coEvery { recentSearchRepository.getRecentSearches() } returns listOf("Spider")
        val result = recentSearchesUseCase.getRecentSearches()
        assertThat(result).isEqualTo(listOf("Spider"))
    }

    @Test
    fun `recentSearchesUsaCase should return empty list when no data`() = runTest {
        coEvery { recentSearchRepository.getRecentSearches() } returns emptyList()
        val result = recentSearchesUseCase.getRecentSearches()
        assertThat(result).isEmpty()
    }

    @Test
    fun `recentSearchesUsaCase should throw Aflami exception when error happened`() = runTest {
        coEvery { recentSearchRepository.getRecentSearches() } throws AflamiException()
        assertThrows<AflamiException> { recentSearchesUseCase.getRecentSearches() }
    }

}
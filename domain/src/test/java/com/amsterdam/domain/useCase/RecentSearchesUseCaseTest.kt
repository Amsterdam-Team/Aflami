package com.amsterdam.domain.useCase

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.repository.RecentSearchRepository
import com.amsterdam.domain.useCase.search.RecentSearchesUseCase
import com.amsterdam.entity.Country
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
    private val country = Country("EGYPT", "EG")

    @BeforeEach
    fun setUp() {
        recentSearchRepository = mockk(relaxed = true)
        recentSearchesUseCase = RecentSearchesUseCase(recentSearchRepository)
    }

    @Test
    fun `addRecentSearch should call repository with valid keyword`() = runTest {
        val keyword = "keyword"
        recentSearchesUseCase.addRecentSearch(keyword)
        coVerify(exactly = 1) { recentSearchRepository.addRecentSearch(keyword) }
    }

    @Test
    fun `addRecentSearch should not call repository when keyword is empty`() = runTest {
        recentSearchesUseCase.addRecentSearch("")
        coVerify(exactly = 0) { recentSearchRepository.addRecentSearch(any()) }
    }

    @Test
    fun `addRecentSearch should not call repository when keyword is blank`() = runTest {
        recentSearchesUseCase.addRecentSearch("   ")
        coVerify(exactly = 0) { recentSearchRepository.addRecentSearch(any()) }
    }

    @Test
    fun `addRecentSearchForCountry should call repository with valid country`() = runTest {
        recentSearchesUseCase.addRecentSearch(country.countryName)
        coVerify(exactly = 1) { recentSearchRepository.addRecentSearch(country.countryName) }
    }

    @Test
    fun `addRecentSearchForCountry should not call repository when countryIsoCode is blank`() =
        runTest {
            val invalidCountry = Country(" ", "  ")
            recentSearchesUseCase.addRecentSearch(invalidCountry.countryName)
            coVerify(exactly = 0) { recentSearchRepository.addRecentSearch(any()) }
        }

    @Test
    fun `addRecentSearchForActor should call repository with valid actor name`() = runTest {
        val actorName = "keyword"
        recentSearchesUseCase.addRecentSearch(actorName)
        coVerify(exactly = 1) { recentSearchRepository.addRecentSearch(actorName) }
    }

    @Test
    fun `addRecentSearchForActor should not call repository when actor name is empty`() = runTest {
        recentSearchesUseCase.addRecentSearch("")
        coVerify(exactly = 0) { recentSearchRepository.addRecentSearch(any()) }
    }

    @Test
    fun `addRecentSearchForActor should not call repository when actor name is blank`() = runTest {
        recentSearchesUseCase.addRecentSearch("   ")
        coVerify(exactly = 0) { recentSearchRepository.addRecentSearch(any()) }
    }


    @Test
    fun `getRecentSearches should call repository once and return a list`() = runTest {
        coEvery { recentSearchRepository.getAllRecentSearches() } returns listOf("Spider")
        val result = recentSearchesUseCase.getRecentSearches()
        coVerify(exactly = 1) { recentSearchRepository.getAllRecentSearches() }
        assertThat(result).containsExactly("Spider")
    }

    @Test
    fun `getRecentSearches should return an empty list when no data is returned`() = runTest {
        coEvery { recentSearchRepository.getAllRecentSearches() } returns emptyList()
        val result = recentSearchesUseCase.getRecentSearches()
        assertThat(result).isEmpty()
    }

    @Test
    fun `deleteRecentSearch should call repository once with the correct keyword`() = runTest {
        val searchKeyword = "searchKeyword"
        recentSearchesUseCase.deleteRecentSearch(searchKeyword)
        coVerify(exactly = 1) { recentSearchRepository.deleteRecentSearch(searchKeyword) }
    }

    @Test
    fun `deleteRecentSearches should call repository once`() = runTest {
        recentSearchesUseCase.deleteRecentSearches()
        coVerify(exactly = 1) { recentSearchRepository.deleteAllRecentSearches() }
    }


    @Test
    fun `getRecentSearches should throw AflamiException when repository fails`() = runTest {
        coEvery { recentSearchRepository.getAllRecentSearches() } throws AflamiException()
        assertThrows<AflamiException> { recentSearchesUseCase.getRecentSearches() }
    }

    @Test
    fun `addRecentSearch should throw AflamiException when repository fails`() = runTest {
        val keyword = "testKeyword"
        coEvery { recentSearchRepository.addRecentSearch(keyword) } throws AflamiException()
        assertThrows<AflamiException> { recentSearchesUseCase.addRecentSearch(keyword) }
    }

    @Test
    fun `addRecentSearchForCountry should throw AflamiException when repository fails`() = runTest {
        coEvery { recentSearchRepository.addRecentSearch(any()) } throws AflamiException()
        assertThrows<AflamiException> { recentSearchesUseCase.addRecentSearch(country.countryName) }
    }

    @Test
    fun `addRecentSearchForActor should throw AflamiException when repository fails`() = runTest {
        val actorName = "actorName"
        coEvery { recentSearchRepository.addRecentSearch(actorName) } throws AflamiException()
        assertThrows<AflamiException> { recentSearchesUseCase.addRecentSearch(actorName) }
    }

    @Test
    fun `deleteRecentSearch should throw AflamiException when repository fails`() = runTest {
        val searchKeyword = "testKeyword"
        coEvery { recentSearchRepository.deleteRecentSearch(searchKeyword) } throws AflamiException()
        assertThrows<AflamiException> { recentSearchesUseCase.deleteRecentSearch(searchKeyword) }
    }
}
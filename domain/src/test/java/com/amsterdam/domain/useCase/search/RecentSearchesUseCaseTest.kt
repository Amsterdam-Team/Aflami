package com.amsterdam.domain.useCase.search

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.repository.RecentSearchRepository
import com.amsterdam.entity.Country
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class RecentSearchesUseCaseTest {
    private val recentSearchRepository: RecentSearchRepository = mockk(relaxed = true)
    private val recentSearchesUseCase by lazy {
        RecentSearchesUseCase(recentSearchRepository)
    }

    @Test
    fun `addRecentSearch should call repository with valid keyword`() = runTest {
        recentSearchesUseCase.addRecentSearch(searchKeyword)
        coVerify(exactly = 1) { recentSearchRepository.addRecentSearch(searchKeyword) }
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
        Truth.assertThat(result).containsExactly("Spider")
    }

    @Test
    fun `getRecentSearches should return an empty list when no data is returned`() = runTest {
        coEvery { recentSearchRepository.getAllRecentSearches() } returns emptyList()
        val result = recentSearchesUseCase.getRecentSearches()
        Truth.assertThat(result).isEmpty()
    }

    @Test
    fun `deleteRecentSearch should call repository once with the correct keyword`() = runTest {
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
        coEvery { recentSearchRepository.addRecentSearch(searchKeyword) } throws AflamiException()
        assertThrows<AflamiException> { recentSearchesUseCase.addRecentSearch(searchKeyword) }
    }

    @Test
    fun `addRecentSearchForCountry should throw AflamiException when repository fails`() = runTest {
        coEvery { recentSearchRepository.addRecentSearch(any()) } throws AflamiException()
        assertThrows<AflamiException> { recentSearchesUseCase.addRecentSearch(country.countryName) }
    }

    @Test
    fun `addRecentSearchForActor should throw AflamiException when repository fails`() = runTest {
        coEvery { recentSearchRepository.addRecentSearch(actorName) } throws AflamiException()
        assertThrows<AflamiException> { recentSearchesUseCase.addRecentSearch(actorName) }
    }

    @Test
    fun `deleteRecentSearch should throw AflamiException when repository fails`() = runTest {
        coEvery { recentSearchRepository.deleteRecentSearch(searchKeyword) } throws AflamiException()
        assertThrows<AflamiException> { recentSearchesUseCase.deleteRecentSearch(searchKeyword) }
    }

    private val actorName = "actorName"
    private val searchKeyword = "testKeyword"
    private val country = Country("EGYPT", "EG")
}
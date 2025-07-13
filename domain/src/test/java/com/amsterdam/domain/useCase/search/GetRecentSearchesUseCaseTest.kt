package com.amsterdam.domain.useCase.search

import com.amsterdam.domain.repository.RecentSearchRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetRecentSearchesUseCaseTest {
    private lateinit var recentSearchRepository: RecentSearchRepository
    private lateinit var getRecentSearchesUseCase: GetRecentSearchesUseCase

    @BeforeEach
    fun setUp() {
        recentSearchRepository = mockk(relaxed = true)
        getRecentSearchesUseCase = GetRecentSearchesUseCase(recentSearchRepository)
    }

    @Test
    fun `should call getAllRecentSearches from recentSearchRepository`() =
        runBlocking {
            getRecentSearchesUseCase()
            coVerify { recentSearchRepository.getAllRecentSearches() }
        }
}

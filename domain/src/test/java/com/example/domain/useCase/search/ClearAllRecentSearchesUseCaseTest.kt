package com.example.domain.useCase.search

import com.example.domain.repository.RecentSearchRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ClearAllRecentSearchesUseCaseTest {
    private lateinit var recentSearchRepository: RecentSearchRepository
    private lateinit var clearAllRecentSearchesUseCase: ClearAllRecentSearchesUseCase

    @BeforeEach
    fun setUp() {
        recentSearchRepository = mockk(relaxed = true)
        clearAllRecentSearchesUseCase = ClearAllRecentSearchesUseCase(recentSearchRepository)
    }

    @Test
    fun `should call deleteAllRecentSearches when executed`() =
        runBlocking {
            clearAllRecentSearchesUseCase()
            coVerify { recentSearchRepository.deleteAllRecentSearches() }
        }
}
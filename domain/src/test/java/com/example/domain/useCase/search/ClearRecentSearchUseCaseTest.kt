package com.example.domain.useCase.search

import com.example.domain.repository.RecentSearchRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ClearRecentSearchUseCaseTest {
    private lateinit var recentSearchRepository: RecentSearchRepository
    private lateinit var clearRecentSearchUseCase: ClearRecentSearchUseCase

    @BeforeEach
    fun setUp() {
        recentSearchRepository = mockk(relaxed = true)
        clearRecentSearchUseCase = ClearRecentSearchUseCase(recentSearchRepository)
    }

    @Test
    fun `should call deleteRecentSearch when a keyword is provided`() =
        runBlocking {
            clearRecentSearchUseCase("searchKeyword")
            coVerify { recentSearchRepository.deleteRecentSearch(any()) }
        }
}

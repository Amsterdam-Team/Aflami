package com.example.domain.useCase.search

import com.example.domain.repository.RecentSearchRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AddRecentSearchUseCaseTest {
    private lateinit var recentSearchRepository: RecentSearchRepository
    private lateinit var addRecentSearchUseCase: AddRecentSearchUseCase

    @BeforeEach
    fun setUp() {
        recentSearchRepository = mockk(relaxed = true)
        addRecentSearchUseCase = AddRecentSearchUseCase(recentSearchRepository)
    }

    @Test
    fun `should call upsertRecentSearch from recentSearchRepository`() =
        runBlocking {
            addRecentSearchUseCase("keyword")
            coVerify { recentSearchRepository.upsertRecentSearch(any()) }
        }

    @Test
    fun `should not call upsertRecentSearch from recentSearchRepository when keyword is empty`() =
        runBlocking {
            addRecentSearchUseCase("")
            coVerify(exactly = 0) { recentSearchRepository.upsertRecentSearch(any()) }
        }

    @Test
    fun `should call upsertRecentSearch from recentSearchRepository when keyword is blank but not empty`() =
        runBlocking {
            addRecentSearchUseCase("   ")
            coVerify(exactly = 1) { recentSearchRepository.upsertRecentSearch("   ") }
        }
}

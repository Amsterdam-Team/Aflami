package com.example.domain.useCase

import com.example.domain.repository.CategoryRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetTvShowCategoriesUseCaseTest {
    private lateinit var categoryRepository: CategoryRepository
    private lateinit var getTvShowCategoriesUseCase: GetTvShowCategoriesUseCase

    @BeforeEach
    fun setUp() {
        categoryRepository = mockk(relaxed = true)
        getTvShowCategoriesUseCase = GetTvShowCategoriesUseCase(categoryRepository)
    }

    @Test
    fun `should call getTvShowCategories when executed`() {
        runBlocking {
            getTvShowCategoriesUseCase()
            coVerify { categoryRepository.getTvShowCategories() }
        }
    }

    @Test
    fun `should return an empty list of categories when repository returns empty`() {
        runBlocking {
            coEvery { categoryRepository.getTvShowCategories() } returns listOf()
            val categories = getTvShowCategoriesUseCase()
            assert(categories.isEmpty())
        }
    }
}

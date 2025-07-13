package com.amsterdam.domain.useCase

import com.amsterdam.domain.repository.CategoryRepository
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
    fun `should call getTvShowCategories from categoryRepository`() {
        runBlocking {
            getTvShowCategoriesUseCase()
            coVerify { categoryRepository.getTvShowCategories() }
        }
    }

    @Test
    fun `should return a list of categories`() {
        runBlocking {
            coEvery { categoryRepository.getTvShowCategories() } returns listOf()
            val categories = getTvShowCategoriesUseCase()
            assert(categories.isEmpty())
        }
    }
}

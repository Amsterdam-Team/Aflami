package com.example.domain.useCase

import com.example.domain.exceptions.AflamiException
import com.example.domain.repository.CategoryRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetTvShowCategoriesUseCaseTest {
    private lateinit var categoryRepository: CategoryRepository
    private lateinit var getTvShowCategoriesUseCase: GetTvShowCategoriesUseCase

    @BeforeEach
    fun setUp() {
        categoryRepository = mockk(relaxed = true)
        getTvShowCategoriesUseCase = GetTvShowCategoriesUseCase(categoryRepository)
    }

    @Test
    fun `getTvShowCategoriesUseCase should call getTvShowCategories one time when called`() {
        runTest {
            getTvShowCategoriesUseCase()
            coVerify(exactly = 1) { categoryRepository.getTvShowCategories() }
        }
    }

    @Test
    fun `getTvShowCategoriesUseCase should return an empty list of categories when no data returned`() {
        runTest {
            coEvery { categoryRepository.getTvShowCategories() } returns listOf()
            assertThat(getTvShowCategoriesUseCase()).isEmpty()
        }
    }

    @Test
    fun `getTvShowCategoriesUseCase should return Aflami exception when an error happened`() =
        runTest {
            coEvery { categoryRepository.getTvShowCategories() } throws AflamiException()
            assertThrows<AflamiException> { getTvShowCategoriesUseCase() }
        }
}

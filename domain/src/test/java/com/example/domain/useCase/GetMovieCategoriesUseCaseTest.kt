package com.example.domain.useCase

import com.example.domain.repository.CategoryRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetMovieCategoriesUseCaseTest {
    private lateinit var categoryRepository: CategoryRepository
    private lateinit var getMovieCategoriesUseCase: GetMovieCategoriesUseCase

    @BeforeEach
    fun setUp() {
        categoryRepository = mockk(relaxed = true)
        getMovieCategoriesUseCase = GetMovieCategoriesUseCase(categoryRepository)
    }

    @Test
    fun `should call getMovieCategories when executed`() = runTest {
            getMovieCategoriesUseCase()
            coVerify { categoryRepository.getMovieCategories() }
        }
}

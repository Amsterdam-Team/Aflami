package com.example.domain.useCase

import com.example.domain.exceptions.AflamiException
import com.example.domain.repository.CategoryRepository
import com.example.domain.useCase.utils.fakeCategories
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetMovieCategoriesUseCaseTest {
    private lateinit var categoryRepository: CategoryRepository
    private lateinit var getMovieCategoriesUseCase: GetMovieCategoriesUseCase

    @BeforeEach
    fun setUp() {
        categoryRepository = mockk(relaxed = true)
        getMovieCategoriesUseCase = GetMovieCategoriesUseCase(categoryRepository)
    }

    @Test
    fun `getMovieCategoriesUseCase should call getMovieCategories exactly one time when executed`() = runTest {
        getMovieCategoriesUseCase()
        coVerify(exactly = 1) { categoryRepository.getMovieCategories() }
    }

    @Test
    fun `getMovieCategoriesUseCase should return Movie categories when data is available`() = runTest {
        coEvery { categoryRepository.getMovieCategories() } returns fakeCategories
        val result = getMovieCategoriesUseCase()
        assertThat(result).isNotEmpty()
    }

    @Test
    fun `getMovieCategoriesUseCase should return Aflami exception when an error happened`() = runTest {
        coEvery { categoryRepository.getMovieCategories() } throws AflamiException()
        assertThrows<AflamiException> { getMovieCategoriesUseCase() }
    }
}

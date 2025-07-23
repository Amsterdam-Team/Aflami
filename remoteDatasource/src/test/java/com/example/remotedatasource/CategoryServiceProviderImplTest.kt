package com.example.remotedatasource

import com.example.remotedatasource.api.CategoryApiService
import com.example.remotedatasource.serviceProvider.implementation.CategoryServiceProviderImpl
import com.example.repository.dto.remote.RemoteCategoryResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class CategoryServiceProviderImplTest {

    private lateinit var categoryApiService: CategoryApiService
    private lateinit var categoryServiceProviderImpl: CategoryServiceProviderImpl

    @Before
    fun setUp() {
        categoryApiService = mockk()
        categoryServiceProviderImpl = CategoryServiceProviderImpl(categoryApiService)
    }

    @Test
    fun `getMovieCategories should call CategoryApiService`() = runTest {
        // Given
        val dummyResponse =
            RemoteCategoryResponse(emptyList())
        coEvery { categoryApiService.getMovieCategories() } returns dummyResponse

        // When
        categoryServiceProviderImpl.getMovieCategories()

        // Then
        coVerify(exactly = 1) { categoryApiService.getMovieCategories() }
    }

    @Test
    fun `getTvShowCategories should call CategoryApiService`() = runTest {
        // Given
        val dummyResponse =
            RemoteCategoryResponse(emptyList())
        coEvery { categoryApiService.getTvShowCategories() } returns dummyResponse

        // When
        categoryServiceProviderImpl.getTvShowCategories()

        // Then
        coVerify(exactly = 1) { categoryApiService.getTvShowCategories() }
    }
}
package com.amsterdam.remotedatasource.datasource

import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.remotedatasource.api.CategoryApiService
import com.amsterdam.repository.dto.remote.RemoteCategoryDto
import com.amsterdam.repository.dto.remote.RemoteCategoryResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CategoryRemoteDataSourceImplTest {

    private lateinit var categoryApiService: CategoryApiService
    private lateinit var categoryRemoteDataSourceImpl: CategoryRemoteDataSourceImpl

    @BeforeEach
    fun setUp() {
        categoryApiService = mockk()
        categoryRemoteDataSourceImpl = CategoryRemoteDataSourceImpl(categoryApiService)
    }

    @Test
    fun `getMovieCategories should return a list of movie categories when successful`() = runTest {
        // Given
        val expectedCategoryResponse = RemoteCategoryResponse(
            genres = listOf(
                RemoteCategoryDto(id = 28, name = "Action"),
                RemoteCategoryDto(id = 12, name = "Adventure")
            )
        )

        coEvery { categoryApiService.getMovieCategories() } returns expectedCategoryResponse

        // When
        val categories = categoryRemoteDataSourceImpl.getMovieCategories()

        // Then
        assertThat(categories).isEqualTo(expectedCategoryResponse)
        coVerify(exactly = 1) { categoryApiService.getMovieCategories() }
    }

    @Test
    fun `getTvShowCategories should return a list of TV show categories when successful`() =
        runTest {
            // Given
            val expectedCategoryResponse = RemoteCategoryResponse(
                genres = listOf(
                    RemoteCategoryDto(id = 10759, name = "Action & Adventure"),
                    RemoteCategoryDto(id = 16, name = "Animation")
                )
            )

            coEvery { categoryApiService.getTvShowCategories() } returns expectedCategoryResponse

            // When
            val categories = categoryRemoteDataSourceImpl.getTvShowCategories()

            // Then
            assertThat(categories).isEqualTo(expectedCategoryResponse)
            coVerify(exactly = 1) { categoryApiService.getTvShowCategories() }
        }

    @Test
    fun `getMovieCategories should rethrow NetworkException from service provider`() = runTest {
        // Given
        coEvery { categoryApiService.getMovieCategories() } throws NetworkException()

        // When & Then
        assertThrows<NetworkException> {
            categoryRemoteDataSourceImpl.getMovieCategories()
        }
    }

    @Test
    fun `getTvShowCategories should rethrow NetworkException from service provider`() = runTest {
        // Given
        coEvery { categoryApiService.getTvShowCategories() } throws NetworkException()

        // When & Then
        assertThrows<NetworkException> {
            categoryRemoteDataSourceImpl.getTvShowCategories()
        }
    }
}
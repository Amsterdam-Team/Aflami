package com.amsterdam.remotedatasource.datasource

import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.remotedatasource.api.CategoryApiService
import com.amsterdam.repository.dto.remote.CategoryRemoteDto
import com.amsterdam.repository.dto.remote.CategoryRemoteResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CategoryRemoteDataSourceImplTest {

    private val categoryApiService: CategoryApiService = mockk()
    private val categoryRemoteDataSourceImpl: CategoryRemoteDataSourceImpl =
        CategoryRemoteDataSourceImpl(categoryApiService)

    @Test
    fun `getMovieCategories should return the correct list of movie categories`() = runTest {
        coEvery { categoryApiService.getMovieCategories() } returns movieCategoryResponse
        val categories = categoryRemoteDataSourceImpl.getMovieCategories()
        assertThat(categories).isEqualTo(movieCategoryResponse)
    }

    @Test
    fun `getMovieCategories should call getMovieCategories exactly once`() = runTest {
        coEvery { categoryApiService.getMovieCategories() } returns movieCategoryResponse
        categoryRemoteDataSourceImpl.getMovieCategories()
        coVerify(exactly = 1) { categoryApiService.getMovieCategories() }
    }

    @Test
    fun `getTvShowCategories should return the correct list of TV show categories`() = runTest {
        coEvery { categoryApiService.getTvShowCategories() } returns tvShowCategoryResponse
        val categories = categoryRemoteDataSourceImpl.getTvShowCategories()
        assertThat(categories).isEqualTo(tvShowCategoryResponse)
    }

    @Test
    fun `getTvShowCategories should call getTvShowCategories exactly once`() = runTest {
        coEvery { categoryApiService.getTvShowCategories() } returns tvShowCategoryResponse
        categoryRemoteDataSourceImpl.getTvShowCategories()
        coVerify(exactly = 1) { categoryApiService.getTvShowCategories() }
    }

    @Test
    fun `getMovieCategories should return an empty list when API service returns empty`() =
        runTest {
            coEvery { categoryApiService.getMovieCategories() } returns emptyCategoryResponse
            val categories = categoryRemoteDataSourceImpl.getMovieCategories()
            assertThat(categories.genres).isEmpty()
        }

    @Test
    fun `getMovieCategories should call getMovieCategories exactly once when the API returns an empty list`() =
        runTest {
            coEvery { categoryApiService.getMovieCategories() } returns emptyCategoryResponse
            categoryRemoteDataSourceImpl.getMovieCategories()
            coVerify(exactly = 1) { categoryApiService.getMovieCategories() }
        }

    @Test
    fun `getTvShowCategories should return an empty list when API service returns empty`() =
        runTest {
            coEvery { categoryApiService.getTvShowCategories() } returns emptyCategoryResponse
            val categories = categoryRemoteDataSourceImpl.getTvShowCategories()
            assertThat(categories.genres).isEmpty()
        }

    @Test
    fun `getTvShowCategories should call getTvShowCategories exactly once when the API returns an empty list`() =
        runTest {
            coEvery { categoryApiService.getTvShowCategories() } returns emptyCategoryResponse
            categoryRemoteDataSourceImpl.getTvShowCategories()
            coVerify(exactly = 1) { categoryApiService.getTvShowCategories() }
        }

    @Test
    fun `getMovieCategories should rethrow NetworkException from service provider`() = runTest {
        coEvery { categoryApiService.getMovieCategories() } throws networkException
        assertThrows<NetworkException> {
            categoryRemoteDataSourceImpl.getMovieCategories()
        }
    }

    @Test
    fun `getTvShowCategories should rethrow NetworkException from service provider`() = runTest {
        coEvery { categoryApiService.getTvShowCategories() } throws networkException
        assertThrows<NetworkException> {
            categoryRemoteDataSourceImpl.getTvShowCategories()
        }
    }

    private val movieCategoryResponse = CategoryRemoteResponse(
        genres = listOf(
            CategoryRemoteDto(id = 28, name = "Action"),
            CategoryRemoteDto(id = 12, name = "Adventure")
        )
    )

    private val tvShowCategoryResponse = CategoryRemoteResponse(
        genres = listOf(
            CategoryRemoteDto(id = 10759, name = "Action & Adventure"),
            CategoryRemoteDto(id = 16, name = "Animation")
        )
    )

    private val emptyCategoryResponse = CategoryRemoteResponse(genres = emptyList())
    private val networkException = NetworkException()
}
package com.amsterdam.remotedatasource.datasource

import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.remotedatasource.api.CategoryApiService
import com.amsterdam.repository.dto.remote.RemoteCategoryResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CategoryRemoteDataSourceImplTest {

    private lateinit var categoryApiService: CategoryApiService
    private lateinit var categoryRemoteDataSourceImpl: CategoryRemoteDataSourceImpl

    private val jsonSerializer = Json { ignoreUnknownKeys = true }

    @BeforeEach
    fun setUp() {
        categoryApiService = mockk()
        categoryRemoteDataSourceImpl = CategoryRemoteDataSourceImpl(categoryApiService)
    }

    @Test
    fun `getMovieCategories should return a list of movie categories`() = runTest {
        // Given
        val jsonString = """
            {
              "genres": [
                {"id": 28, "name": "Action"},
                {"id": 12, "name": "Adventure"}
              ]
            }
        """.trimIndent()

        val expectedCategoryResponse =
            jsonSerializer.decodeFromString<RemoteCategoryResponse>(jsonString)

        coEvery {
            categoryApiService.getMovieCategories()
        } returns expectedCategoryResponse

        // When
        val categories = categoryRemoteDataSourceImpl.getMovieCategories()

        // Then
        assertThat(categories.genres).hasSize(2)
        assertThat(categories.genres[0].name).isEqualTo("Action")
        assertThat(categories.genres[1].id).isEqualTo(12)
    }

    @Test
    fun `getTvShowCategories should return a list of TV show categories when executed`() = runTest {
        // Given
        val jsonString = """
            {
              "genres": [
                {"id": 10759, "name": "Action & Adventure"},
                {"id": 16, "name": "Animation"}
              ]
            }
        """.trimIndent()

        val expectedCategoryResponse =
            jsonSerializer.decodeFromString<RemoteCategoryResponse>(jsonString)

        coEvery {
            categoryApiService.getTvShowCategories()
        } returns expectedCategoryResponse

        // When
        val categories = categoryRemoteDataSourceImpl.getTvShowCategories()

        // Then
        assertThat(categories.genres).hasSize(2)
        assertThat(categories.genres[0].name).isEqualTo("Action & Adventure")
        assertThat(categories.genres[1].id).isEqualTo(16)
    }


    @Test
    fun `getMovieCategories should throw NetworkException from service provider when exception occurs`() =
        runTest {
            // Given
            coEvery { categoryApiService.getMovieCategories() } throws NetworkException()

            // When & Then
            assertThrows<NetworkException> {
                categoryRemoteDataSourceImpl.getMovieCategories()
            }
        }

    @Test
    fun `getTvShowCategories should rethrow NetworkException from service provider when exception occurs`() =
        runTest {
            // Given
            coEvery { categoryApiService.getTvShowCategories() } throws NetworkException()

            // When & Then
            assertThrows<NetworkException> {
                categoryRemoteDataSourceImpl.getTvShowCategories()
            }
        }

    @Test
    fun `getMovieCategories should call CategoryApiService`() = runTest {
        // Given
        val dummyResponse =
            RemoteCategoryResponse(emptyList())
        coEvery { categoryApiService.getMovieCategories() } returns dummyResponse

        // When
        val result = categoryApiService.getMovieCategories()

        // Then
        coVerify(exactly = 1) { categoryApiService.getMovieCategories() }
        assertThat(result).isEqualTo(dummyResponse)
    }

    @Test
    fun `getTvShowCategories should call CategoryApiService`() = runTest {
        // Given
        val dummyResponse =
            RemoteCategoryResponse(emptyList())
        coEvery { categoryApiService.getTvShowCategories() } returns dummyResponse

        // When
        val result = categoryApiService.getTvShowCategories()

        // Then
        coVerify(exactly = 1) { categoryApiService.getTvShowCategories() }
        assertThat(result).isEqualTo(dummyResponse)
    }
}
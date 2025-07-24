package com.example.remotedatasource

import com.example.domain.exceptions.NetworkException
import com.example.domain.exceptions.NoInternetException
import com.example.domain.exceptions.ServerErrorException
import com.example.remotedatasource.datasource.CategoryRemoteDataSourceImpl
import com.example.remotedatasource.serviceProvider.CategoryServiceProvider
import com.example.repository.dto.remote.RemoteCategoryResponse
import io.mockk.coEvery
import io.mockk.mockk
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CategoryRemoteDataSourceImplTest {

    private lateinit var categoryServiceProvider: CategoryServiceProvider
    private lateinit var categoryRemoteDataSourceImpl: CategoryRemoteDataSourceImpl

    private val jsonSerializer = Json { ignoreUnknownKeys = true }

    @BeforeEach
    fun setUp() {
        categoryServiceProvider = mockk()
        categoryRemoteDataSourceImpl = CategoryRemoteDataSourceImpl(categoryServiceProvider)
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
            categoryServiceProvider.getMovieCategories()
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
            categoryServiceProvider.getTvShowCategories()
        } returns expectedCategoryResponse

        // When
        val categories = categoryRemoteDataSourceImpl.getTvShowCategories()

        // Then
        assertThat(categories.genres).hasSize(2)
        assertThat(categories.genres[0].name).isEqualTo("Action & Adventure")
        assertThat(categories.genres[1].id).isEqualTo(16)
    }


    @Test
    fun `getMovieCategories should rethrow ServerErrorException from service provider`() = runTest {
        // Given
        coEvery { categoryServiceProvider.getMovieCategories() } throws ServerErrorException()

        // When & Then
        assertThrows<ServerErrorException> {
            categoryRemoteDataSourceImpl.getMovieCategories()
        }
    }

    @Test
    fun `getMovieCategories should rethrow NoInternetException from service provider`() = runTest {
        // Given
        coEvery { categoryServiceProvider.getMovieCategories() } throws NoInternetException()

        // When & Then
        assertThrows<NoInternetException> {
            categoryRemoteDataSourceImpl.getMovieCategories()
        }
    }

    @Test
    fun `getMovieCategories should throw NetworkException from service provider when exception occurs`() =
        runTest {
            // Given
            coEvery { categoryServiceProvider.getMovieCategories() } throws NetworkException()

            // When & Then
            assertThrows<NetworkException> {
                categoryRemoteDataSourceImpl.getMovieCategories()
            }
        }

    @Test
    fun `getTvShowCategories should rethrow ServerErrorException from service provider when exception occurs`() =
        runTest {
            // Given
            coEvery { categoryServiceProvider.getTvShowCategories() } throws ServerErrorException()

            // When & Then
            assertThrows<ServerErrorException> {
                categoryRemoteDataSourceImpl.getTvShowCategories()
            }
        }

    @Test
    fun `getTvShowCategories should rethrow NoInternetException from service provider when exception occurs`() =
        runTest {
            // Given
            coEvery { categoryServiceProvider.getTvShowCategories() } throws NoInternetException()

            // When & Then
            assertThrows<NoInternetException> {
                categoryRemoteDataSourceImpl.getTvShowCategories()
            }
        }

    @Test
    fun `getTvShowCategories should rethrow NetworkException from service provider when exception occurs`() =
        runTest {
            // Given
            coEvery { categoryServiceProvider.getTvShowCategories() } throws NetworkException()

            // When & Then
            assertThrows<NetworkException> {
                categoryRemoteDataSourceImpl.getTvShowCategories()
            }
        }
}
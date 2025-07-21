package com.example.remotedatasource

import com.example.remotedatasource.client.NetworkClient
import com.example.remotedatasource.datasource.CategoryRemoteDataSourceImpl
import com.example.repository.dto.remote.RemoteCategoryResponse
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CategoryRemoteDataSourceImplTest {

    private lateinit var networkClient: NetworkClient
    private lateinit var categoryRemoteDataSourceImpl: CategoryRemoteDataSourceImpl

    private val jsonSerializer = Json { ignoreUnknownKeys = true }

    @Before
    fun setUp() {
        networkClient = mockk()
        categoryRemoteDataSourceImpl = CategoryRemoteDataSourceImpl(networkClient)
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

        val mockHttpResponse = mockk<HttpResponse>(relaxed = true)

        coEvery { mockHttpResponse.status } returns HttpStatusCode.OK
        coEvery { mockHttpResponse.body<RemoteCategoryResponse>() } returns expectedCategoryResponse

        coEvery {
            networkClient.get("genre/movie/list", any())
        } returns mockHttpResponse

        // When
        val categories = categoryRemoteDataSourceImpl.getMovieCategories()

        // Then
        assertEquals(2, categories.genres.size)
        assertEquals("Action", categories.genres[0].name)
        assertEquals(12, categories.genres[1].id)
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

        val mockHttpResponse = mockk<HttpResponse>(relaxed = true)

        coEvery { mockHttpResponse.status } returns HttpStatusCode.OK
        coEvery { mockHttpResponse.body<RemoteCategoryResponse>() } returns expectedCategoryResponse

        coEvery {
            networkClient.get("genre/tv/list", any())
        } returns mockHttpResponse

        // When
        val categories = categoryRemoteDataSourceImpl.getTvShowCategories()

        // Then
        assertEquals(2, categories.genres.size)
        assertEquals("Action & Adventure", categories.genres[0].name)
        assertEquals(16, categories.genres[1].id)
    }
}
package com.example.remotedatasource.datasource

import com.example.domain.exceptions.NetworkException
import com.example.domain.exceptions.NoInternetException
import com.example.domain.exceptions.ServerErrorException
import com.example.remotedatasource.serviceProvider.TvShowsServiceProvider
import com.example.repository.dto.remote.RemoteTvShowResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

class TvRemoteDataSourceImplTest {

    private lateinit var tvShowsServiceProvider: TvShowsServiceProvider
    private lateinit var tvRemoteDataSourceImpl: TvRemoteDataSourceImpl

    private val jsonSerializer = Json { ignoreUnknownKeys = true }

    @Before
    fun setUp() {
        tvShowsServiceProvider = mockk()
        tvRemoteDataSourceImpl = TvRemoteDataSourceImpl(tvShowsServiceProvider)
    }

    @Test
    fun `getTvShowsByKeyword should return a list of TV shows when executed`() = runTest {
        // Given
        val keyword = "Game of Thrones"
        val page = 1

        val jsonString = """
            {
              "page": 1,
              "results": [
                {
                  "adult": false,
                  "backdrop_path": "/suopoADq0bPmYhnN8jQePVKzfdg.jpg",
                  "genre_ids": [10765, 10759, 18],
                  "id": 1399,
                  "name": "Game of Thrones",
                  "origin_country": ["US"],
                  "original_language": "en",
                  "original_name": "Game of Thrones",
                  "overview": "Seven noble families...",
                  "popularity": 450.0,
                  "poster_path": "/2yafLgJ9jL6t7jM0W7W9Bv0qP7j.jpg",
                  "first_air_date": "2011-04-17",
                  "vote_average": 8.4,
                  "vote_count": 20000
                }
              ],
              "total_pages": 1,
              "total_results": 1
            }
        """.trimIndent()

        val expectedTvShowResponse =
            jsonSerializer.decodeFromString<RemoteTvShowResponse>(jsonString)

        coEvery {
            tvShowsServiceProvider.getTvShowsByKeyword(keyword, page)
        } returns expectedTvShowResponse

        // When
        val tvShows =
            tvRemoteDataSourceImpl.getTvShowsByKeyword(keyword, page)

        coVerify(exactly = 1) { tvShowsServiceProvider.getTvShowsByKeyword(keyword, page) }

        // Then
        assertEquals(1, tvShows.results.size)
        assertEquals(
            "Game of Thrones",
            tvShows.results[0].title
        )
        assertEquals(1399, tvShows.results[0].id)
        assertEquals(
            "2011-04-17",
            tvShows.results[0].releaseDate
        )
        assertNotNull(tvShows.results[0].overview)
        assertEquals(450.0, tvShows.results[0].popularity, 0.001)
        assertEquals("/suopoADq0bPmYhnN8jQePVKzfdg.jpg", tvShows.results[0].backdropPath)
        assertEquals("/2yafLgJ9jL6t7jM0W7W9Bv0qP7j.jpg", tvShows.results[0].posterPath)
        assertEquals(8.4, tvShows.results[0].voteAverage, 0.001)
        assertEquals(20000, tvShows.results[0].voteCount)
    }

    @Test
    fun `getTvShowsByKeyword should rethrow ServerErrorException from service provider when exception occurs`() =
        runTest {
            // Given
            val keyword = "test"
            val page = 1
            coEvery {
                tvShowsServiceProvider.getTvShowsByKeyword(
                    keyword,
                    page
                )
            } throws ServerErrorException()

            // When & Then
            assertFailsWith<ServerErrorException> {
                tvRemoteDataSourceImpl.getTvShowsByKeyword(keyword, page)
            }
        }

    @Test
    fun `getTvShowsByKeyword should rethrow NoInternetException from service provider when exception occurs`() = runTest {
        // Given
        val keyword = "test"
        val page = 1
        coEvery {
            tvShowsServiceProvider.getTvShowsByKeyword(
                keyword,
                page
            )
        } throws NoInternetException()

        // When & Then
        assertFailsWith<NoInternetException> {
            tvRemoteDataSourceImpl.getTvShowsByKeyword(keyword, page)
        }
    }

    @Test
    fun `getTvShowsByKeyword should rethrow NetworkException from service provider when exception occurs`() = runTest {
        // Given
        val keyword = "test"
        val page = 1
        coEvery {
            tvShowsServiceProvider.getTvShowsByKeyword(
                keyword,
                page
            )
        } throws NetworkException()

        // When & Then
        assertFailsWith<NetworkException> {
            tvRemoteDataSourceImpl.getTvShowsByKeyword(keyword, page)
        }
    }
}
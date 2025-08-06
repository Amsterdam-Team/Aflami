package com.amsterdam.remotedatasource.datasource

import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.remotedatasource.api.UserListApiService
import com.amsterdam.remotedatasource.util.remoteListResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UserListRemoteDataSourceImplTest {

    private lateinit var userListRemoteDataSourceImpl: UserListRemoteDataSourceImpl
    private lateinit var userListApiService: UserListApiService

    @BeforeEach
    fun setup() {
        userListApiService = mockk()
        userListRemoteDataSourceImpl = UserListRemoteDataSourceImpl(userListApiService)
    }

    @Test
    fun `getMoviesFromList should call api service getMoviesFromList`() = runTest {
        // Given
        val listId = 1L
        coEvery { userListApiService.getMoviesFromList(listId, 1) } returns remoteListResponse

        // When
        userListRemoteDataSourceImpl.getMoviesFromList(listId, 1)

        // Then
        coVerify { userListApiService.getMoviesFromList(listId, 1) }
    }

    @Test
    fun `getMoviesFromList should return a list of movies from a user list`() = runTest {
        // Given
        val listId = 1L
        val response = remoteListResponse
        coEvery { userListApiService.getMoviesFromList(listId, 1) } returns response

        // When
        val result = userListRemoteDataSourceImpl.getMoviesFromList(listId, 1)

        // Then
        assertThat(result).isEqualTo(response)
    }

    @Test
    fun `getMoviesFromList should throw NetworkException when api call fails`() = runTest {
        // Given
        val listId = 1L
        coEvery { userListApiService.getMoviesFromList(listId, 1) } throws NetworkException()

        // When & Then
        assertThrows<NetworkException> { userListRemoteDataSourceImpl.getMoviesFromList(listId, 1) }
    }

    @Test
    fun `deleteList should call api service deleteList`() = runTest {
        // Given
        val listId = 1L
        val sessionId = "abc"
        coEvery { userListApiService.deleteList(listId, sessionId) } returns Unit

        // When
        userListRemoteDataSourceImpl.deleteList(listId, sessionId)

        // Then
        coVerify { userListApiService.deleteList(listId, sessionId) }
    }

    @Test
    fun `delete list should throw NetworkException when api call fails`() = runTest {
        val listId = 1L
        val sessionId = "abc"
        coEvery { userListApiService.deleteList(listId, sessionId) } throws NetworkException()

        assertThrows<NetworkException> { userListRemoteDataSourceImpl.deleteList(listId, sessionId) }
    }

    @Test
    fun `removeMovieFromList should call api service removeMovieFromList`() = runTest {
        // Given
        val listId = 1L
        val movieId = 1L
        val sessionId = "abc"
        coEvery { userListApiService.removeMovieFromList(listId, sessionId, movieId) } returns Unit

        // When
        userListRemoteDataSourceImpl.removeMovieFromList(listId, sessionId, movieId)

        // Then
        coVerify { userListApiService.removeMovieFromList(listId, sessionId, movieId) }
    }

    @Test
    fun `removeMovieFromList should throw NetworkException when api call fails`() = runTest {
        val listId = 1L
        val movieId = 1L
        val sessionId = "abc"
        coEvery { userListApiService.removeMovieFromList(listId, sessionId, movieId) } throws NetworkException()

        assertThrows<NetworkException> {
            userListRemoteDataSourceImpl.removeMovieFromList(listId, sessionId, movieId)
        }
    }
}
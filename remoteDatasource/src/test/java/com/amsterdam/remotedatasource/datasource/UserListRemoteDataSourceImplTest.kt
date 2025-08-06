package com.amsterdam.remotedatasource.datasource

import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.remotedatasource.api.UserListApiService
import com.amsterdam.remotedatasource.util.remoteListResponse
import com.amsterdam.repository.dto.remote.AddItemToListResponse
import com.amsterdam.repository.dto.remote.CreateUserListResponse
import com.amsterdam.repository.dto.remote.RemoteUserListResponse
import com.amsterdam.repository.dto.remote.AddItemToListResponse
import com.amsterdam.repository.dto.remote.CreateUserListResponse
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
    fun `createNewList should call api service createNewList`() =
        runTest {
            val listName = "My list"
            val sessionId = "abc"
            coEvery {
                userListApiService
                    .createNewList(
                        sessionId,
                        listName,
                        any(),
                        any(),
                    )
            } returns
                CreateUserListResponse(
                    listId = 1,
                    statusCode = 1,
                    statusMessage = "",
                    success = true,
                )

            val result = userListRemoteDataSourceImpl.createNewList(listName, "", "", sessionId)

            assertThat(result).isEqualTo(
                CreateUserListResponse(
                    listId = 1,
                    statusCode = 1,
                    statusMessage = "",
                    success = true,
                ),
            )
            coVerify {
                userListApiService
                    .createNewList(
                        sessionId,
                        listName,
                        any(),
                        any(),
                    )
            }
        }

    @Test
    fun `create list should throw NetworkException when api call fails`() =
        runTest {
            val listName = "My list"
            val sessionId = "abc"
            coEvery {
                userListApiService
                    .createNewList(
                        sessionId,
                        listName,
                        any(),
                        any(),
                    )
            } throws NetworkException()

            assertThrows<NetworkException> {
                userListRemoteDataSourceImpl
                    .createNewList(
                        sessionId,
                        "",
                        "",
                        listName,
                    )
            }
        }

    @Test
    fun `addMovieToList should call api service addMediaItemToList`() =
        runTest {
            val listId = 1L
            val movieId = 1
            val sessionId = "abc"
            coEvery {
                userListApiService
                    .addMediaItemToList(
                        listId,
                        sessionId,
                        movieId,
                    )
            } returns AddItemToListResponse(1, "", true)

            val result = userListRemoteDataSourceImpl.addMovieToList(listId, sessionId, movieId)

            assertThat(result).isEqualTo(AddItemToListResponse(1, "", true))
            coVerify { userListApiService.addMediaItemToList(listId, sessionId, movieId) }
        }

    @Test
    fun `addMovieToList should throw NetworkException when api call fails`() =
        runTest {
            val listId = 1L
            val movieId = 1
            val sessionId = "abc"
            coEvery {
                userListApiService
                    .addMediaItemToList(
                        listId,
                        sessionId,
                        movieId,
                    )
            } throws NetworkException()
            assertThrows<NetworkException> {
                userListRemoteDataSourceImpl.addMovieToList(listId, sessionId, movieId)
            }
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

        assertThrows<NetworkException> {
            userListRemoteDataSourceImpl.deleteList(
                listId,
                sessionId
            )
        }
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
        coEvery {
            userListApiService.removeMovieFromList(
                listId,
                sessionId,
                movieId
            )
        } throws NetworkException()

        assertThrows<NetworkException> {
            userListRemoteDataSourceImpl.removeMovieFromList(listId, sessionId, movieId)
        }
    }

    @Test
    fun `addMovieToList should call api service addMovieToList`() = runTest {
        // Given
        val listId = 1L
        val movieId = 1
        val sessionId = "abc"
        val expectedResponse = AddItemToListResponse(
            success = true,
            statusCode = 1,
            statusMessage = "success",
        )
        coEvery {
            userListApiService.addMediaItemToList(
                listId,
                sessionId,
                movieId
            )
        } returns expectedResponse
        // When
        val result = userListRemoteDataSourceImpl.addMovieToList(listId, sessionId, movieId)
        // Then
        assertThat(result).isEqualTo(expectedResponse)
        coVerify { userListApiService.addMediaItemToList(listId, sessionId, movieId) }


    }

    @Test
    fun `addMovieToList should throw NetworkException when api call fails`() = runTest {
        val listId = 1L
        val movieId = 1
        val sessionId = "abc"
        coEvery {
            userListApiService.addMediaItemToList(
                listId,
                sessionId,
                movieId
            )
        } throws NetworkException()
        assertThrows<NetworkException> {
            userListRemoteDataSourceImpl.addMovieToList(listId, sessionId, movieId)
        }
    }

    @Test
    fun `getUserLists should call api service getMovieDetails`() = runTest {
        // Given
        val accountId = 1
        val sessionId = "abc"
        val expectedResponse = RemoteUserListResponse(
            results = listOf(),
            page = 1,
            totalPages = 1,
            totalResults = 1,
        )
        coEvery {
            userListApiService.getUserLists(
                accountId,
                1,
                sessionId
            )
        } returns expectedResponse
        // When
        val result = userListRemoteDataSourceImpl.getUserLists(accountId, 1, sessionId)
        // Then
        assertThat(result).isEqualTo(expectedResponse)
        coVerify { userListApiService.getUserLists(accountId, 1, sessionId) }

    }

    @Test
    fun `createNewList should call api service createNewList`() = runTest {
        // Given
        val listName = "listName"
        val description = "description"
        val language = "language"
        val sessionId = "abc"
        val expectedResponse = CreateUserListResponse(
            success = true,
            statusMessage = "success",
            statusCode = 1,
            listId = 1,
        )
        coEvery {
            userListApiService.createNewList(
                sessionId = sessionId,
                listName = listName,
                description = description,
                language = language,
            )
        } returns expectedResponse
        // When
        val result =
            userListRemoteDataSourceImpl.createNewList(listName, description, language, sessionId)
        // Then
        assertThat(result).isEqualTo(expectedResponse)
        coVerify {
            userListApiService.createNewList(
                sessionId = sessionId,
                listName = listName,
                description = description,
                language = language,
            )
        }

    }

}
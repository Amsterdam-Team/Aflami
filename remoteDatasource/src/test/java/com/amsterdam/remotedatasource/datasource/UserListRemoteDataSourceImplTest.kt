package com.amsterdam.remotedatasource.datasource

import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.remotedatasource.api.UserListApiService
import com.amsterdam.repository.dto.remote.AddItemToListRemoteResponse
import com.amsterdam.repository.dto.remote.CreateUserListRemoteResponse
import com.amsterdam.repository.dto.remote.UserListRemoteResponse
import com.amsterdam.repository.dto.remote.UserListDetailsRemoteResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UserListRemoteDataSourceImplTest {

    private lateinit var userListRemoteDataSourceImpl: UserListRemoteDataDataSourceImpl
    private lateinit var userListApiService: UserListApiService

    private val remoteListResponse = UserListDetailsRemoteResponse(
        id = 1L,
        name = "Test List",
        description = "Test description",
        itemCount = 1,
        favoriteCount = 0,
        createdBy = "testUser",
        items = emptyList(),
        posterPath = null
    )

    @BeforeEach
    fun setup() {
        userListApiService = mockk()
        val jsonMock: Json = mockk(relaxed = true)
        userListRemoteDataSourceImpl = UserListRemoteDataDataSourceImpl(
            userListApiService = userListApiService,
            json = jsonMock
        )
    }

    @Test
    fun `createNewList should return a new user list response on success`() = runTest {
        val listName = "My list"
        val description = "A list of movies"
        val language = "en"
        val sessionId = "abc"
        val expectedResponse = CreateUserListRemoteResponse(
            listId = 1,
            statusCode = 1,
            statusMessage = "Success",
            success = true,
        )

        coEvery {
            userListApiService.createNewList(
                sessionId = sessionId,
                listName = listName,
                description = description,
                language = language,
            )
        } returns expectedResponse

        val result =
            userListRemoteDataSourceImpl.createNewList(listName, description, language, sessionId)

        assertThat(result).isEqualTo(expectedResponse)
        coVerify(exactly = 1) {
            userListApiService.createNewList(
                sessionId = sessionId,
                listName = listName,
                description = description,
                language = language,
            )
        }
    }

    @Test
    fun `createNewList should throw NetworkException when api call fails`() = runTest {
        val listName = "My list"
        val description = "A list of movies"
        val language = "en"
        val sessionId = "abc"

        coEvery {
            userListApiService.createNewList(
                sessionId = sessionId,
                listName = listName,
                description = description,
                language = language,
            )
        } throws NetworkException()

        assertThrows<NetworkException> {
            userListRemoteDataSourceImpl.createNewList(listName, description, language, sessionId)
        }
    }

    @Test
    fun `getUserLists should return remote user list response on success`() = runTest {
        val accountId = 1
        val page = 1
        val sessionId = "abc"
        val expectedResponse = UserListRemoteResponse(
            results = emptyList(),
            page = 1,
            totalPages = 1,
            totalResults = 1,
        )

        coEvery {
            userListApiService.getUserLists(
                accountId,
                page,
                sessionId
            )
        } returns expectedResponse

        val result = userListRemoteDataSourceImpl.getUserLists(accountId, page, sessionId)

        assertThat(result).isEqualTo(expectedResponse)
        coVerify(exactly = 1) { userListApiService.getUserLists(accountId, page, sessionId) }
    }

    @Test
    fun `addMovieToList should return a response on success`() = runTest {
        val listId = 1L
        val movieId = 1
        val sessionId = "abc"
        val expectedResponse = AddItemToListRemoteResponse(1, "success", true)

        coEvery {
            userListApiService.addMediaItemToList(
                listId,
                sessionId,
                movieId,
            )
        } returns expectedResponse

        val result = userListRemoteDataSourceImpl.addMovieToList(listId, sessionId, movieId)

        assertThat(result).isEqualTo(expectedResponse)
        coVerify(exactly = 1) { userListApiService.addMediaItemToList(listId, sessionId, movieId) }
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
                movieId,
            )
        } throws NetworkException()

        assertThrows<NetworkException> {
            userListRemoteDataSourceImpl.addMovieToList(listId, sessionId, movieId)
        }
    }

    @Test
    fun `getMoviesFromList should return a list of movies and call the api service`() = runTest {
        val listId = 1L
        coEvery { userListApiService.getMoviesAndTvShowsFromList(listId, 1) } returns remoteListResponse

        val result = userListRemoteDataSourceImpl.getMoviesAndTvShowsFromList(listId, 1)

        assertThat(result).isEqualTo(remoteListResponse)
        coVerify(exactly = 1) { userListApiService.getMoviesAndTvShowsFromList(listId, 1) }
    }

    @Test
    fun `getMoviesFromList should throw NetworkException when api call fails`() = runTest {
        val listId = 1L
        coEvery { userListApiService.getMoviesAndTvShowsFromList(listId, 1) } throws NetworkException()

        assertThrows<NetworkException> { userListRemoteDataSourceImpl.getMoviesAndTvShowsFromList(listId, 1) }
    }

    @Test
    fun `deleteList should call api service deleteList`() = runTest {
        val listId = 1L
        val sessionId = "abc"
        coEvery { userListApiService.deleteList(listId, sessionId) } returns Unit

        userListRemoteDataSourceImpl.deleteList(listId, sessionId)

        coVerify(exactly = 1) { userListApiService.deleteList(listId, sessionId) }
    }

    @Test
    fun `deleteList should throw NetworkException when api call fails`() = runTest {
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
        val listId = 1L
        val movieId = 1L
        val sessionId = "abc"
        coEvery { userListApiService.removeMovieFromList(listId, sessionId, movieId) } returns Unit

        userListRemoteDataSourceImpl.deleteMovieFromList(listId, sessionId, movieId)

        coVerify(exactly = 1) { userListApiService.removeMovieFromList(listId, sessionId, movieId) }
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
            userListRemoteDataSourceImpl.deleteMovieFromList(listId, sessionId, movieId)
        }
    }
}
package com.amsterdam.remotedatasource.datasource

import com.amsterdam.domain.exceptions.InvalidCredentialsException
import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.remotedatasource.api.UserListApiService
import com.amsterdam.repository.dto.remote.AddItemToListRemoteResponse
import com.amsterdam.repository.dto.remote.CreateUserListRemoteResponse
import com.amsterdam.repository.dto.remote.UserListDetailsRemoteResponse
import com.amsterdam.repository.dto.remote.UserListRemoteResponse
import com.amsterdam.repository.dto.remote.authentication.AuthenticationRemoteResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import retrofit2.HttpException
import retrofit2.Response

class UserListRemoteDataSourceImplTest {

    private val userListApiService: UserListApiService = mockk()
    private val jsonMock: Json = mockk(relaxed = true)
    private val userListRemoteDataSourceImpl: UserListRemoteDataSourceImpl =
        UserListRemoteDataSourceImpl(
            userListApiService = userListApiService,
            json = jsonMock
        )

    @Test
    fun `createNewList should return a new user list response on successful API call`() = runTest {
        coEvery {
            userListApiService.createNewList(
                any(),
                any(),
                any()
            )
        } returns createListSuccessResponse

        val result = userListRemoteDataSourceImpl.createNewList(listName, language)

        assertThat(result).isEqualTo(createListSuccessResponse)
    }

    @Test
    fun `createNewList should call the API service exactly once with the correct parameters`() =
        runTest {
            coEvery {
                userListApiService.createNewList(
                    any(),
                    any(),
                    any()
                )
            } returns createListSuccessResponse

            userListRemoteDataSourceImpl.createNewList(listName, language)

            coVerify(exactly = 1) {
                userListApiService.createNewList(
                    listName = listName,
                    language = language
                )
            }
        }

    @Test
    fun `createNewList should throw NetworkException when the API call fails`() = runTest {
        coEvery { userListApiService.createNewList(any(), any(), any()) } throws networkException

        assertThrows<NetworkException> {
            userListRemoteDataSourceImpl.createNewList(
                listName,
                language
            )
        }
    }

    @Test
    fun `getUserLists should return a remote user list response on successful API call`() =
        runTest {
            coEvery {
                userListApiService.getUserLists(
                    any(),
                    any()
                )
            } returns userListsSuccessResponse

            val result = userListRemoteDataSourceImpl.getUserLists(accountId, page)

            assertThat(result).isEqualTo(userListsSuccessResponse)
        }

    @Test
    fun `getUserLists should call the API service exactly once with the correct parameters`() =
        runTest {
            coEvery {
                userListApiService.getUserLists(
                    any(),
                    any()
                )
            } returns userListsSuccessResponse

            userListRemoteDataSourceImpl.getUserLists(accountId, page)

            coVerify(exactly = 1) { userListApiService.getUserLists(accountId, page) }
        }

    @Test
    fun `getUserLists should throw InvalidCredentialsException for 401 error with specific status code`() =
        runTest {
            coEvery {
                userListApiService.getUserLists(
                    any(),
                    any()
                )
            } throws createHttpException(401, errorBody401)
            coEvery { jsonMock.decodeFromString<AuthenticationRemoteResponse>(any<String>()) } returns authenticationRemoteResponse401

            assertThrows<InvalidCredentialsException> {
                userListRemoteDataSourceImpl.getUserLists(
                    accountId,
                    page
                )
            }
        }

    @Test
    fun `addMovieToList should return a response on successful API call`() = runTest {
        coEvery {
            userListApiService.addMediaItemToList(
                any(),
                any()
            )
        } returns addItemSuccessResponse

        val result = userListRemoteDataSourceImpl.addMovieToList(listId, movieId)

        assertThat(result).isEqualTo(addItemSuccessResponse)
    }

    @Test
    fun `addMovieToList should call the API service exactly once with the correct parameters`() =
        runTest {
            coEvery {
                userListApiService.addMediaItemToList(
                    any(),
                    any()
                )
            } returns addItemSuccessResponse

            userListRemoteDataSourceImpl.addMovieToList(listId, movieId)

            coVerify(exactly = 1) { userListApiService.addMediaItemToList(listId, movieId) }
        }

    @Test
    fun `addMovieToList should throw NetworkException when the API call fails`() = runTest {
        coEvery { userListApiService.addMediaItemToList(any(), any()) } throws networkException

        assertThrows<NetworkException> {
            userListRemoteDataSourceImpl.addMovieToList(
                listId,
                movieId
            )
        }
    }

    @Test
    fun `getMoviesAndTvShowsFromList should return a list of movies when the API call is successful`() =
        runTest {
            coEvery {
                userListApiService.getMoviesAndTvShowsFromList(
                    any(),
                    any()
                )
            } returns remoteListResponse

            val result = userListRemoteDataSourceImpl.getMoviesAndTvShowsFromList(listId, page)

            assertThat(result).isEqualTo(remoteListResponse)
        }

    @Test
    fun `getMoviesAndTvShowsFromList should call the API service exactly once with the correct parameters`() =
        runTest {
            coEvery {
                userListApiService.getMoviesAndTvShowsFromList(
                    any(),
                    any()
                )
            } returns remoteListResponse

            userListRemoteDataSourceImpl.getMoviesAndTvShowsFromList(listId, page)

            coVerify(exactly = 1) { userListApiService.getMoviesAndTvShowsFromList(listId, page) }
        }

    @Test
    fun `getMoviesAndTvShowsFromList should throw InvalidCredentialsException for 401 error with specific status code`() =
        runTest {
            coEvery {
                userListApiService.getMoviesAndTvShowsFromList(
                    any(),
                    any()
                )
            } throws createHttpException(401, errorBody401)
            coEvery { jsonMock.decodeFromString<AuthenticationRemoteResponse>(any<String>()) } returns authenticationRemoteResponse401

            assertThrows<InvalidCredentialsException> {
                userListRemoteDataSourceImpl.getMoviesAndTvShowsFromList(
                    listId,
                    page
                )
            }
        }

    @Test
    fun `getMoviesAndTvShowsFromList should throw NetworkException when the API call fails`() =
        runTest {
            coEvery {
                userListApiService.getMoviesAndTvShowsFromList(
                    any(),
                    any()
                )
            } throws networkException

            assertThrows<NetworkException> {
                userListRemoteDataSourceImpl.getMoviesAndTvShowsFromList(
                    listId,
                    page
                )
            }
        }

    @Test
    fun `deleteList should call the API service exactly once to delete the list`() = runTest {
        coEvery { userListApiService.deleteList(any()) } returns Unit

        userListRemoteDataSourceImpl.deleteList(listId)

        coVerify(exactly = 1) { userListApiService.deleteList(listId) }
    }

    @Test
    fun `deleteList should throw InvalidCredentialsException for 401 error with specific status code`() =
        runTest {
            coEvery { userListApiService.deleteList(any()) } throws createHttpException(
                401,
                errorBody401
            )
            coEvery { jsonMock.decodeFromString<AuthenticationRemoteResponse>(any<String>()) } returns authenticationRemoteResponse401

            assertThrows<InvalidCredentialsException> {
                userListRemoteDataSourceImpl.deleteList(
                    listId
                )
            }
        }

    @Test
    fun `deleteList should throw NetworkException when the API call fails`() = runTest {
        coEvery { userListApiService.deleteList(any()) } throws networkException

        assertThrows<NetworkException> { userListRemoteDataSourceImpl.deleteList(listId) }
    }

    @Test
    fun `deleteMovieFromList should call the API service exactly once to remove the movie from the list`() =
        runTest {
            coEvery { userListApiService.removeMovieFromList(any(), any()) } returns Unit

            userListRemoteDataSourceImpl.deleteMovieFromList(listId, movieId)

            coVerify(exactly = 1) { userListApiService.removeMovieFromList(listId, movieId) }
        }

    @Test
    fun `deleteMovieFromList should throw InvalidCredentialsException for 401 error with specific status code`() =
        runTest {
            coEvery {
                userListApiService.removeMovieFromList(
                    any(),
                    any()
                )
            } throws createHttpException(401, errorBody401)
            coEvery { jsonMock.decodeFromString<AuthenticationRemoteResponse>(any<String>()) } returns authenticationRemoteResponse401

            assertThrows<InvalidCredentialsException> {
                userListRemoteDataSourceImpl.deleteMovieFromList(
                    listId,
                    movieId
                )
            }
        }

    @Test
    fun `deleteMovieFromList should throw NetworkException when the API call fails`() = runTest {
        coEvery { userListApiService.removeMovieFromList(any(), any()) } throws networkException

        assertThrows<NetworkException> {
            userListRemoteDataSourceImpl.deleteMovieFromList(
                listId,
                movieId
            )
        }
    }

    private val listName = "My list"
    private val language = "en"
    private val listId = 1L
    private val movieId = 1L
    private val accountId = 1
    private val page = 1

    private val remoteListResponse = UserListDetailsRemoteResponse(
        id = listId,
        name = "Test List",
        description = "Test description",
        itemCount = 1,
        favoriteCount = 0,
        createdBy = "testUser",
        items = emptyList(),
        posterPath = null
    )

    private val createListSuccessResponse = CreateUserListRemoteResponse(
        listId = 1,
        statusCode = 1,
        statusMessage = "Success",
        success = true,
    )

    private val userListsSuccessResponse = UserListRemoteResponse(
        results = emptyList(),
        page = 1,
        totalPages = 1,
        totalResults = 1,
    )

    private val addItemSuccessResponse = AddItemToListRemoteResponse(
        statusCode = 1,
        statusMessage = "success",
        success = true
    )

    private val networkException = NetworkException()
    private val errorBody401 = "{\"status_code\": 3}"
    private val authenticationRemoteResponse401 = AuthenticationRemoteResponse(
        statusCode = 3,
        statusMessage = "Test"
    )

    private fun createHttpException(code: Int, body: String): HttpException {
        val errorBody = body.toResponseBody("application/json".toMediaTypeOrNull())
        val response = Response.error<Any>(
            errorBody,
            okhttp3.Response.Builder()
                .code(code)
                .message("Unauthorized")
                .protocol(okhttp3.Protocol.HTTP_1_1)
                .request(okhttp3.Request.Builder().url("http://localhost").build())
                .build()
        )
        return HttpException(response)
    }

}
package com.amsterdam.remotedatasource.datasource

import com.amsterdam.domain.exceptions.InvalidCredentialsException
import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.domain.exceptions.NoInternetException
import com.amsterdam.domain.exceptions.ServerErrorException
import com.amsterdam.remotedatasource.api.WishListApiService
import com.amsterdam.repository.dto.remote.AddItemToListRemoteResponse
import com.amsterdam.repository.dto.remote.CreateUserListRemoteResponse
import com.amsterdam.repository.dto.remote.WishListMovieItemStatusRemoteResponse
import com.amsterdam.repository.dto.remote.WishListDetailsRemoteResponse
import com.amsterdam.repository.dto.remote.WishListRemoteResponse
import com.amsterdam.repository.dto.remote.authentication.AuthenticationRemoteResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException

class WishListRemoteDataSourceImplTest {

    private val wishListApiService: WishListApiService = mockk()
    private val jsonMock: Json = mockk(relaxed = true)
    private val wishListRemoteDataSourceImpl: WishListRemoteDataSourceImpl =
        WishListRemoteDataSourceImpl(
            wishListApiService = wishListApiService,
            json = jsonMock
        )

    @Test
    fun `createNewList should return a new user list response on successful API call`() = runTest {
        coEvery {
            wishListApiService.createNewList(
                any(),
                any(),
                any()
            )
        } returns createListSuccessResponse

        val result = wishListRemoteDataSourceImpl.createNewList(listName, language)

        assertThat(result).isEqualTo(createListSuccessResponse)
    }

    @Test
    fun `createNewList should call the API service exactly once with the correct parameters`() =
        runTest {
            coEvery {
                wishListApiService.createNewList(
                    any(),
                    any(),
                    any()
                )
            } returns createListSuccessResponse

            wishListRemoteDataSourceImpl.createNewList(listName, language)

            coVerify(exactly = 1) {
                wishListApiService.createNewList(
                    listName = listName,
                    language = language
                )
            }
        }

    @Test
    fun `createNewList should throw NetworkException when the API call fails`() = runTest {
        coEvery { wishListApiService.createNewList(any(), any(), any()) } throws networkException

        assertThrows<NetworkException> {
            wishListRemoteDataSourceImpl.createNewList(
                listName,
                language
            )
        }
    }

    @Test
    fun `getUserLists should return a remote user list response on successful API call`() =
        runTest {
            coEvery {
                wishListApiService.getWishLists(
                    any(),
                    any()
                )
            } returns wishListsSuccessResponse

            val result = wishListRemoteDataSourceImpl.getWishLists(accountId, page)

            assertThat(result).isEqualTo(wishListsSuccessResponse)
        }

    @Test
    fun `getUserLists should call the API service exactly once with the correct parameters`() =
        runTest {
            coEvery {
                wishListApiService.getWishLists(
                    any(),
                    any()
                )
            } returns wishListsSuccessResponse

            wishListRemoteDataSourceImpl.getWishLists(accountId, page)

            coVerify(exactly = 1) { wishListApiService.getWishLists(accountId, page) }
        }

    @Test
    fun `getUserLists should throw InvalidCredentialsException for 401 error with specific status code`() =
        runTest {
            coEvery {
                wishListApiService.getWishLists(
                    any(),
                    any()
                )
            } throws createHttpException(401, errorBody401)
            coEvery { jsonMock.decodeFromString<AuthenticationRemoteResponse>(any<String>()) } returns authenticationRemoteResponse401

            assertThrows<InvalidCredentialsException> {
                wishListRemoteDataSourceImpl.getWishLists(
                    accountId,
                    page
                )
            }
        }

    @Test
    fun `addMovieToList should return a response on successful API call`() = runTest {
        coEvery {
            wishListApiService.addMediaItemToList(
                any(),
                any()
            )
        } returns addItemSuccessResponse

        val result = wishListRemoteDataSourceImpl.addMovieToList(listId, movieId)

        assertThat(result).isEqualTo(addItemSuccessResponse)
    }

    @Test
    fun `addMovieToList should call the API service exactly once with the correct parameters`() =
        runTest {
            coEvery {
                wishListApiService.addMediaItemToList(
                    any(),
                    any()
                )
            } returns addItemSuccessResponse

            wishListRemoteDataSourceImpl.addMovieToList(listId, movieId)

            coVerify(exactly = 1) { wishListApiService.addMediaItemToList(listId, movieId) }
        }

    @Test
    fun `addMovieToList should throw NetworkException when the API call fails`() = runTest {
        coEvery { wishListApiService.addMediaItemToList(any(), any()) } throws networkException

        assertThrows<NetworkException> {
            wishListRemoteDataSourceImpl.addMovieToList(
                listId,
                movieId
            )
        }
    }

    @Test
    fun `getMoviesAndTvShowsFromList should return a list of movies when the API call is successful`() =
        runTest {
            coEvery {
                wishListApiService.getMoviesAndTvShowsFromList(
                    any(),
                    any()
                )
            } returns remoteListResponse

            val result = wishListRemoteDataSourceImpl.getMoviesAndTvShowsFromList(listId, page)

            assertThat(result).isEqualTo(remoteListResponse)
        }

    @Test
    fun `getMoviesAndTvShowsFromList should call the API service exactly once with the correct parameters`() =
        runTest {
            coEvery {
                wishListApiService.getMoviesAndTvShowsFromList(
                    any(),
                    any()
                )
            } returns remoteListResponse

            wishListRemoteDataSourceImpl.getMoviesAndTvShowsFromList(listId, page)

            coVerify(exactly = 1) { wishListApiService.getMoviesAndTvShowsFromList(listId, page) }
        }

    @Test
    fun `getMoviesAndTvShowsFromList should throw InvalidCredentialsException for 401 error with specific status code`() =
        runTest {
            coEvery {
                wishListApiService.getMoviesAndTvShowsFromList(
                    any(),
                    any()
                )
            } throws createHttpException(401, errorBody401)
            coEvery { jsonMock.decodeFromString<AuthenticationRemoteResponse>(any<String>()) } returns authenticationRemoteResponse401

            assertThrows<InvalidCredentialsException> {
                wishListRemoteDataSourceImpl.getMoviesAndTvShowsFromList(
                    listId,
                    page
                )
            }
        }

    @Test
    fun `getMoviesAndTvShowsFromList should throw NetworkException when the API call fails`() =
        runTest {
            coEvery {
                wishListApiService.getMoviesAndTvShowsFromList(
                    any(),
                    any()
                )
            } throws networkException

            assertThrows<NetworkException> {
                wishListRemoteDataSourceImpl.getMoviesAndTvShowsFromList(
                    listId,
                    page
                )
            }
        }

    @Test
    fun `checkIsMovieInList should return true when API returns true`() = runTest {
        coEvery { wishListApiService.checkIsMovieInList(listId, movieId) } returns movieInListResponse

        val result = wishListRemoteDataSourceImpl.checkIsMovieInList(movieId, listId)

        assertThat(result.itemPresent).isTrue()
    }

    @Test
    fun `checkIsMovieInList should return false when API returns false`() = runTest {
        coEvery { wishListApiService.checkIsMovieInList(listId, movieId) } returns movieNotInListResponse

        val result = wishListRemoteDataSourceImpl.checkIsMovieInList(movieId, listId)

        assertThat(result.itemPresent).isFalse()
    }

    @Test
    fun `checkIsMovieInList should throw ServerErrorException when API service throws HttpException`() = runTest {
        coEvery { wishListApiService.checkIsMovieInList(listId, movieId) } throws createHttpException(404, "")

        assertThrows<ServerErrorException> {
            wishListRemoteDataSourceImpl.checkIsMovieInList(movieId, listId)
        }
    }

    @Test
    fun `checkIsMovieInList should throw NoInternetException when API service throws ConnectException`() = runTest {
        coEvery { wishListApiService.checkIsMovieInList(listId, movieId) } throws ConnectException()

        assertThrows<NoInternetException> {
            wishListRemoteDataSourceImpl.checkIsMovieInList(movieId, listId)
        }
    }

    @Test
    fun `checkIsMovieInList should throw ServerErrorException when API service throws SerializationException`() = runTest {
        coEvery { wishListApiService.checkIsMovieInList(listId, movieId) } throws SerializationException()

        assertThrows<ServerErrorException> {
            wishListRemoteDataSourceImpl.checkIsMovieInList(movieId, listId)
        }
    }

    @Test
    fun `checkIsMovieInList should throw NetworkException when API service throws IOException`() = runTest {
        coEvery { wishListApiService.checkIsMovieInList(listId, movieId) } throws IOException()

        assertThrows<NetworkException> {
            wishListRemoteDataSourceImpl.checkIsMovieInList(movieId, listId)
        }
    }

    @Test
    fun `deleteList should call the API service exactly once to delete the list`() = runTest {
        coEvery { wishListApiService.deleteList(any()) } returns Unit

        wishListRemoteDataSourceImpl.deleteList(listId)

        coVerify(exactly = 1) { wishListApiService.deleteList(listId) }
    }

    @Test
    fun `deleteList should throw InvalidCredentialsException for 401 error with specific status code`() =
        runTest {
            coEvery { wishListApiService.deleteList(any()) } throws createHttpException(
                401,
                errorBody401
            )
            coEvery { jsonMock.decodeFromString<AuthenticationRemoteResponse>(any<String>()) } returns authenticationRemoteResponse401

            assertThrows<InvalidCredentialsException> {
                wishListRemoteDataSourceImpl.deleteList(
                    listId
                )
            }
        }

    @Test
    fun `deleteList should throw NetworkException when the API call fails`() = runTest {
        coEvery { wishListApiService.deleteList(any()) } throws networkException

        assertThrows<NetworkException> { wishListRemoteDataSourceImpl.deleteList(listId) }
    }

    @Test
    fun `deleteMovieFromList should call the API service exactly once to remove the movie from the list`() =
        runTest {
            coEvery { wishListApiService.removeMovieFromList(any(), any()) } returns Unit

            wishListRemoteDataSourceImpl.deleteMovieFromList(listId, movieId)

            coVerify(exactly = 1) { wishListApiService.removeMovieFromList(listId, movieId) }
        }

    @Test
    fun `deleteMovieFromList should throw InvalidCredentialsException for 401 error with specific status code`() =
        runTest {
            coEvery {
                wishListApiService.removeMovieFromList(
                    any(),
                    any()
                )
            } throws createHttpException(401, errorBody401)
            coEvery { jsonMock.decodeFromString<AuthenticationRemoteResponse>(any<String>()) } returns authenticationRemoteResponse401

            assertThrows<InvalidCredentialsException> {
                wishListRemoteDataSourceImpl.deleteMovieFromList(
                    listId,
                    movieId
                )
            }
        }

    @Test
    fun `deleteMovieFromList should throw NetworkException when the API call fails`() = runTest {
        coEvery { wishListApiService.removeMovieFromList(any(), any()) } throws networkException

        assertThrows<NetworkException> {
            wishListRemoteDataSourceImpl.deleteMovieFromList(
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

    private val remoteListResponse = WishListDetailsRemoteResponse(
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

    private val wishListsSuccessResponse = WishListRemoteResponse(
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

    private val movieInListResponse = WishListMovieItemStatusRemoteResponse(
        id = listId.toString(),
        itemPresent = true
    )

    private val movieNotInListResponse = WishListMovieItemStatusRemoteResponse(
        id = listId.toString(),
        itemPresent = false
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
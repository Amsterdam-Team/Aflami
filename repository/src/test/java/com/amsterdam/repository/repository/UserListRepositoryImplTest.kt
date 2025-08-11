package com.amsterdam.repository.repository

import com.amsterdam.domain.exceptions.NoInternetException
import com.amsterdam.domain.exceptions.UnknownException
import com.amsterdam.domain.repository.AppPreferencesRepository
import com.amsterdam.domain.repository.AuthenticationRepository
import com.amsterdam.domain.repository.UserListRepository
import com.amsterdam.entity.UserList
import com.amsterdam.repository.datasource.remote.UserListRemoteSource
import com.amsterdam.repository.dto.remote.AddItemToListResponse
import com.amsterdam.repository.dto.remote.CreateUserListResponse
import com.amsterdam.repository.dto.remote.RemoteUserListDto
import com.amsterdam.repository.dto.remote.RemoteUserListResponse
import com.amsterdam.repository.mapper.remote.toMovie
import com.amsterdam.repository.utils.listItems
import com.amsterdam.repository.utils.remoteListResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UserListRepositoryImplTest {

    private lateinit var userListRepository: UserListRepository
    private lateinit var userListRemoteSource: UserListRemoteSource
    private lateinit var authenticationRepository: AuthenticationRepository
    private lateinit var preferences: AppPreferencesRepository

    @BeforeEach
    fun setUp() {
        userListRemoteSource = mockk()
        authenticationRepository = mockk()
        preferences = mockk()
        userListRepository = UserListRepositoryImpl(
            userListRemoteSource,
            authenticationRepository,
            preferences
        )
    }

    @Test
    fun `addMovieToList should call addMovieToList from userListRemoteSource`() =
        runTest {
            val listId = 1L
            val sessionId = "123"
            val movieId = 456
            coEvery { authenticationRepository.getSessionId() } returns sessionId
            coEvery {
                userListRemoteSource
                    .addMovieToList(
                        listId,
                        sessionId,
                        movieId,
                    )
            } returns AddItemToListResponse(1, "", true)

            userListRepository.addMovieToList(listId, movieId)

            coVerify { authenticationRepository.getSessionId() }
            coVerify { userListRemoteSource.addMovieToList(listId, sessionId, movieId) }
        }

    @Test
    fun `addMovieToList should throw UnknownException when addMovieToList fails`() =
        runTest {
            val listId = 1L
            val sessionId = "123"
            val movieId = 456
            coEvery { authenticationRepository.getSessionId() } returns sessionId
            coEvery {
                userListRemoteSource
                    .addMovieToList(
                        listId,
                        sessionId,
                        movieId,
                    )
            } returns AddItemToListResponse(1, "", false)

            assertThrows<UnknownException> { userListRepository.addMovieToList(listId, movieId) }
        }

    @Test
    fun `addMovieToList should throw UnknownException when getSessionId returns empty string`() =
        runTest {
            val listId = 1L
            val movieId = 456
            coEvery { authenticationRepository.getSessionId() } throws UnknownException()

            assertThrows<UnknownException> { userListRepository.addMovieToList(listId, movieId) }
        }

    @Test
    fun `createNewList should call createNewList from userListRemoteSource`() =
        runTest {
            val listName = "New List"
            val sessionId = "123"
            val language = "en"
            val response = CreateUserListResponse(1, 1, "", true)
            coEvery { authenticationRepository.getSessionId() } returns sessionId
            coEvery { preferences.getAppLanguage() } returns flowOf(language)
            coEvery {
                userListRemoteSource
                    .createNewList(
                        listName,
                        "",
                        language,
                        sessionId,
                    )
            } returns response

            val createdListId = userListRepository.createNewList(listName)

            assertThat(createdListId).isEqualTo(1)
            coVerify { authenticationRepository.getSessionId() }
            coVerify { userListRemoteSource.createNewList(listName, "", language, sessionId) }
        }

    @Test
    fun `createNewList should throw UnknownException when getSessionId returns empty string`() =
        runTest {
            val listName = "New List"
            val language = "en"
            coEvery { authenticationRepository.getSessionId() } throws UnknownException()
            coEvery { preferences.getAppLanguage() } returns flowOf(language)

            assertThrows<UnknownException> { userListRepository.createNewList(listName) }
        }

    @Test
    fun `should call getSessionId from authentication repository when deleteList is called`() =
        runTest {
            val listId = 1L
            val sessionId = "123"
            coEvery { authenticationRepository.getSessionId() } returns sessionId
            coEvery { userListRemoteSource.deleteList(listId, sessionId) } returns Unit

            userListRepository.deleteList(listId)

            coVerify { authenticationRepository.getSessionId() }
            coVerify { userListRemoteSource.deleteList(listId, sessionId) }
        }

    @Test
    fun `should throw UnknownException when getSessionId returns empty string`() = runTest {
        val listId = 1L
        coEvery { authenticationRepository.getSessionId() } throws UnknownException()

        assertThrows<UnknownException> { userListRepository.deleteList(listId) }
    }

    @Test
    fun `should call deleteList from userListRemoteSource when session id is not empty`() =
        runTest {
            val listId = 1L
            val sessionId = "123"
            coEvery { authenticationRepository.getSessionId() } returns sessionId
            coEvery { userListRemoteSource.deleteList(listId, sessionId) } returns Unit

            userListRepository.deleteList(listId)

            coVerify { userListRemoteSource.deleteList(listId, sessionId) }
        }

    @Test
    fun `should throw NoInternetException when deleteList failed`() = runTest {
        val listId = 1L
        val sessionId = "123"
        coEvery { authenticationRepository.getSessionId() } returns sessionId
        coEvery { userListRemoteSource.deleteList(listId, sessionId) } throws NoInternetException()

        assertThrows<NoInternetException> { userListRepository.deleteList(listId) }
    }

    @Test
    fun `should call getSessionId from authentication repository when removeFromList called is called`() =
        runTest {
            val listId = 1L
            val movieId = 1L
            val sessionId = "123"
            coEvery { authenticationRepository.getSessionId() } returns sessionId
            coEvery {
                userListRemoteSource.removeMovieFromList(
                    listId,
                    sessionId,
                    movieId
                )
            } returns Unit

            userListRepository.removeMovieFromList(listId, movieId)

            coVerify { authenticationRepository.getSessionId() }
            coVerify { userListRemoteSource.removeMovieFromList(listId, sessionId, movieId) }
        }

    @Test
    fun `should throw UnknownException when tru to remove movie with no session`() = runTest {
        val listId = 1L
        val movieId = 1L
        coEvery { authenticationRepository.getSessionId() } throws UnknownException()

        assertThrows<UnknownException> { userListRepository.removeMovieFromList(listId, movieId) }
    }

    @Test
    fun `should call removeMovieFromList from listRemoteSource when session id is not empty`() =
        runTest {
            val listId = 1L
            val sessionId = "123"
            coEvery { authenticationRepository.getSessionId() } returns sessionId
            coEvery { userListRemoteSource.deleteList(listId, sessionId) } returns Unit

            userListRepository.deleteList(listId)

            coVerify { userListRemoteSource.deleteList(listId, sessionId) }
        }

    @Test
    fun `should return list of movies when response return with results`() = runTest {
        val listId = 1L
        val response = remoteListResponse.copy(items = listItems)
        coEvery { userListRemoteSource.getMoviesAndTvShowsFromList(listId, 1) } returns response

        val result = userListRepository.getMoviesAndTvShowsFromList(listId, 1)

        assertThat(result.listDetailsMovies).containsExactlyElementsIn(listItems.filter { it.mediaType == "movie" }.map { it.toMovie() })
    }

    @Test
    fun `should return empty list of movies when response returns empty list with`() = runTest {
        val listId = 1L
        val response = remoteListResponse
        coEvery { userListRemoteSource.getMoviesAndTvShowsFromList(listId, 1) } returns response

        val result = userListRepository.getMoviesAndTvShowsFromList(listId, 1)

        assertThat(result.listDetailsMovies).isEmpty()
    }

    @Test
    fun `getUserList should return list of users when response return with results`() = runTest {
        val accountId = 1
        val page = 1
        val sessionId = "123"
        val expectedUserList = listOf(
            UserList(
                id = 1,
                name = "List 1",
                description = "Description 1",
                itemCount = 2
            )
        )
        val expectedResult = RemoteUserListResponse(
            page = 1,
            results = listOf(
                RemoteUserListDto(
                    id = 1,
                    name = "List 1",
                    description = "Description 1",
                    itemCount = 2
                )
            ),
            totalPages = 1,
            totalResults = 1
        )
        coEvery { authenticationRepository.getSessionId() } returns sessionId

        coEvery {
            userListRemoteSource.getUserLists(
                accountId,
                page,
                sessionId
            )
        } returns expectedResult

        val result = userListRepository.getUserLists(accountId, page)

        assertThat(result).isEqualTo(expectedUserList)
        coVerify { userListRemoteSource.getUserLists(accountId, page, sessionId) }
    }
}

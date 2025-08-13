package com.amsterdam.repository.repository

import com.amsterdam.domain.exceptions.NoInternetException
import com.amsterdam.domain.exceptions.UnknownException
import com.amsterdam.domain.repository.AppPreferencesRepository
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
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UserListRepositoryImplTest {
    private val userListRemoteSource: UserListRemoteSource = mockk()
    private val preferences: AppPreferencesRepository = mockk()

    private val userListRepository: UserListRepository by lazy {
        UserListRepositoryImpl(userListRemoteSource, preferences)
    }

    @Test
    fun `addMovieToList should call addMovieToList from userListRemoteSource`() =
        runTest {
            coEvery { userListRemoteSource.addMovieToList(listId, movieId) }returns AddItemToListResponse(1, "", true)

            userListRepository.addMovieToList(listId, movieId)

            coVerify { userListRemoteSource.addMovieToList(listId, movieId) }
        }

    @Test
    fun `addMovieToList should throw UnknownException when addMovieToList fails`() =
        runTest {
            coEvery { userListRemoteSource.addMovieToList(listId, movieId)
            } returns AddItemToListResponse(1, "", false)

            assertThrows<UnknownException> { userListRepository.addMovieToList(listId, movieId) }
        }

    @Test
    fun `createNewList should call createNewList from userListRemoteSource`() =
        runTest {
            coEvery { preferences.getAppLanguage() } returns flowOf(language)
            coEvery { userListRemoteSource.createNewList(listName, language) } returns fakeUserListResponse

            val createdListId = userListRepository.createNewList(listName)

            assertThat(createdListId).isEqualTo(1)
            coVerify { userListRemoteSource.createNewList(listName, language) }
        }

    @Test
    fun `should call deleteList from userListRemoteSource when session id is not empty`() =
        runTest {
            coEvery { userListRemoteSource.deleteList(listId) } returns Unit

            userListRepository.deleteList(listId)

            coVerify { userListRemoteSource.deleteList(listId) }
        }

    @Test
    fun `should throw NoInternetException when deleteList failed`() = runTest {
        coEvery { userListRemoteSource.deleteList(listId) } throws NoInternetException()

        assertThrows<NoInternetException> { userListRepository.deleteList(listId) }
    }

    @Test
    fun `should call getSessionId from authentication repository when removeFromList called is called`() =
        runTest {
            coEvery { userListRemoteSource.deleteMovieFromList(listId, movieId) } returns Unit

            userListRepository.removeMovieFromList(listId, movieId)

            coVerify { userListRemoteSource.deleteMovieFromList(listId, movieId) }
        }

    @Test
    fun `should call removeMovieFromList from listRemoteSource when session id is not empty`() =
        runTest {
            coEvery { userListRemoteSource.deleteList(listId) } returns Unit

            userListRepository.deleteList(listId)

            coVerify { userListRemoteSource.deleteList(listId) }
        }

    @Test
    fun `should return list of movies when response return with results`() = runTest {
        val response = remoteListResponse.copy(items = listItems)
        coEvery { userListRemoteSource.getMoviesFromList(listId, 1) } returns response

        val result = userListRepository.getMoviesFromList(listId, 1)

        assertThat(result).containsExactlyElementsIn(listItems.map { it.toMovie() })
    }

    @Test
    fun `should return empty list of movies when response returns empty list with`() = runTest {
        coEvery { userListRemoteSource.getMoviesFromList(listId, 1) } returns remoteListResponse

        val result = userListRepository.getMoviesFromList(listId, 1)

        assertThat(result).isEmpty()
    }

    @Test
    fun `getUserList should return list of users when response return with results`() = runTest {
        coEvery { userListRemoteSource.getUserLists(accountId, page) } returns expectedResult

        val result = userListRepository.getUserLists(accountId, page)

        assertThat(result).isEqualTo(expectedUserList)
        coVerify { userListRemoteSource.getUserLists(accountId, page) }
    }

    private val listId = 1L
    private val movieId = 456L
    private val listName = "New List"
    private val language = "en"
    val accountId = 1

    private val expectedUserList = listOf(
        UserList(
            id = 1,
            name = "List 1",
            description = "Description 1",
            itemCount = 2
        )
    )

    private val expectedResult = RemoteUserListResponse(
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

    private val page = 1

    val fakeUserListResponse = CreateUserListResponse(1, 1, "", true)
}

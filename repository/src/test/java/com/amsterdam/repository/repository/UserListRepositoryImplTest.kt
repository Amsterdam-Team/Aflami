package com.amsterdam.repository.repository

import com.amsterdam.domain.exceptions.NoInternetException
import com.amsterdam.domain.exceptions.UnknownException
import com.amsterdam.domain.repository.AppPreferencesRepository
import com.amsterdam.domain.repository.AuthenticationRepository
import com.amsterdam.domain.repository.UserListRepository
import com.amsterdam.repository.datasource.remote.UserListRemoteSource
import com.amsterdam.repository.mapper.remote.toMovie
import com.amsterdam.repository.utils.listItems
import com.amsterdam.repository.utils.remoteListResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
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
    fun `should call getSessionId from authentication repository when deleteList is called`() = runTest{
        // Given
        val listId = 1L
        val sessionId = "123"
        coEvery { authenticationRepository.getSessionId() } returns sessionId
        coEvery { userListRemoteSource.deleteList(listId, sessionId) } returns Unit
        // When
        userListRepository.deleteList(listId)

        // Then
        coVerify { authenticationRepository.getSessionId() }
        coVerify { userListRemoteSource.deleteList(listId, sessionId) }
    }

    @Test
    fun `should throw UnknownException when getSessionId returns empty string`() = runTest{
        val listId = 1L
        coEvery { authenticationRepository.getSessionId() } throws  UnknownException()

        assertThrows<UnknownException> { userListRepository.deleteList(listId) }
    }

    @Test
    fun `should call deleteList from userListRemoteSource when session id is not empty`() = runTest{
        // Given
        val listId = 1L
        val sessionId = "123"
        coEvery { authenticationRepository.getSessionId() } returns sessionId
        coEvery { userListRemoteSource.deleteList(listId, sessionId) } returns Unit

        // When
        userListRepository.deleteList(listId)

        // Then
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
    fun `should call getSessionId from authentication repository when removeFromList called is called`() = runTest{
        // Given
        val listId = 1L
        val movieId = 1L
        val sessionId = "123"
        coEvery { authenticationRepository.getSessionId() } returns sessionId
        coEvery { userListRemoteSource.removeMovieFromList(listId, sessionId, movieId) } returns Unit

        // When
        userListRepository.removeMovieFromList(listId, movieId)

        // Then
        coVerify { authenticationRepository.getSessionId() }
        coVerify { userListRemoteSource.removeMovieFromList(listId, sessionId, movieId) }
    }

    @Test
    fun `should throw UnknownException when tru to remove movie with no session`() = runTest{
        val listId = 1L
        val movieId = 1L
        coEvery { authenticationRepository.getSessionId() } throws  UnknownException()

        assertThrows<UnknownException> { userListRepository.removeMovieFromList(listId, movieId) }
    }

    @Test
    fun `should call removeMovieFromList from listRemoteSource when session id is not empty`() = runTest{
        // Given
        val listId = 1L
        val sessionId = "123"
        coEvery { authenticationRepository.getSessionId() } returns sessionId
        coEvery { userListRemoteSource.deleteList(listId, sessionId) } returns Unit

        // When
        userListRepository.deleteList(listId)

        // Then
        coVerify { userListRemoteSource.deleteList(listId, sessionId) }
    }

    @Test
    fun `should return list of movies when response return with results`() = runTest {
        // Given
        val listId = 1L
        val response = remoteListResponse.copy(items = listItems)
        coEvery { userListRemoteSource.getMoviesFromList(listId, 1) } returns response

        // When
        val result = userListRepository.getMoviesFromList(listId, 1)

        // Then
        assertThat(result).containsExactlyElementsIn(listItems.map { it.toMovie() })
    }

    @Test
    fun `should return empty list of movies when response returns empty list with`() = runTest {
        // Given
        val listId = 1L
        val response = remoteListResponse
        coEvery { userListRemoteSource.getMoviesFromList(listId, 1) } returns response

        // When
        val result = userListRepository.getMoviesFromList(listId, 1)

        // Then
        assertThat(result).isEmpty()
    }
}

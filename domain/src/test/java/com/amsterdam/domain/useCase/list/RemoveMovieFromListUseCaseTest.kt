package com.amsterdam.domain.useCase.list

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.repository.UserListRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class RemoveMovieFromListUseCaseTest {
    private lateinit var removeMovieFromListUseCase: RemoveMovieFromListUseCase
    private lateinit var userListRepository: UserListRepository

    @BeforeEach
    fun setUp() {
        userListRepository = mockk()
        removeMovieFromListUseCase = RemoveMovieFromListUseCase(userListRepository)
    }

    @Test
    fun `should call removeMovieFromList on userListRepository`() = runTest {
        // Given
        val listId = 1L
        val movieId = 1L
        coEvery { userListRepository.removeMovieFromList(listId, movieId) } returns Unit

        // When
        removeMovieFromListUseCase(listId, movieId)

        // Then
        coVerify(exactly = 1) { userListRepository.removeMovieFromList(listId, movieId) }
    }

    @Test
    fun `should throw exception when remove movie failed`() = runTest {
        val listId = 1L
        val movieId = 1L
        coEvery { userListRepository.removeMovieFromList(listId, movieId) } throws AflamiException()

        assertThrows<AflamiException> { removeMovieFromListUseCase(listId, movieId) }
    }
}
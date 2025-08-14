package com.amsterdam.domain.useCase.list

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.repository.UserListRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class RemoveMovieFromListUseCaseTest {
    private val userListRepository: UserListRepository = mockk()
    private val removeMovieFromListUseCase by lazy {
        RemoveMovieFromListUseCase(userListRepository)
    }

    @Test
    fun `should call removeMovieFromList when invoked`() = runTest {
        coEvery { userListRepository.removeMovieFromList(listId, movieId) } returns Unit

        removeMovieFromListUseCase(listId, movieId)

        coVerify(exactly = 1) { userListRepository.removeMovieFromList(listId, movieId) }
    }

    @Test
    fun `should throw exception when remove movie failed`() = runTest {
        coEvery { userListRepository.removeMovieFromList(listId, movieId) } throws AflamiException()

        assertThrows<AflamiException> { removeMovieFromListUseCase(listId, movieId) }
    }

    private val listId = 1L
    private val movieId = 1L
}
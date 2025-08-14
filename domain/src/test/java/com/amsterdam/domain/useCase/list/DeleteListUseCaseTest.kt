package com.amsterdam.domain.useCase.list

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.repository.UserListRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DeleteListUseCaseTest {

    private val userListRepository: UserListRepository = mockk()
    private val deleteListUseCase by lazy {
        DeleteListUseCase(userListRepository)
    }

    @Test
    fun `should call deleteList when invoked`() = runTest {
        coEvery { userListRepository.deleteList(listId) } returns Unit

        deleteListUseCase(listId)

        coVerify(exactly = 1) { userListRepository.deleteList(listId) }
    }

    @Test
    fun `should throw exception when delete list failed`() = runTest {
        coEvery { userListRepository.deleteList(listId) } throws AflamiException()

        assertThrows<AflamiException> { deleteListUseCase(listId) }
    }

    private val listId = 1L
}
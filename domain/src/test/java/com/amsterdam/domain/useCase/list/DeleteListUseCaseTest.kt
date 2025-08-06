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

class DeleteListUseCaseTest {

    private lateinit var deleteListUseCase: DeleteListUseCase
    private lateinit var userListRepository: UserListRepository

    @BeforeEach
    fun setUp() {
        userListRepository = mockk()
        deleteListUseCase = DeleteListUseCase(userListRepository)
    }

    @Test
    fun `should call deleteList on userListRepository`() = runTest {
        // Given
        val listId = 1L
        coEvery { userListRepository.deleteList(listId) } returns Unit

        // When
        deleteListUseCase(listId)

        // Then
        coVerify(exactly = 1) { userListRepository.deleteList(listId) }
    }

    @Test
    fun `should throw exception when delete list failed`() = runTest {
        val listId = 1L
        coEvery { userListRepository.deleteList(listId) } throws AflamiException()

        assertThrows<AflamiException> { deleteListUseCase(listId) }
    }
}
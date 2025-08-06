package com.amsterdam.domain.useCase.list

import com.amsterdam.domain.repository.UserListRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CreateNewListUseCaseTest {
    private lateinit var createNewListUseCase: CreateNewListUseCase
    private lateinit var userListRepository: UserListRepository

    @BeforeEach
    fun setUp() {
        userListRepository = mockk(relaxed = true)
        createNewListUseCase = CreateNewListUseCase(userListRepository)
    }

    @Test
    fun `should call createNewList from userListRepository`() =
        runTest {
            val listName = "My List"

            createNewListUseCase(listName)

            coVerify { userListRepository.createNewList(listName) }
        }
}

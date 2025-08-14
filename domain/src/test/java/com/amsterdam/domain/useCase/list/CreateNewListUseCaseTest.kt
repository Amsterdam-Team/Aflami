package com.amsterdam.domain.useCase.list

import com.amsterdam.domain.repository.UserListRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class CreateNewListUseCaseTest {
    private val userListRepository: UserListRepository = mockk(relaxed = true)
    private val createNewListUseCase by lazy {
        CreateNewListUseCase(userListRepository)
    }

    @Test
    fun `should call createNewList from userListRepository when invoked`() = runTest {
        val listName = "My List"

        createNewListUseCase(listName)

        coVerify { userListRepository.createNewList(listName) }
    }
}

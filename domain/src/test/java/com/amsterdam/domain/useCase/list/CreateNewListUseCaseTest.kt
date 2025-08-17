package com.amsterdam.domain.useCase.list

import com.amsterdam.domain.repository.WishListRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class CreateNewListUseCaseTest {
    private val wishListRepository: WishListRepository = mockk(relaxed = true)
    private val createNewListUseCase by lazy {
        CreateNewListUseCase(wishListRepository)
    }

    @Test
    fun `should call createNewList from userListRepository when invoked`() = runTest {
        val listName = "My List"

        createNewListUseCase(listName)

        coVerify { wishListRepository.createNewList(listName) }
    }
}

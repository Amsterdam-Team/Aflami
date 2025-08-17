package com.amsterdam.domain.useCase.list

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.repository.WishListRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DeleteListUseCaseTest {

    private val wishListRepository: WishListRepository = mockk()
    private val deleteListUseCase by lazy {
        DeleteListUseCase(wishListRepository)
    }

    @Test
    fun `should call deleteList when invoked`() = runTest {
        coEvery { wishListRepository.deleteList(listId) } returns Unit

        deleteListUseCase(listId)

        coVerify(exactly = 1) { wishListRepository.deleteList(listId) }
    }

    @Test
    fun `should throw exception when delete list failed`() = runTest {
        coEvery { wishListRepository.deleteList(listId) } throws AflamiException()

        assertThrows<AflamiException> { deleteListUseCase(listId) }
    }

    private val listId = 1L
}
package com.amsterdam.domain.useCase.list

import com.amsterdam.domain.repository.WishListRepository
import com.amsterdam.entity.WishList
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test


class GetWishListsUseCaseTest {
    private val wishListRepository: WishListRepository = mockk()
    private val getWishListsUseCase by lazy {
        GetWishListsUseCase(wishListRepository)
    }

    @Test
    fun `should return list of user lists when invoked`() = runTest {
        coEvery { wishListRepository.getWishLists(0, page) } returns expectedResult

        val result = getWishListsUseCase(page)

        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `should use default page value when no page is provided`() = runTest {
        val expectedResult = emptyList<WishList>()
        coEvery { wishListRepository.getWishLists(any()) } returns expectedResult

        getWishListsUseCase()

        coVerify(exactly = 1) { wishListRepository.getWishLists(page = 1) }
    }

    @Test
    fun `should return empty list when no user lists found`() = runTest {
        val expectedResult = emptyList<WishList>()
        coEvery { wishListRepository.getWishLists(0, page) } returns expectedResult

        val result = getWishListsUseCase(page)

        assertThat(result).isEqualTo(expectedResult)
    }

    private val page = 1
    private val expectedResult = listOf(
        WishList(
            description = "description",
            itemCount = 2,
            name = "name",
            id = 1
        )
    )
}
package com.amsterdam.domain.useCase.list

import com.amsterdam.domain.repository.UserListRepository
import com.amsterdam.entity.UserList
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test


class GetUserListsUseCaseTest {
    private val userListRepository: UserListRepository = mockk()
    private val getUserListsUseCase by lazy {
        GetUserListsUseCase(userListRepository)
    }

    @Test
    fun `should return list of user lists when invoked`() = runTest {
        coEvery { userListRepository.getUserLists(0, page) } returns expectedResult

        val result = getUserListsUseCase(page)

        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `should use default page value when no page is provided`() = runTest {
        val expectedResult = emptyList<UserList>()
        coEvery { userListRepository.getUserLists(any()) } returns expectedResult

        getUserListsUseCase()

        coVerify(exactly = 1) { userListRepository.getUserLists(page = 1) }
    }

    @Test
    fun `should return empty list when no user lists found`() = runTest {
        val expectedResult = emptyList<UserList>()
        coEvery { userListRepository.getUserLists(0, page) } returns expectedResult

        val result = getUserListsUseCase(page)

        assertThat(result).isEqualTo(expectedResult)
    }

    private val page = 1
    private val expectedResult = listOf(
        UserList(
            description = "description",
            itemCount = 2,
            name = "name",
            id = 1
        )
    )
}
package com.amsterdam.domain.useCase.list

import com.amsterdam.domain.repository.UserListRepository
import com.amsterdam.entity.UserList
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class GetUserListsUseCaseTest {
    private lateinit var userListRepository: UserListRepository
    private lateinit var getUserListsUseCase: GetUserListsUseCase

    @BeforeEach
    fun setUp() {
        userListRepository = mockk()
        getUserListsUseCase = GetUserListsUseCase(userListRepository)
    }

    @Test
    fun `getUserListsUseCase should return list of user lists`() =runTest{
        //Given
        val page = 1
        val expectedResult = listOf(
            UserList(
                description = "description",
                itemCount =2,
                name = "name",
                id = 1
            )
        )
        coEvery { userListRepository.getUserLists(0,page) } returns expectedResult

        //When
        val result = getUserListsUseCase(page)

        //Then
        assertThat(result).isEqualTo(expectedResult)
        coVerify(exactly = 1) { userListRepository.getUserLists(0,page) }

    }
    @Test
    fun `getUserListsUseCase should return empty list when no user lists found`() =runTest{
        //Given
        val page = 1
        val expectedResult = emptyList<UserList>()
        coEvery { userListRepository.getUserLists(0,page) } returns expectedResult
        //When
        val result = getUserListsUseCase(page)
        //Then
        assertThat(result).isEqualTo(expectedResult)
        coVerify(exactly = 1) { userListRepository.getUserLists(0,page) }


    }

}
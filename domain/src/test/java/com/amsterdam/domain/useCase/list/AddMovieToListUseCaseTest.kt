package com.amsterdam.domain.useCase.list

import com.amsterdam.domain.repository.UserListRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AddMovieToListUseCaseTest {
    private lateinit var addMovieToListUseCase: AddMovieToListUseCase
    private lateinit var userListRepository: UserListRepository

    @BeforeEach
    fun setUp() {
        userListRepository = mockk(relaxed = true)
        addMovieToListUseCase = AddMovieToListUseCase(userListRepository)
    }

    @Test
    fun `should call addMovieToList from userListRepository`() =
        runTest {
            val listId = 1L
            val movieId = 123

            addMovieToListUseCase(listId, movieId)

            coVerify { userListRepository.addMovieToList(listId, movieId) }
        }
}

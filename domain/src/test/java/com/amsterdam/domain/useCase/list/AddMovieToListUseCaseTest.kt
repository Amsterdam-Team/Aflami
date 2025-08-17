package com.amsterdam.domain.useCase.list

import com.amsterdam.domain.repository.WishListRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class AddMovieToListUseCaseTest {
    private val wishListRepository: WishListRepository = mockk(relaxed = true)
    private val addMovieToListUseCase by lazy {
        AddMovieToListUseCase(wishListRepository)
    }

    @Test
    fun `should call addMovieToList from userListRepository when invoked`() = runTest {
        addMovieToListUseCase(listId, movieId)

        coVerify { wishListRepository.addMovieToList(listId, movieId) }
    }

    private val listId = 1L
    private val movieId = 123L

}

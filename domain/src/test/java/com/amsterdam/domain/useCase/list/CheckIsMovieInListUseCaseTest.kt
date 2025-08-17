package com.amsterdam.domain.useCase.list

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.repository.WishListRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CheckIsMovieInListUseCaseTest {
    private val wishListRepository: WishListRepository = mockk()
    private val checkIsMovieInListUseCase by lazy {
        CheckIsMovieInListUseCase(wishListRepository)
    }

    @Test
    fun `should call checkIsMovieInList when invoked`() = runTest {
        coEvery { wishListRepository.checkIsMovieInList(movieId, listId) } returns true

        checkIsMovieInListUseCase(movieId, listId)

        coVerify(exactly = 1) { wishListRepository.checkIsMovieInList(movieId, listId) }
    }

    @Test
    fun `should return false when checkIsMovieInList returns false`() = runTest {
        coEvery { wishListRepository.checkIsMovieInList(movieId, listId) } returns false

        val result = checkIsMovieInListUseCase(movieId, listId)

        assertThat(result).isFalse()
    }

    @Test
    fun `should return true when checkIsMovieInList returns true`() = runTest {
        coEvery { wishListRepository.checkIsMovieInList(movieId, listId) } returns true

        val result = checkIsMovieInListUseCase(movieId, listId)

        assertThat(result).isTrue()
    }

    @Test
    fun `should throw exception when getMoviesFromList failed`() = runTest {
        coEvery { wishListRepository.checkIsMovieInList(movieId, listId) } throws AflamiException()

        assertThrows<AflamiException> { checkIsMovieInListUseCase(movieId, listId) }
    }

    private val listId = 1L
    private val movieId = 1L
}
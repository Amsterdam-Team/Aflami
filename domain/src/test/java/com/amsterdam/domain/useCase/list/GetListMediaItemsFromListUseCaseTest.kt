package com.amsterdam.domain.useCase.list

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.repository.UserListRepository
import com.amsterdam.domain.useCase.utils.specificMovieList
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetListMediaItemsFromListUseCaseTest {

    private val userListRepository: UserListRepository = mockk()
    private val getListMediaItemsFromListUseCase by lazy {
        GetListMediaItemsFromListUseCase(userListRepository)
    }

    @Test
    fun `should call getMoviesFromList when invoked`() = runTest {
        coEvery { userListRepository.getMoviesAndTvShowsFromList(listId, page) } returns emptyResult

        getListMediaItemsFromListUseCase(listId, page)

        coVerify(exactly = 1) { userListRepository.getMoviesAndTvShowsFromList(listId, page) }
    }

    @Test
    fun `should return empty list when userListRepository return empty list`() = runTest {
        coEvery { userListRepository.getMoviesAndTvShowsFromList(listId, page) } returns emptyResult

        val result = getListMediaItemsFromListUseCase(listId, page)

        assertThat(result).isEqualTo(emptyResult)
    }

    @Test
    fun `should return list of movies when userListRepository return list successfully`() = runTest {
        coEvery { userListRepository.getMoviesAndTvShowsFromList(listId, page) } returns repositoryResult

        val result = getListMediaItemsFromListUseCase(listId, page)

        assertThat(result.listDetailsMovies).containsExactlyElementsIn(specificMovieList)
    }

    @Test
    fun `should throw exception when getMoviesFromList failed`() = runTest {
        coEvery { userListRepository.getMoviesAndTvShowsFromList(listId, page) } throws AflamiException()

        assertThrows<AflamiException> { getListMediaItemsFromListUseCase(listId, page) }
    }

    private val listId = 1L
    private val page = 1
    private val emptyResult = GetListMediaItemsFromListUseCase.ListScreenDetailsMediaItems(
        emptyList(),
        emptyList()
    )
    private val repositoryResult = GetListMediaItemsFromListUseCase.ListScreenDetailsMediaItems(
        specificMovieList,
        emptyList()
    )
}
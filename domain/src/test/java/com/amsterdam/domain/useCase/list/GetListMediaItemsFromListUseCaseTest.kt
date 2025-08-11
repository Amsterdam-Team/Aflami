package com.amsterdam.domain.useCase.list

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.repository.UserListRepository
import com.amsterdam.domain.useCase.utils.specificMovieList
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetListMediaItemsFromListUseCaseTest {

    private lateinit var getListMediaItemsFromListUseCase: GetListMediaItemsFromListUseCase
    private lateinit var userListRepository: UserListRepository

    @BeforeEach
    fun setup() {
        userListRepository = mockk()
        getListMediaItemsFromListUseCase = GetListMediaItemsFromListUseCase(userListRepository)
    }

    @Test
    fun `should call getMoviesFromList on userListRepository`() = runTest {
        val listId = 1L
        val page = 1
        val emptyResult = GetListMediaItemsFromListUseCase.ListScreenDetailsMediaItems(
            emptyList(),
            emptyList()
        )
        coEvery { userListRepository.getMoviesAndTvShowsFromList(listId, page) } returns emptyResult

        getListMediaItemsFromListUseCase(listId, page)

        coVerify(exactly = 1) { userListRepository.getMoviesAndTvShowsFromList(listId, page) }
    }

    @Test
    fun `should return empty list userListRepository return empty list`() = runTest {
        val listId = 1L
        val page = 1
        val emptyResult = GetListMediaItemsFromListUseCase.ListScreenDetailsMediaItems(
            emptyList(),
            emptyList()
        )
        coEvery { userListRepository.getMoviesAndTvShowsFromList(listId, page) } returns emptyResult

        val result = getListMediaItemsFromListUseCase(listId, page)

        assertThat(result).isEqualTo(emptyResult)
    }

    @Test
    fun `should return list of movies when userListRepository return list successfully`() = runTest {
        val listId = 1L
        val page = 1
        val repositoryResult = GetListMediaItemsFromListUseCase.ListScreenDetailsMediaItems(
            specificMovieList,
            emptyList()
        )
        coEvery { userListRepository.getMoviesAndTvShowsFromList(listId, page) } returns repositoryResult

        val result = getListMediaItemsFromListUseCase(listId, page)

        assertThat(result.listDetailsMovies).containsExactlyElementsIn(specificMovieList)
    }

    @Test
    fun `should throw exception when getMoviesFromList failed`() = runTest {
        val listId = 1L
        val page = 1
        coEvery { userListRepository.getMoviesAndTvShowsFromList(listId, page) } throws AflamiException()

        assertThrows<AflamiException> { getListMediaItemsFromListUseCase(listId, page) }
    }
}
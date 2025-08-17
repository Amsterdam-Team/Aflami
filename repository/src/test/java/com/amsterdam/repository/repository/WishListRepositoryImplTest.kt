package com.amsterdam.repository.repository

import com.amsterdam.domain.exceptions.NoInternetException
import com.amsterdam.domain.exceptions.UnknownException
import com.amsterdam.domain.repository.AppPreferencesRepository
import com.amsterdam.domain.repository.WishListRepository
import com.amsterdam.entity.WishList
import com.amsterdam.repository.datasource.remote.WishListRemoteDataSource
import com.amsterdam.repository.dto.remote.AddItemToListRemoteResponse
import com.amsterdam.repository.dto.remote.CreateUserListRemoteResponse
import com.amsterdam.repository.dto.remote.WishListMovieItemStatusRemoteResponse
import com.amsterdam.repository.dto.remote.WishListRemoteDto
import com.amsterdam.repository.dto.remote.WishListRemoteResponse
import com.amsterdam.repository.mapper.toMovieEntity
import com.amsterdam.repository.utils.listItems
import com.amsterdam.repository.utils.remoteListResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class WishListRepositoryImplTest {
    private val wishListRemoteDataSource: WishListRemoteDataSource = mockk()
    private val preferences: AppPreferencesRepository = mockk()

    private val wishListRepository: WishListRepository by lazy {
        WishListRepositoryImpl(wishListRemoteDataSource, preferences)
    }

    @Test
    fun `addMovieToList should call addMovieToList from userListRemoteSource`() =
        runTest {
            coEvery {
                wishListRemoteDataSource.addMovieToList(
                    listId,
                    movieId
                )
            } returns AddItemToListRemoteResponse(1, "", true)

            wishListRepository.addMovieToList(listId, movieId)

            coVerify { wishListRemoteDataSource.addMovieToList(listId, movieId) }
        }

    @Test
    fun `addMovieToList should throw UnknownException when addMovieToList fails`() =
        runTest {
            coEvery {
                wishListRemoteDataSource.addMovieToList(listId, movieId)
            } returns AddItemToListRemoteResponse(1, "", false)

            assertThrows<UnknownException> { wishListRepository.addMovieToList(listId, movieId) }
        }

    @Test
    fun `createNewList should call createNewList from userListRemoteSource`() =
        runTest {
            coEvery { preferences.getAppLanguage() } returns flowOf(language)
            coEvery {
                wishListRemoteDataSource.createNewList(
                    listName,
                    language
                )
            } returns fakeUserListResponse

            val createdListId = wishListRepository.createNewList(listName)

            assertThat(createdListId).isEqualTo(1)
            coVerify { wishListRemoteDataSource.createNewList(listName, language) }
        }


    @Test
    fun `checkIsMovieInList should return true when service returns true`() = runTest {
        coEvery { wishListRemoteDataSource.checkIsMovieInList(movieId, listId) } returns movieInListResponse

        val result = wishListRepository.checkIsMovieInList(movieId, listId)

        assertThat(result).isTrue()
    }

    @Test
    fun `checkIsMovieInList should return false when service returns false`() = runTest {
        coEvery { wishListRemoteDataSource.checkIsMovieInList(movieId, listId) } returns movieNotInListResponse

        val result = wishListRepository.checkIsMovieInList(movieId, listId)

        assertThat(result).isFalse()
    }

    @Test
    fun `deleteList should call deleteList from userListRemoteSource when session id is not empty`() =
        runTest {
            coEvery { wishListRemoteDataSource.deleteList(listId) } returns Unit

            wishListRepository.deleteList(listId)

            coVerify { wishListRemoteDataSource.deleteList(listId) }
        }

    @Test
    fun `deleteList should throw NoInternetException when deleteList failed`() = runTest {
        coEvery { wishListRemoteDataSource.deleteList(listId) } throws NoInternetException()

        assertThrows<NoInternetException> { wishListRepository.deleteList(listId) }
    }

    @Test
    fun `should call getSessionId from authentication repository when removeFromList called is called`() =
        runTest {
            coEvery { wishListRemoteDataSource.deleteMovieFromList(listId, movieId) } returns Unit

            wishListRepository.removeMovieFromList(listId, movieId)

            coVerify { wishListRemoteDataSource.deleteMovieFromList(listId, movieId) }
        }

    @Test
    fun `deleteList should call removeMovieFromList from listRemoteSource when session id is not empty`() =
        runTest {
            coEvery { wishListRemoteDataSource.deleteList(listId) } returns Unit

            wishListRepository.deleteList(listId)

            coVerify { wishListRemoteDataSource.deleteList(listId) }
        }

    @Test
    fun `getMoviesAndTvShowsFromList should return list of movies when response return with results`() = runTest {
        val response = remoteListResponse.copy(items = listItems)
        coEvery { wishListRemoteDataSource.getMoviesAndTvShowsFromList(listId, 1) } returns response

        val result = wishListRepository.getMoviesAndTvShowsFromList(listId, 1)

        assertThat(result.listDetailsMovies).containsExactlyElementsIn(listItems.map { it.toMovieEntity() })
    }

    @Test
    fun `getMoviesAndTvShowsFromList should return empty list of movies when response returns empty list with`() = runTest {
        coEvery {
            wishListRemoteDataSource.getMoviesAndTvShowsFromList(
                listId,
                1
            )
        } returns remoteListResponse

        val result = wishListRepository.getMoviesAndTvShowsFromList(listId, 1)

        assertThat(result.listDetailsMovies).isEmpty()
    }

    @Test
    fun `getUserList should return list of users when response return with results`() = runTest {
        coEvery { wishListRemoteDataSource.getWishLists(accountId, page) } returns expectedResult

        val result = wishListRepository.getWishLists(accountId, page)

        assertThat(result).isEqualTo(expectedWishList)
        coVerify { wishListRemoteDataSource.getWishLists(accountId, page) }
    }

    private val listId = 1L
    private val movieId = 456L
    private val listName = "New List"
    private val language = "en"
    private val accountId = 1

    private val expectedWishList = listOf(
        WishList(
            id = 1,
            name = "List 1",
            description = "Description 1",
            itemCount = 2
        )
    )

    private val expectedResult = WishListRemoteResponse(
        page = 1,
        results = listOf(
            WishListRemoteDto(
                id = 1,
                name = "List 1",
                description = "Description 1",
                itemCount = 2
            )
        ),
        totalPages = 1,
        totalResults = 1
    )

    private val movieInListResponse = WishListMovieItemStatusRemoteResponse(
        id = listId.toString(),
        itemPresent = true
    )

    private val movieNotInListResponse = WishListMovieItemStatusRemoteResponse(
        id = listId.toString(),
        itemPresent = false
    )


    private val page = 1

    private val fakeUserListResponse = CreateUserListRemoteResponse(1, 1, "", true)
}

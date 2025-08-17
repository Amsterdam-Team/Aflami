package com.amsterdam.domain.repository

import com.amsterdam.domain.useCase.list.GetListMediaItemsFromListUseCase
import com.amsterdam.entity.WishList

interface WishListRepository {
    suspend fun createNewList(listName: String): Int
    suspend fun getWishLists(accountId: Int = 0, page: Int = 1, ): List<WishList>
    suspend fun deleteList(listId: Long)
    suspend fun checkIsMovieInList(movieId: Long, listId: Long): Boolean

    suspend fun addMovieToList(listId: Long, movieId: Long)
    suspend fun getMoviesAndTvShowsFromList(listId: Long, page: Int): GetListMediaItemsFromListUseCase.ListScreenDetailsMediaItems
    suspend fun removeMovieFromList(listId: Long, movieId: Long)
}

package com.amsterdam.domain.repository

import com.amsterdam.domain.useCase.list.GetListMediaItemsFromListUseCase
import com.amsterdam.entity.UserList

interface UserListRepository {
    suspend fun createNewList(listName: String): Int
    suspend fun getUserLists(accountId: Int = 0, page: Int = 1, ): List<UserList>
    suspend fun deleteList(listId: Long)

    suspend fun addMovieToList(listId: Long, movieId: Long)
    suspend fun getMoviesAndTvShowsFromList(listId: Long, page: Int): GetListMediaItemsFromListUseCase.ListScreenDetailsMediaItems
    suspend fun removeMovieFromList(listId: Long, movieId: Long)
}

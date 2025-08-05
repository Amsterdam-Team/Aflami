package com.amsterdam.domain.repository

import com.amsterdam.entity.Movie
import com.amsterdam.entity.UserList

interface UserListRepository {
    suspend fun createNewList(listName: String): Int

    suspend fun addMovieToList(
        listId: Long,
        movieId: Int,
    )
    suspend fun getUserLists(
        accountId: Int = 0,
        page: Int = 1,
    ): List<UserList>

    suspend fun getMoviesFromList(listId: Long, page: Int): List<Movie>
    suspend fun deleteList(listId: Long)
    suspend fun removeMovieFromList(listId: Long, movieId: Long)
}

package com.amsterdam.repository.datasource.remote

import com.amsterdam.repository.dto.remote.AddItemToListRemoteResponse
import com.amsterdam.repository.dto.remote.CreateUserListRemoteResponse
import com.amsterdam.repository.dto.remote.UserListRemoteResponse
import com.amsterdam.repository.dto.remote.UserListDetailsRemoteResponse

interface UserListRemoteDataSource {
    suspend fun createNewList(listName: String, language: String): CreateUserListRemoteResponse
    suspend fun getUserLists(accountId: Int, page: Int): UserListRemoteResponse
    suspend fun deleteList(listId: Long)

    suspend fun addMovieToList(listId: Long, movieId: Long): AddItemToListRemoteResponse
    suspend fun getMoviesAndTvShowsFromList(listId: Long, page: Int): UserListDetailsRemoteResponse
    suspend fun deleteMovieFromList(listId: Long, movieId: Long)
}

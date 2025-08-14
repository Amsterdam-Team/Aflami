package com.amsterdam.repository.datasource.remote

import com.amsterdam.repository.dto.remote.AddItemToListResponse
import com.amsterdam.repository.dto.remote.CreateUserListResponse
import com.amsterdam.repository.dto.remote.RemoteUserListResponse
import com.amsterdam.repository.dto.remote.UserListDetailsResponse

interface UserListRemoteDataSource {
    suspend fun createNewList(listName: String, language: String, ): CreateUserListResponse
    suspend fun getUserLists(accountId: Int, page: Int): RemoteUserListResponse
    suspend fun deleteList(listId: Long)

    suspend fun addMovieToList(listId: Long, movieId: Long): AddItemToListResponse
    suspend fun getMoviesAndTvShowsFromList(listId: Long, page: Int): UserListDetailsResponse
    suspend fun deleteMovieFromList(listId: Long, movieId: Long)
}

package com.amsterdam.repository.datasource.remote

import com.amsterdam.repository.dto.remote.AddItemToListResponse
import com.amsterdam.repository.dto.remote.CreateUserListResponse
import com.amsterdam.repository.dto.remote.RemoteUserListResponse
import com.amsterdam.repository.dto.remote.UserListDetailsResponse

interface UserListRemoteSource {
    suspend fun createNewList(
        listName: String,
        description: String,
        language: String,
        sessionId: String,
    ): CreateUserListResponse

    suspend fun getUserLists(
        accountId: Int,
        page: Int,
        sessionId: String
    ): RemoteUserListResponse
    suspend fun addMovieToList(
        listId: Long,
        sessionId: String,
        movieId: Int,
    ): AddItemToListResponse

    suspend fun getMoviesAndTvShowsFromList(listId: Long, page: Int): UserListDetailsResponse
    suspend fun deleteList(listId: Long, sessionId: String)
    suspend fun removeMovieFromList(listId: Long, sessionId: String, movieId: Long)
}

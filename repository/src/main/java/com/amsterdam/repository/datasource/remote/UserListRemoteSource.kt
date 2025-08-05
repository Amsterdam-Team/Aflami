package com.amsterdam.repository.datasource.remote

import com.amsterdam.repository.dto.remote.CreateUserListResponse
import com.amsterdam.repository.dto.remote.UserListDetailsResponse
import com.amsterdam.repository.dto.remote.RemoteUserListResponse

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

    suspend fun getMoviesFromList(listId: Long, page: Int): UserListDetailsResponse
    suspend fun deleteList(listId: Long, sessionId: String)
    suspend fun removeMovieFromList(listId: Long, sessionId: String, movieId: Long)
}
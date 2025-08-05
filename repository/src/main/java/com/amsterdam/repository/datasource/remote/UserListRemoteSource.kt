package com.amsterdam.repository.datasource.remote

import com.amsterdam.repository.dto.remote.RemoteUserListResponse
import com.amsterdam.repository.dto.remote.UserListDetailsResponse

interface UserListRemoteSource {
    suspend fun getUserLists(
        accountId: Int,
        page: Int,
        sessionId: String
    ): RemoteUserListResponse
    suspend fun addMovieToList(
        listId: Long,
        sessionId: String,
        movieId: Int,
    )

    suspend fun getMoviesFromList(listId: Long, page: Int): UserListDetailsResponse
    suspend fun deleteList(listId: Long, sessionId: String)
    suspend fun removeMovieFromList(listId: Long, sessionId: String, movieId: Long)
}

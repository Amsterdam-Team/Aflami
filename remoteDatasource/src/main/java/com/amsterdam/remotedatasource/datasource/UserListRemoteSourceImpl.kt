package com.amsterdam.remotedatasource.datasource

import com.amsterdam.remotedatasource.api.UserListApiService
import com.amsterdam.remotedatasource.utils.apiHandler.responseCall
import com.amsterdam.repository.datasource.remote.UserListRemoteSource
import com.amsterdam.repository.dto.remote.UserListDetailsResponse
import javax.inject.Inject

class UserListRemoteSourceImpl @Inject constructor(
    private val userListApiService: UserListApiService
) : UserListRemoteSource {
    override suspend fun getMoviesFromList(
        listId: Long,
        page: Int
    ): UserListDetailsResponse {
        return responseCall { userListApiService.getMoviesFromList(listId, page) }
    }

    override suspend fun deleteList(listId: Long, sessionId: String) {
        responseCall { userListApiService.deleteList(listId, sessionId) }
    }

    override suspend fun removeMovieFromList(listId: Long, sessionId: String, movieId: Long) {
        responseCall { userListApiService.removeMovieFromList(listId, sessionId, movieId) }
    }
}
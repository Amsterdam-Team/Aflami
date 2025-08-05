package com.amsterdam.remotedatasource.datasource

import com.amsterdam.remotedatasource.api.UserListApiService
import com.amsterdam.remotedatasource.utils.apiHandler.responseCall
import com.amsterdam.repository.datasource.remote.UserListRemoteSource
import com.amsterdam.repository.dto.remote.RemoteUserListResponse
import com.amsterdam.repository.dto.remote.UserListDetailsResponse
import javax.inject.Inject

class UserListRemoteDataSourceImpl @Inject constructor(
    private val userListApiService: UserListApiService
) : UserListRemoteSource {
    override suspend fun getUserLists(
        accountId: Int,
        page: Int,
        sessionId: String
    ): RemoteUserListResponse {
        return responseCall {  userListApiService.getUserLists(accountId, page, sessionId) }
    }

    override suspend fun getMoviesFromList(
        listId: Long,
        page: Int,
    ): UserListDetailsResponse = responseCall { userListApiService.getMoviesFromList(listId, page) }

    override suspend fun deleteList(
        listId: Long,
        sessionId: String,
    ) {
        responseCall { userListApiService.deleteList(listId, sessionId) }
    }

    override suspend fun removeMovieFromList(
        listId: Long,
        sessionId: String,
        movieId: Long,
    ) {
        responseCall { userListApiService.removeMovieFromList(listId, sessionId, movieId) }
    }
}

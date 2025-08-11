package com.amsterdam.remotedatasource.datasource

import com.amsterdam.remotedatasource.api.UserListApiService
import com.amsterdam.remotedatasource.utils.apiHandler.responseCall
import com.amsterdam.repository.datasource.remote.UserListRemoteSource
import com.amsterdam.repository.dto.remote.AddItemToListResponse
import com.amsterdam.repository.dto.remote.CreateUserListResponse
import com.amsterdam.repository.dto.remote.RemoteUserListResponse
import com.amsterdam.repository.dto.remote.UserListDetailsResponse
import com.amsterdam.repository.dto.remote.authentication.AuthenticationResponseDto
import kotlinx.serialization.json.Json
import javax.inject.Inject

class UserListRemoteDataSourceImpl @Inject constructor(
    private val userListApiService: UserListApiService,
    private val json: Json
) : UserListRemoteSource {
    override suspend fun createNewList(
        listName: String,
        description: String,
        language: String,
        sessionId: String,
    ): CreateUserListResponse {
        return responseCall {
            userListApiService.createNewList(
                sessionId = sessionId,
                listName = listName,
                description = description,
                language = language,
            )
        }
    }

    override suspend fun getUserLists(
        accountId: Int,
        page: Int,
        sessionId: String
    ): RemoteUserListResponse {
        return responseCall({ userListApiService.getUserLists(accountId, page, sessionId) }) {
            val response = json.decodeFromString<AuthenticationResponseDto>(it)
            response.statusCode!!
        }
    }

    override suspend fun addMovieToList(
        listId: Long,
        sessionId: String,
        movieId: Int,
    ): AddItemToListResponse {
        return responseCall { userListApiService.addMediaItemToList(listId, sessionId, movieId) }
    }

    override suspend fun getMoviesAndTvShowsFromList(listId: Long, page: Int): UserListDetailsResponse {
        return responseCall({ userListApiService.getMoviesAndTvShowsFromList(listId, page) }) {
            val response = json.decodeFromString<AuthenticationResponseDto>(it)
            response.statusCode!!
        }
    }

    override suspend fun deleteList(listId: Long, sessionId: String) {
        responseCall({ userListApiService.deleteList(listId, sessionId) }) {
            val response = json.decodeFromString<AuthenticationResponseDto>(it)
            response.statusCode!!
        }
    }

    override suspend fun removeMovieFromList(listId: Long, sessionId: String, movieId: Long) {
        responseCall({ userListApiService.removeMovieFromList(listId, sessionId, movieId) }) {
            val response = json.decodeFromString<AuthenticationResponseDto>(it)
            response.statusCode!!
        }
    }
}

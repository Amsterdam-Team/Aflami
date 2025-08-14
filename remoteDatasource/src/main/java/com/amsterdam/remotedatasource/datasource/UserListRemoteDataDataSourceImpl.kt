package com.amsterdam.remotedatasource.datasource

import com.amsterdam.remotedatasource.api.UserListApiService
import com.amsterdam.remotedatasource.utils.apiHandler.responseCall
import com.amsterdam.repository.datasource.remote.UserListRemoteDataSource
import com.amsterdam.repository.dto.remote.AddItemToListResponse
import com.amsterdam.repository.dto.remote.CreateUserListResponse
import com.amsterdam.repository.dto.remote.RemoteUserListResponse
import com.amsterdam.repository.dto.remote.UserListDetailsResponse
import com.amsterdam.repository.dto.remote.authentication.AuthenticationResponseDto
import kotlinx.serialization.json.Json
import javax.inject.Inject

class UserListRemoteDataDataSourceImpl @Inject constructor(
    private val userListApiService: UserListApiService,
    private val json: Json
) : UserListRemoteDataSource {
    override suspend fun createNewList(
        listName: String,
        language: String,
    ): CreateUserListResponse {
        return responseCall {
            userListApiService.createNewList(listName = listName, language = language)
        }
    }

    override suspend fun getUserLists(accountId: Int, page: Int): RemoteUserListResponse {
        return responseCall({ userListApiService.getUserLists(accountId, page) }) {
            val response = json.decodeFromString<AuthenticationResponseDto>(it)
            response.statusCode!!
        }
    }

    override suspend fun addMovieToList(listId: Long, movieId: Long): AddItemToListResponse {
        return responseCall { userListApiService.addMediaItemToList(listId, movieId) }
    }

    override suspend fun getMoviesAndTvShowsFromList(
        listId: Long,
        page: Int
    ): UserListDetailsResponse {
        return responseCall({ userListApiService.getMoviesAndTvShowsFromList(listId, page) }) {
            val response = json.decodeFromString<AuthenticationResponseDto>(it)
            response.statusCode!!
        }
    }

    override suspend fun deleteList(listId: Long) {
        responseCall({ userListApiService.deleteList(listId) }) {
            val response = json.decodeFromString<AuthenticationResponseDto>(it)
            response.statusCode!!
        }
    }

    override suspend fun deleteMovieFromList(listId: Long, movieId: Long) {
        responseCall({ userListApiService.removeMovieFromList(listId, movieId) }) {
            val response = json.decodeFromString<AuthenticationResponseDto>(it)
            response.statusCode!!
        }
    }
}
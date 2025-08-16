package com.amsterdam.remotedatasource.datasource

import com.amsterdam.remotedatasource.api.UserListApiService
import com.amsterdam.remotedatasource.utils.apiHandler.responseCall
import com.amsterdam.repository.datasource.remote.UserListRemoteDataSource
import com.amsterdam.repository.dto.remote.AddItemToListRemoteResponse
import com.amsterdam.repository.dto.remote.CreateUserListRemoteResponse
import com.amsterdam.repository.dto.remote.UserListDetailsRemoteResponse
import com.amsterdam.repository.dto.remote.UserListRemoteResponse
import com.amsterdam.repository.dto.remote.authentication.AuthenticationRemoteResponse
import kotlinx.serialization.json.Json
import javax.inject.Inject

class UserListRemoteDataSourceImpl @Inject constructor(
    private val userListApiService: UserListApiService,
    private val json: Json
) : UserListRemoteDataSource {
    override suspend fun createNewList(
        listName: String,
        language: String,
    ): CreateUserListRemoteResponse {
        return responseCall(execute = {
            userListApiService.createNewList(listName = listName, language = language)
        })
    }

    override suspend fun getUserLists(accountId: Int, page: Int): UserListRemoteResponse {
        return responseCall({ userListApiService.getUserLists(accountId, page) }) {
            val response = json.decodeFromString<AuthenticationRemoteResponse>(it)
            response.statusCode!!
        }
    }

    override suspend fun addMovieToList(listId: Long, movieId: Long): AddItemToListRemoteResponse {
        return responseCall(execute = { userListApiService.addMediaItemToList(listId, movieId) })
    }

    override suspend fun getMoviesAndTvShowsFromList(
        listId: Long,
        page: Int
    ): UserListDetailsRemoteResponse {
        return responseCall({ userListApiService.getMoviesAndTvShowsFromList(listId, page) }) {
            val response = json.decodeFromString<AuthenticationRemoteResponse>(it)
            response.statusCode!!
        }
    }

    override suspend fun deleteList(listId: Long) {
        responseCall({ userListApiService.deleteList(listId) }) {
            val response = json.decodeFromString<AuthenticationRemoteResponse>(it)
            response.statusCode!!
        }
    }

    override suspend fun deleteMovieFromList(listId: Long, movieId: Long) {
        responseCall({ userListApiService.removeMovieFromList(listId, movieId) }) {
            val response = json.decodeFromString<AuthenticationRemoteResponse>(it)
            response.statusCode!!
        }
    }
}
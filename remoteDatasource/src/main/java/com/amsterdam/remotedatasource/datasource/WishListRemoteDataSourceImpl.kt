package com.amsterdam.remotedatasource.datasource

import com.amsterdam.remotedatasource.api.WishListApiService
import com.amsterdam.remotedatasource.utils.apiHandler.responseCall
import com.amsterdam.repository.datasource.remote.WishListRemoteDataSource
import com.amsterdam.repository.dto.remote.AddItemToListRemoteResponse
import com.amsterdam.repository.dto.remote.CreateUserListRemoteResponse
import com.amsterdam.repository.dto.remote.WishListDetailsRemoteResponse
import com.amsterdam.repository.dto.remote.WishListMovieItemStatusRemoteResponse
import com.amsterdam.repository.dto.remote.WishListRemoteResponse
import com.amsterdam.repository.dto.remote.authentication.AuthenticationRemoteResponse
import kotlinx.serialization.json.Json
import javax.inject.Inject

class WishListRemoteDataSourceImpl @Inject constructor(
    private val wishListApiService: WishListApiService,
    private val json: Json
) : WishListRemoteDataSource {
    override suspend fun createNewList(
        listName: String,
        language: String,
    ): CreateUserListRemoteResponse {
        return responseCall(execute = {
            wishListApiService.createNewList(listName = listName, language = language)
        })
    }

    override suspend fun getWishLists(accountId: Int, page: Int): WishListRemoteResponse {
        return responseCall({ wishListApiService.getWishLists(accountId, page) }) {
            val response = json.decodeFromString<AuthenticationRemoteResponse>(it)
            response.statusCode!!
        }
    }

    override suspend fun addMovieToList(listId: Long, movieId: Long): AddItemToListRemoteResponse {
        return responseCall(
            execute = { wishListApiService.addMediaItemToList(listId, movieId) })
    }

    override suspend fun getMoviesAndTvShowsFromList(
        listId: Long,
        page: Int
    ): WishListDetailsRemoteResponse {
        return responseCall(
            { wishListApiService.getMoviesAndTvShowsFromList(listId, page) }) {
            val response = json.decodeFromString<AuthenticationRemoteResponse>(it)
            response.statusCode!!
        }
    }

    override suspend fun checkIsMovieInList(
        movieId: Long,
        listId: Long
    ): WishListMovieItemStatusRemoteResponse {
        return responseCall(
            execute = { wishListApiService.checkIsMovieInList(movieId = movieId, listId = listId) })
    }

    override suspend fun deleteList(listId: Long) {
        responseCall({ wishListApiService.deleteList(listId) }) {
            val response = json.decodeFromString<AuthenticationRemoteResponse>(it)
            response.statusCode!!
        }
    }

    override suspend fun deleteMovieFromList(listId: Long, movieId: Long) {
        responseCall({ wishListApiService.removeMovieFromList(listId, movieId) }) {
            val response = json.decodeFromString<AuthenticationRemoteResponse>(it)
            response.statusCode!!
        }
    }
}
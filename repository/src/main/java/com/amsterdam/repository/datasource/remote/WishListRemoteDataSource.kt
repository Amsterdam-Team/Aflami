package com.amsterdam.repository.datasource.remote

import com.amsterdam.repository.dto.remote.AddItemToListRemoteResponse
import com.amsterdam.repository.dto.remote.CreateUserListRemoteResponse
import com.amsterdam.repository.dto.remote.WishListRemoteResponse
import com.amsterdam.repository.dto.remote.WishListDetailsRemoteResponse
import com.amsterdam.repository.dto.remote.WishListMovieItemStatusRemoteResponse

interface WishListRemoteDataSource {
    suspend fun createNewList(listName: String, language: String): CreateUserListRemoteResponse

    suspend fun getWishLists(accountId: Int, page: Int): WishListRemoteResponse

    suspend fun deleteList(listId: Long)
    suspend fun checkIsMovieInList(movieId: Long, listId: Long): WishListMovieItemStatusRemoteResponse
    suspend fun addMovieToList(listId: Long, movieId: Long): AddItemToListRemoteResponse

    suspend fun getMoviesAndTvShowsFromList(listId: Long, page: Int): WishListDetailsRemoteResponse

    suspend fun deleteMovieFromList(listId: Long, movieId: Long)
}

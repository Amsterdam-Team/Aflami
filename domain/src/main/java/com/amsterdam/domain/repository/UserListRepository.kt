package com.amsterdam.domain.repository

interface UserListRepository {
    suspend fun addMovieToList(listId: String, movieId: Long)
}
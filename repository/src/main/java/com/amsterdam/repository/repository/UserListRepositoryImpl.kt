package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.UserListRepository

class UserListRepositoryImpl : UserListRepository {
    override suspend fun addMovieToList(listId: String, movieId: Long) {
        TODO("Not yet implemented")
    }
}
package com.amsterdam.domain.repository

import com.amsterdam.entity.Movie

interface UserListRepository {
    suspend fun getMoviesFromList(listId: Long, page: Int): List<Movie>
    suspend fun deleteList(listId: Long)
    suspend fun removeMovieFromList(listId: Long, movieId: Long)
}
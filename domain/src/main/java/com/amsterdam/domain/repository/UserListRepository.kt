package com.amsterdam.domain.repository

interface UserListRepository {
    suspend fun addMovieToList(
        listId: Long,
        movieId: Int,
    )

    suspend fun addTvShowToList(
        listId: Long,
        tvShowId: Int,
    )
}

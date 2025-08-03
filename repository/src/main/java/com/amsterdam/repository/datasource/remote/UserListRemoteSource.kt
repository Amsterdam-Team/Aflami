package com.amsterdam.repository.datasource.remote

interface UserListRemoteSource {
    suspend fun addMovieToList(
        listId: Long,
        movieId: Int,
    )

    suspend fun addTvShowToList(
        listId: Long,
        tvShowId: Int,
    )
}

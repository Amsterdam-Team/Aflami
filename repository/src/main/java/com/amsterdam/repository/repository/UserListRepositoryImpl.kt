package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.UserListRepository
import com.amsterdam.repository.datasource.remote.UserListRemoteSource
import javax.inject.Inject

class UserListRepositoryImpl
    @Inject
    constructor(
        private val userListRemoteSource: UserListRemoteSource,
    ) : UserListRepository {
        override suspend fun addMovieToList(
            listId: Long,
            movieId: Int,
        ) {
            userListRemoteSource.addMovieToList(listId, movieId)
        }

        override suspend fun addTvShowToList(
            listId: Long,
            tvShowId: Int,
        ) {
            userListRemoteSource.addTvShowToList(listId, tvShowId)
    }
}

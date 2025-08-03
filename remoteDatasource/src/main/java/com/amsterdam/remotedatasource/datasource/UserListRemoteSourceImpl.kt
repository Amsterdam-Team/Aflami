package com.amsterdam.remotedatasource.datasource

import com.amsterdam.remotedatasource.api.UserListApiService
import com.amsterdam.repository.datasource.remote.UserListRemoteSource
import javax.inject.Inject

class UserListRemoteSourceImpl
    @Inject
    constructor(
        private val userListApiService: UserListApiService,
    ) : UserListRemoteSource {
        override suspend fun addMovieToList(
            listId: Long,
            movieId: Int,
        ) {
            userListApiService.addMediaItemToList(listId, MOVIE_MEDIA_TYPE, movieId)
        }

        override suspend fun addTvShowToList(
            listId: Long,
            tvShowId: Int,
        ) {
            userListApiService.addMediaItemToList(listId, TV_MEDIA_TYPE, tvShowId)
        }

        companion object {
            const val MOVIE_MEDIA_TYPE = "movie"
            const val TV_MEDIA_TYPE = "tv"
        }
    }

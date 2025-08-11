package com.amsterdam.repository.repository

import com.amsterdam.domain.exceptions.UnknownException
import com.amsterdam.domain.repository.AppPreferencesRepository
import com.amsterdam.domain.repository.AuthenticationRepository
import com.amsterdam.domain.repository.UserListRepository
import com.amsterdam.domain.useCase.list.GetListMediaItemsFromListUseCase
import com.amsterdam.entity.Movie
import com.amsterdam.entity.TvShow
import com.amsterdam.entity.UserList
import com.amsterdam.repository.datasource.remote.UserListRemoteSource
import com.amsterdam.repository.mapper.remote.toMovie
import com.amsterdam.repository.mapper.remote.toTvShow
import com.amsterdam.repository.mapper.remote.toUserList
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UserListRepositoryImpl @Inject constructor(
    private val userListDataSource: UserListRemoteSource,
    private val authenticationRepository: AuthenticationRepository,
    private val preferences: AppPreferencesRepository,
) : UserListRepository {

    override suspend fun addMovieToList(listId: Long, movieId: Int) {
        val sessionId = authenticationRepository.getSessionId()
        val response =
            userListDataSource.addMovieToList(listId, sessionId, movieId)
        if (!response.success) throw UnknownException()
    }

    override suspend fun createNewList(listName: String): Int {
        return userListDataSource.createNewList(
            listName = listName,
            description = "",
            language = preferences.getAppLanguage().first(),
            sessionId = authenticationRepository.getSessionId(),
        ).listId
    }

    override suspend fun getMoviesAndTvShowsFromList(listId: Long, page: Int): GetListMediaItemsFromListUseCase.ListScreenDetailsMediaItems {
        val items = userListDataSource.getMoviesAndTvShowsFromList(listId, page).items

        val tvShows = items.filter { it.mediaType == "tv" }.map { it.toTvShow() }
        val movies = items.filter { it.mediaType == "movie" }.map { it.toMovie() }

        return GetListMediaItemsFromListUseCase.ListScreenDetailsMediaItems(
            listDetailsMovies = movies,
            listDetailsShows = tvShows
        )
    }


    override suspend fun deleteList(listId: Long) {
        val sessionId = authenticationRepository.getSessionId()
        userListDataSource.deleteList(listId, sessionId)
    }

    override suspend fun getUserLists(accountId: Int, page: Int): List<UserList> {
        val sessionId = authenticationRepository.getSessionId()
        return userListDataSource.getUserLists(accountId, page, sessionId).results
            .map { it.toUserList() }
    }

    override suspend fun removeMovieFromList(listId: Long, movieId: Long) {
        val sessionId = authenticationRepository.getSessionId()
        userListDataSource.removeMovieFromList(listId, sessionId, movieId)
    }
}

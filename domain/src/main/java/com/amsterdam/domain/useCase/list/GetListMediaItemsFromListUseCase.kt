package com.amsterdam.domain.useCase.list

import com.amsterdam.domain.repository.UserListRepository
import com.amsterdam.entity.Movie
import com.amsterdam.entity.TvShow

class GetListMediaItemsFromListUseCase(
    private val userListRepository: UserListRepository
) {
    suspend operator fun invoke(listId: Long, page: Int): ListScreenDetailsMediaItems {
        return userListRepository.getMoviesAndTvShowsFromList(listId, page)
    }

    data class ListScreenDetailsMediaItems(
        val listDetailsMovies: List<Movie>,
        val listDetailsShows: List<TvShow>
    )
}

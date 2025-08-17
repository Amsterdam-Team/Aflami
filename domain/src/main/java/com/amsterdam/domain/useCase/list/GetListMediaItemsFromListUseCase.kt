package com.amsterdam.domain.useCase.list

import com.amsterdam.domain.repository.WishListRepository
import com.amsterdam.entity.Movie
import com.amsterdam.entity.TvShow

class GetListMediaItemsFromListUseCase(
    private val wishListRepository: WishListRepository
) {
    suspend operator fun invoke(listId: Long, page: Int): ListScreenDetailsMediaItems {
        return wishListRepository.getMoviesAndTvShowsFromList(listId, page)
    }

    data class ListScreenDetailsMediaItems(
        val listDetailsMovies: List<Movie>,
        val listDetailsShows: List<TvShow>
    )
}

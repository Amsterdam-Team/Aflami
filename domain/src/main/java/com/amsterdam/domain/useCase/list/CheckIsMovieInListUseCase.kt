package com.amsterdam.domain.useCase.list

import com.amsterdam.domain.repository.WishListRepository

class CheckIsMovieInListUseCase(
    private val wishListRepository: WishListRepository
) {
    suspend operator fun invoke(
        movieId: Long,
        listId: Long
    ): Boolean {
        return wishListRepository.checkIsMovieInList(movieId = movieId, listId = listId)
    }
}
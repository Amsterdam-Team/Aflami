package com.amsterdam.domain.useCase.list

import com.amsterdam.domain.repository.WishListRepository

class RemoveMovieFromListUseCase(
    private val wishListRepository: WishListRepository
) {
    suspend operator fun invoke(listId: Long, movieId: Long) {
        return wishListRepository.removeMovieFromList(listId, movieId)
    }
}
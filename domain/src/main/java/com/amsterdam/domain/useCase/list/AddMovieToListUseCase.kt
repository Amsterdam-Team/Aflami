package com.amsterdam.domain.useCase.list

import com.amsterdam.domain.repository.WishListRepository

class AddMovieToListUseCase(
    private val wishListRepository: WishListRepository,
) {
    suspend operator fun invoke(
        listId: Long,
        movieId: Long,
    ) {
        wishListRepository.addMovieToList(listId, movieId)
    }
}

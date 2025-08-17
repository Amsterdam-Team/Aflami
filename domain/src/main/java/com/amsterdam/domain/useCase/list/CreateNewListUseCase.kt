package com.amsterdam.domain.useCase.list

import com.amsterdam.domain.repository.WishListRepository

class CreateNewListUseCase(
    private val wishListRepository: WishListRepository,
) {
    suspend operator fun invoke(listName: String): Int = wishListRepository.createNewList(listName)
}

package com.amsterdam.domain.useCase.list

import com.amsterdam.domain.repository.WishListRepository

class DeleteListUseCase(
    private val wishListRepository: WishListRepository
) {
    suspend operator fun invoke(listId: Long) {
        wishListRepository.deleteList(listId)
    }
}
package com.amsterdam.domain.useCase.list

import com.amsterdam.domain.repository.WishListRepository
import com.amsterdam.entity.WishList

class GetWishListsUseCase(
    private val wishListRepository: WishListRepository,
) {
    suspend operator fun invoke(
        page: Int = 1,
    ): List<WishList> {
        return wishListRepository.getWishLists(page = page)
    }
}

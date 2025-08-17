package com.amsterdam.viewmodel.lists

import com.amsterdam.entity.WishList
import com.amsterdam.viewmodel.shared.uiStates.WishListItemUiState

fun WishList.toWishListItemUiState(): WishListItemUiState {
    return WishListItemUiState(
        id = id,
        name = name,
        description = description,
        itemCount = itemCount,
    )
} 
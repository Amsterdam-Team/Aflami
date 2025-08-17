package com.amsterdam.viewmodel.movieDetails

import com.amsterdam.entity.WishList

data class WishListUiState(
    val id: Long = 0,
    val name: String = "",
    val itemCount: Int = 0,
    val isMovieInList: Boolean = false
)

fun WishList.toUiState() =
    WishListUiState(
        id = id.toLong(),
        name = name,
        itemCount = itemCount,
    )

fun List<WishList>.toUiState() = map { it.toUiState() }

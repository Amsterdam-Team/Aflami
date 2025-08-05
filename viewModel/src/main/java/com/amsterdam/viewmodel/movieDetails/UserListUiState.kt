package com.amsterdam.viewmodel.movieDetails

import com.amsterdam.entity.UserList

data class UserListUiState(
    val id: Long = 0,
    val name: String = "",
    val itemCount: Int = 0,
)

fun UserList.toUiState() =
    UserListUiState(
        id = id.toLong(),
        name = name,
        itemCount = itemCount,
    )

fun List<UserList>.toUiState() = map { it.toUiState() }

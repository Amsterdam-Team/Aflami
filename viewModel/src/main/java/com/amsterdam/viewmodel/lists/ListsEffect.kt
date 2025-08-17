package com.amsterdam.viewmodel.lists

sealed interface ListsEffect {
    data object ListCreatedSuccessfully : ListsEffect

    data object FailedToCreateList : ListsEffect

    data class NavigateToListDetails(
        val listId: Long,
        val listName: String,
    ) : ListsEffect

    data object NavigateToLogin : ListsEffect
}

package com.amsterdam.viewmodel.lists

sealed interface ListsEffect {
    object ListCreatedSuccessfully : ListsEffect

    object FailedToCreateList : ListsEffect

    data class NavigateToListDetails(
        val listId: Long,
        val listName: String,
    ) : ListsEffect

    object NavigateToLogin : ListsEffect
}

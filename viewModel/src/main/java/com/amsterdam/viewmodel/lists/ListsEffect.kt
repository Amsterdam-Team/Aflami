package com.amsterdam.viewmodel.lists

sealed interface ListsEffect {
    data object NavigateToAddCustomList : ListsEffect

    data class NavigateToListDetails(
        val listId: Long,
        val listName: String,
    ) : ListsEffect
}

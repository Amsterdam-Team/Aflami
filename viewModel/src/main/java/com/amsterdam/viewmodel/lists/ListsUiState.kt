package com.amsterdam.viewmodel.lists

import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.viewmodel.shared.uiStates.UserListItemUiState

data class ListsUiState(
    val isLoading: Boolean = true,
    val userLists: List<UserListItemUiState> = emptyList(),
    val errorUiState: ListsErrorState? = null,
    val isCreateNewListDialogVisible: Boolean = false,
    val isUserLoggedIn: Boolean = true,
    val listName: String = "",
    val isCreateListLoading: Boolean = false,
) {
    sealed interface ListsErrorState {
        data object NoNetworkConnection : ListsErrorState

        data object UnknownError : ListsErrorState

        companion object {
            fun toListsErrorState(exception: Throwable): ListsErrorState =
                when (exception) {
                    is NetworkException -> NoNetworkConnection
                    else -> UnknownError
                }
        }
    }
}

package com.amsterdam.viewmodel.lists

import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.viewmodel.shared.uiStates.UserListItemUiState

data class ListsUiState(
    val isLoading: Boolean = false,
    val userLists: List<UserListItemUiState> = emptyList(),
    val errorUiState: ListsErrorState? = null
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

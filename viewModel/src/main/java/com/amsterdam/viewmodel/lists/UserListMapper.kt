package com.amsterdam.viewmodel.lists

import com.amsterdam.entity.UserList
import com.amsterdam.viewmodel.shared.uiStates.UserListItemUiState

fun UserList.toUserListItemUiState(): UserListItemUiState {
    return UserListItemUiState(
        id = id,
        name = name,
        description = description,
        itemCount = itemCount,
    )
} 
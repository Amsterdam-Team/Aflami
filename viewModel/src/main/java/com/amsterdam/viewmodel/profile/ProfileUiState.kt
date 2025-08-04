package com.amsterdam.viewmodel.profile

data class ProfileUiState(
    val isUserLoggedIn: Boolean = false,
    val isLoading: Boolean = false,
    val userInfo: UserInfoUiState = UserInfoUiState(),
) {
    data class UserInfoUiState(
        val name: String = ""
    )
}

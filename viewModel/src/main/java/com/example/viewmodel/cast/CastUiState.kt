package com.example.viewmodel.cast

data class CastUiState(
    val cast: List<CastItemUiState> = emptyList(),
    val isLoading: Boolean = false,
    val errorUiState: CastErrorUiState? = null
)

data class CastItemUiState(
    val actorImage: String = "",
    val actorName: String = ""
)

sealed interface CastErrorUiState {
    object NoNetworkConnection : CastErrorUiState
}
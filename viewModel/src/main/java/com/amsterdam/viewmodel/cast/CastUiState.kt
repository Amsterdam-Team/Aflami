package com.amsterdam.viewmodel.cast

data class CastUiState(
    val cast: List<ActorUiState> = emptyList(),
    val isLoading: Boolean = false,
    val errorUiState: CastErrorUiState? = null
) {

    data class ActorUiState(
        val actorImage: String = "",
        val actorName: String = ""
    )

    sealed interface CastErrorUiState {
        data object NoNetworkConnection : CastErrorUiState
    }
}
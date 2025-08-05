package com.amsterdam.viewmodel.search.mapper

import com.amsterdam.domain.useCase.myRating.tvShow.GetUserRatedTvShowsUseCase.UserRatedTvShow
import com.amsterdam.viewmodel.shared.uiStates.TvShowItemUiState
import kotlin.collections.map

fun UserRatedTvShow.toTvShowItemUiState(): TvShowItemUiState {
    val movieItemUiState = tvShow.toMediaItemUiState()
    return movieItemUiState.copy(rate = userRate.toString())
}

fun List<UserRatedTvShow>.toRatedMovieUiStates() = map(UserRatedTvShow::toTvShowItemUiState)
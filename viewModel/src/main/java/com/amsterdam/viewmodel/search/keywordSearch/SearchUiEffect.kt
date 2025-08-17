package com.amsterdam.viewmodel.search.keywordSearch

import com.amsterdam.viewmodel.shared.BaseViewModel

sealed interface SearchUiEffect: BaseViewModel.BaseUiEffect {
    data object NavigateToWorldSearch : SearchUiEffect
    data object NavigateToActorSearch : SearchUiEffect
    data class NavigateToMovieDetails(val movieId: Long) : SearchUiEffect
    data class NavigateToTvShowDetails(val tvShowId: Long) : SearchUiEffect
    data object NavigateBack : SearchUiEffect
}
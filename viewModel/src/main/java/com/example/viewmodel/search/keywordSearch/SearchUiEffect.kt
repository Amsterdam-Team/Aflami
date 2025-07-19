package com.example.viewmodel.search.keywordSearch

import com.example.viewmodel.shared.BaseViewModel

sealed interface SearchUiEffect: BaseViewModel.BaseUiEffect {
    object NavigateToWorldSearch : SearchUiEffect
    object NavigateToActorSearch : SearchUiEffect
    data class NavigateToMovieDetails(val movieId: Long) : SearchUiEffect
    data class NavigateToTvShowDetails(val tvShowId: Long) : SearchUiEffect
    object NavigateBack : SearchUiEffect
}
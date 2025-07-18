package com.example.viewmodel.search.keywordSearch

import com.example.viewmodel.shared.BaseViewModel

sealed interface SearchUiEffect: BaseViewModel.BaseUiEffect {
    object NavigateToWorldSearch : SearchUiEffect
    object NavigateToActorSearch : SearchUiEffect
    data class NavigateToMovieDetails(val movieId: Long) : SearchUiEffect
    object NavigateBack : SearchUiEffect
}
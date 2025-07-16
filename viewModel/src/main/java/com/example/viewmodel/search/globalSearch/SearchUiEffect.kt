package com.example.viewmodel.search.globalSearch

import com.example.viewmodel.BaseViewModel

sealed interface SearchUiEffect: BaseViewModel.BaseUiEffect {
    object NavigateToWorldSearch : SearchUiEffect
    object NavigateToActorSearch : SearchUiEffect
    object NavigateToMovieDetails : SearchUiEffect
    object NavigateBack : SearchUiEffect
}
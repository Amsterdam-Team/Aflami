package com.amsterdam.viewmodel.search

import com.amsterdam.viewmodel.BaseViewModel

sealed interface SearchUiEffect: BaseViewModel.BaseUiEffect {
    object NavigateToWorldSearch : SearchUiEffect
    object NavigateToActorSearch : SearchUiEffect
    object NavigateToMovieDetails : SearchUiEffect
    object NavigateBack : SearchUiEffect
}
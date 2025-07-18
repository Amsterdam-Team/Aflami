package com.example.viewmodel.search.searchByKeyword

import com.example.viewmodel.shared.BaseViewModel

sealed interface SearchUiEffect: BaseViewModel.BaseUiEffect {
    object NavigateToWorldSearch : SearchUiEffect
    object NavigateToActorSearch : SearchUiEffect
    object NavigateToMovieDetails : SearchUiEffect
    object NavigateBack : SearchUiEffect
}
package com.example.viewmodel.movieDetails

sealed interface MovieDetailsEffect {
    object NavigateBackEffect : MovieDetailsEffect
    object NavigateToCastsScreenEffect : MovieDetailsEffect
}
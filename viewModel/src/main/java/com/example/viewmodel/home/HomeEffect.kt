package com.example.viewmodel.home

sealed interface HomeEffect {
    object NavigateToSearchScreenEffect : HomeEffect
}
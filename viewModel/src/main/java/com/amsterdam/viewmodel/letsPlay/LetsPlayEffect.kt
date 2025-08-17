package com.amsterdam.viewmodel.letsPlay

interface LetsPlayEffect {
    data class NavigateToGuessCharacterScreen(val difficulty: String) : LetsPlayEffect
    data class NavigateToGuessMovieByPosterScreen(val difficulty: String) : LetsPlayEffect
    data class NavigateToGuessMovieByReleaseScreen(val difficulty: String) : LetsPlayEffect
    data class NavigateToGuessMovieByGenreScreen(val difficulty: String) : LetsPlayEffect
}
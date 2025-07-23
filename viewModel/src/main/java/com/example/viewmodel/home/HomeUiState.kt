package com.example.viewmodel.home

import com.example.viewmodel.shared.defaultMovieGenres
import com.example.viewmodel.shared.uiStates.MovieGenreItemUiState
import com.example.viewmodel.shared.uiStates.MovieItemUiState

data class HomeUiState(
    val popularMovies : List<PopularMovieItemUiState> = emptyList(),
    val upcomingMovies : List<MovieItemUiState> = emptyList(),
    val upcomingMovieGenres: List<MovieGenreItemUiState> = defaultMovieGenres,
    val topRatedMovies : List<MovieItemUiState> = emptyList(),
    val isLoading : Boolean = false,
    val error : HomeError? = null
){
    data class PopularMovieItemUiState(
        val name : String = "",
        val rating: String = "" ,
        val posterUrl : String = ""
    )

    sealed class HomeError{
        data object NetworkError : HomeError()
    }
}

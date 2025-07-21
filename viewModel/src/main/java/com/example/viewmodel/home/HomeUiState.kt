package com.example.viewmodel.home

data class HomeUiState(
    val popularMovies : List<PopularMovieItemUiState> = emptyList(),
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

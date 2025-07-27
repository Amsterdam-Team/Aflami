package com.amsterdam.viewmodel.home

import com.amsterdam.domain.models.Mood
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.viewmodel.shared.defaultMovieGenres
import com.amsterdam.viewmodel.shared.uiStates.MovieGenreItemUiState
import com.amsterdam.viewmodel.shared.uiStates.MovieItemUiState

data class HomeUiState(
    val popularMoviesSectionUiState: PopularMoviesSectionUiState = PopularMoviesSectionUiState(),
    val upcomingMoviesSectionUiState: UpcomingMoviesSectionUiState = UpcomingMoviesSectionUiState(),
    val topRatedMoviesSectionUiState : TopRatedMoviesSectionUiState = TopRatedMoviesSectionUiState(),
    val continueWatchingMoviesSectionUiState : ContinueWatchingMoviesSectionUiState = ContinueWatchingMoviesSectionUiState(),
    val moodPickerUiState: MoodPickerUiState = MoodPickerUiState(),
    val error : HomeError? = null
){
    data class PopularMoviesSectionUiState(
        val movies : List<PopularMovieItemUiState> = emptyList(),
        val isLoading : Boolean = false,
    )

    data class TopRatedMoviesSectionUiState(
        val movies : List<MovieItemUiState> = emptyList(),
        val isLoading : Boolean = false,
    )

    data class ContinueWatchingMoviesSectionUiState(
        val movies : List<MovieItemUiState> = emptyList(),
        val isLoading : Boolean = false,
    )

    data class UpcomingMoviesSectionUiState(
        val movies : List<MovieItemUiState> = emptyList(),
        val movieGenres: List<MovieGenreItemUiState> = defaultMovieGenres,
        val isLoading : Boolean = false,
    ){
        fun getSelectedUpcomingMovieGenre(): MovieGenre {
            return movieGenres
                .firstOrNull { it.selectableMovieGenre.isSelected }
                ?.selectableMovieGenre?.item ?: MovieGenre.ALL
        }
    }

    data class PopularMovieItemUiState(
        val id: Long = 0L,
        val name : String = "",
        val rating: String = "" ,
        val posterUrl : String = ""
    )

    data class MoodPickerUiState(
        val moods: List<Mood> = listOf(
            Mood.SAD,
            Mood.THINKING,
            Mood.IN_LOVE,
            Mood.ANGRY,
            Mood.UN_HAPPY,
            Mood.CONFUSED
        ),
        val selectedMood: Mood? = null,
        val selectedMovie: MovieItemUiState = MovieItemUiState(),
        val movies: List<MovieItemUiState> = emptyList(),
        val isLoadingMovies: Boolean = false,
        val openMovieDialog: Boolean = false,
    )

    sealed class HomeError{
        data object NetworkError : HomeError()
    }
}

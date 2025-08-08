package com.amsterdam.viewmodel.home

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.domain.models.Mood
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.viewmodel.shared.defaultMovieGenres
import com.amsterdam.viewmodel.shared.uiStates.MediaType
import com.amsterdam.viewmodel.shared.uiStates.MovieGenreItemUiState
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

data class HomeUiState(
    val popularMediaSectionUiState: PopularMediaSectionUiState = PopularMediaSectionUiState(),
    val upcomingMoviesSectionUiState: UpcomingMoviesSectionUiState = UpcomingMoviesSectionUiState(),
    val topRatedMediaSectionUiState: TopRatedMediaSectionUiState = TopRatedMediaSectionUiState(),
    val continueWatchingMediaSectionUiState: ContinueWatchingMediaSectionUiState = ContinueWatchingMediaSectionUiState(),
    val moodPickerUiState: MoodPickerUiState = MoodPickerUiState(),
    val isLoading: Boolean = true,
    val error: HomeError? = null
) {
    data class PopularMediaSectionUiState(
        val mediaItems: List<PopularMediaItemUiState> = emptyList(),
        val isLoading: Boolean = false,
    )

    data class ContinueWatchingMediaSectionUiState(
        val mediaItems: List<ContinueWatchingHomeItemUiState> = emptyList(),
        val isLoading: Boolean = false,
    )

    data class TopRatedMediaSectionUiState(
        val mediaItems: List<TopRatedMoviesUiState> = emptyList(),
        val isLoading: Boolean = false,
    )

    data class UpcomingMoviesSectionUiState(
        val movies: List<UpcomingMoviesUiState> = emptyList(),
        val movieGenres: List<MovieGenreItemUiState> = defaultMovieGenres,
        val isLoading: Boolean = false,
    ) {
        fun getSelectedUpcomingMovieGenre(): MovieGenre {
            return movieGenres
                .firstOrNull { it.selectableMovieGenre.isSelected }
                ?.selectableMovieGenre?.item ?: MovieGenre.ALL
        }
    }

    data class MoodPickerUiState(
        val moods: List<Mood> = listOf(
            Mood.SAD,
            Mood.NEUTRAL,
            Mood.ROMANTIC,
            Mood.ANGRY,
            Mood.DEPRESSED,
            Mood.SAD_DIZZY
        ),
        val selectedMood: Mood? = null,
        val selectedMovie: MoodPickerItemUiState = MoodPickerItemUiState(),
        val movies: List<MoodPickerItemUiState> = emptyList(),
        val isLoadingMovies: Boolean = false,
        val openMovieDialog: Boolean = false,
        val error: HomeError? = null
    )

    data class PopularMediaItemUiState(
        val id: Long = 0L,
        val name: String = "",
        val rating: String = "",
        val posterUrl: String = "",
        val type: MediaType = MediaType.MOVIE,
        val categories: List<String> = emptyList(),
    )

    data class TopRatedMoviesUiState(
        val id: Long = 0,
        val name: String = "",
        val posterImageUrl: String = "",
        val rate: String = "",
        val yearOfRelease: String = "",
        val mediaType: MediaType = MediaType.MOVIE
    )

    data class UpcomingMoviesUiState(
        val id: Long = 0,
        val name: String = "",
        val posterImageUrl: String = "",
        val rate: String = "",
        val yearOfRelease: String = "",
        val mediaType: MediaType = MediaType.MOVIE
    )

    data class MoodPickerItemUiState(
        val id: Long = 0,
        val name: String = "",
        val posterImageUrl: String = "",
        val rate: String = "",
        val yearOfRelease: String = "",
        val mediaType: MediaType = MediaType.MOVIE
    )

    data class ContinueWatchingHomeItemUiState(
        val id: Long = 0,
        val name: String = "",
        val posterImageUrl: String = "",
        val yearOfRelease: String = "",
        val rate: String = "",
        val dateAdded: Instant = Clock.System.now(),
        val mediaType: MediaType = MediaType.MOVIE
    )

    sealed class HomeError {
        data object NetworkError : HomeError()
        data object UnknownError : HomeError()

        companion object {
            fun toHomeErrorUiState(exception: AflamiException): HomeError {
                return when(exception){
                    is NetworkException -> NetworkError
                    else -> UnknownError
                }
            }
        }
    }
}

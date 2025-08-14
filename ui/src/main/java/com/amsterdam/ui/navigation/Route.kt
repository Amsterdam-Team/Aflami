package com.amsterdam.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    sealed interface Tab : Route {
        @Serializable
        data object Home : Tab

        @Serializable
        data object Lists : Tab

        @Serializable
        data object Categories : Tab

        @Serializable
        data object LetsPlay : Tab

        @Serializable
        data object Profile : Tab
    }

    @Serializable
    data object Login : Route

    @Serializable
    data object Search : Route

    @Serializable
    data object SearchByActor : Route

    @Serializable
    data object SearchByCountry : Route

    @Serializable
    data class MovieDetails(val movieId: Long) : Route

    @Serializable
    data class Cast(
        val mediaId: Long,
        val mediaType: String
    ) : Route

    @Serializable
    data class CategoriesDetails(
        val genreName: String,
    ) : Route
    @Serializable
    data class CategoriesTvShowsDetails(
        val genreName: String
    ) : Route

    @Serializable
    data class SeriesDetails(val tvShowId: Long) : Route

    @Serializable
    data object TopRated : Route

    @Serializable
    data object Register : Route

    @Serializable
    data object ResetPassword : Route

    @Serializable
    data object ContinueWatching : Route

    @Serializable
    data object Onboarding : Route

    @Serializable
    data class ListDetails(
        val listId: Long,
        val listName: String
    ) : Route

    @Serializable
    data object WatchHistory: Route

    @Serializable
    data object MyRating: Route


    @Serializable
    data class GuessReleaseYearGame(val difficulty : String): Route

    @Serializable
    data class GuessCharacter(val difficulty : String): Route

    @Serializable
    data class GenreGame(
        val difficulty: String
    ) : Route

    @Serializable
    data class GuessMovieByPosterGame(val difficulty: String): Route


    @Serializable
    data class ResultScreen (
        val totalCollectedPoints: Int,
        val totalSpentSeconds: Int,
        val gameType: String,
        val difficulty: String
    ): Route
}
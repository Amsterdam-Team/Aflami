package com.example.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    sealed interface Tab {
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
    data class MovieDetails(private val movieId: Long) : Route

    @Serializable
    data class Cast(private val movieId: Long) : Route

    @Serializable
    data class SeriesDetails(private val tvShowId: Long) : Route

    @Serializable
    data object TopRated : Route

    @Serializable
    data object Register : Route

    @Serializable
    data object ResetPassword : Route
}
package com.amsterdam.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Immutable
class NavigationManager(
    private val navController: NavController,
) {

    fun navigateUp() {
        navController.navigateUp()
    }

    fun navigateUpWithFlag(flagName: String, value: Boolean) {
        navController.previousBackStackEntry
            ?.savedStateHandle
            ?.set(flagName, value)

        this.navigateUp()
    }

    // --- Tabs ---
    fun toTab(tab: Route, selectedDestination: Route) {
        if (selectedDestination != tab) {
            navController.navigate(tab) {
                navController.popBackStack(
                    route = Route.Tab.Home,
                    inclusive = true,
                    saveState = true
                )

                launchSingleTop = true
                restoreState = true
            }
        }
    }

    fun toLetsPlay(clearBackStack: Boolean = false) {
        navController.navigate(Route.Tab.LetsPlay) {
            if (clearBackStack) popUpTo(Route.Tab.Home) {
                inclusive = false
            }
        }
    }

    fun toHome(clearBackStack: Boolean = false) {
        navController.navigate(Route.Tab.Home) {
            if (clearBackStack) popUpTo(0)
        }
    }

    // --- Auth & Onboarding ---
    fun toLogin(clearBackStack: Boolean = true) {
        navController.navigate(Route.Login) {
            if (clearBackStack) popUpTo(0)
        }
    }

    fun toRegister() {
        navController.navigate(Route.Register)
    }

    fun toResetPassword() {
        navController.navigate(Route.ResetPassword)
    }

    // --- Search ---
    fun toSearchByKeyword() {
        navController.navigate(Route.Search)
    }

    fun toSearchByActor() {
        navController.navigate(Route.SearchByActor)
    }

    fun toSearchByCountry() {
        navController.navigate(Route.SearchByCountry)
    }

    // --- Details ---
    fun toMovieDetails(movieId: Long) {
        navController.navigate(Route.MovieDetails(movieId))
    }

    fun toSeriesDetails(tvShowId: Long) {
        navController.navigate(Route.SeriesDetails(tvShowId))
    }

    fun toCast(mediaId: Long, mediaType: String) {
        navController.navigate(Route.Cast(mediaId, mediaType))
    }

    fun toCategoriesDetails(genreName: String) {
        navController.navigate(Route.CategoriesDetails(genreName))
    }

    fun toCategoriesTvShowsDetails(genreName: String) {
        navController.navigate(Route.CategoriesTvShowsDetails(genreName))
    }

    fun toListDetails(listId: Long, listName: String) {
        navController.navigate(Route.ListDetails(listId, listName))
    }

    // --- Lists / History ---
    fun toTopRated() {
        navController.navigate(Route.TopRated)
    }

    fun toContinueWatching() {
        navController.navigate(Route.ContinueWatching)
    }

    fun toWatchHistory() {
        navController.navigate(Route.WatchHistory)
    }

    fun toMyRating() {
        navController.navigate(Route.MyRating)
    }

    // --- Game ---
    fun toGenreGame(difficulty: String) {
        navController.navigate(Route.GenreGame(difficulty = difficulty))
    }

    fun toGuessReleaseYearGame(difficulty: String) {
        navController.navigate(Route.GuessReleaseYearGame(difficulty = difficulty))
    }

    fun toGuessCharacter(difficulty: String) {
        navController.navigate(Route.GuessCharacter(difficulty = difficulty))
    }

    fun toGuessMovieByPosterGame(difficulty: String) {
        navController.navigate(Route.GuessMovieByPosterGame(difficulty))
    }

    fun toResultScreen(
        totalCollectedPoints: Int,
        totalSpentSeconds: Int,
        gameType: String,
        difficulty: String,
    ) {
        navController.navigate(
            Route.ResultScreen(
                totalCollectedPoints = totalCollectedPoints,
                totalSpentSeconds = totalSpentSeconds,
                gameType = gameType,
                difficulty = difficulty
            )
        ) {
            popUpTo(Route.Tab.LetsPlay) { inclusive = false }
        }
    }

    @Composable
    fun getCurrentBackStackEntryAsState(): State<NavBackStackEntry?> {
        return navController.currentBackStackEntryAsState()
    }
}

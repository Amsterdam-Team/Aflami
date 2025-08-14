package com.amsterdam.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.amsterdam.ui.screens.cast.castScreenRoute
import com.amsterdam.ui.screens.categories.categoriesScreenRoute
import com.amsterdam.ui.screens.categoriesDetails.movies.categoriesMoviesDetailsScreenRoute
import com.amsterdam.ui.screens.categoriesDetails.tvShow.categoriesTvShowsDetailsScreen
import com.amsterdam.ui.screens.continueWatching.continueWatchingScreenRoute
import com.amsterdam.ui.screens.games.guessByPoster.guessByPosterGameScreenRoute
import com.amsterdam.ui.screens.games.character.guessCharacterScreenRoute
import com.amsterdam.ui.screens.games.releaseYear.guessReleaseYearScreenScreenRoute
import com.amsterdam.ui.screens.games.guessGenre.guessGenreScreenRoute
import com.amsterdam.ui.screens.home.homeScreenRoute
import com.amsterdam.ui.screens.letsPlay.letsPlayScreenRoute
import com.amsterdam.ui.screens.letsPlay.resultScreenRoute
import com.amsterdam.ui.screens.listDetails.listDetailsScreenRoute
import com.amsterdam.ui.screens.lists.listsScreenRoute
import com.amsterdam.ui.screens.login.loginScreenRoute
import com.amsterdam.ui.screens.movieDetails.movieDetailsScreenRoute
import com.amsterdam.ui.screens.onBoarding.onboardingScreenRoute
import com.amsterdam.ui.screens.myRating.myRatingScreenRoute
import com.amsterdam.ui.screens.profile.profileScreenRoute
import com.amsterdam.ui.screens.watchHistory.watchHistoryScreenRoute
import com.amsterdam.ui.screens.register.registerScreenRoute
import com.amsterdam.ui.screens.resetPassword.resetPasswordScreenRoute
import com.amsterdam.ui.screens.search.actorSearch.searchByActorScreenRoute
import com.amsterdam.ui.screens.search.countrySearch.searchByCountryScreenRoute
import com.amsterdam.ui.screens.search.keywordSearch.searchScreenRoute
import com.amsterdam.ui.screens.seriesDetails.seriesDetailsScreenRoute
import com.amsterdam.ui.screens.topRated.topRatedScreenRoute

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: Route = Route.Tab.Home
) {
    NavHost(navController = navController, startDestination = startDestination) {
        loginScreenRoute()
        homeScreenRoute()
        listsScreenRoute()
        letsPlayScreenRoute()
        categoriesScreenRoute()
        profileScreenRoute()
        searchScreenRoute()
        searchByActorScreenRoute()
        searchByCountryScreenRoute()
        movieDetailsScreenRoute()
        seriesDetailsScreenRoute()
        castScreenRoute()
        topRatedScreenRoute()
        registerScreenRoute()
        continueWatchingScreenRoute()
        resetPasswordScreenRoute()
        onboardingScreenRoute()
        listDetailsScreenRoute()
        watchHistoryScreenRoute()
        myRatingScreenRoute()
        categoriesMoviesDetailsScreenRoute()
        categoriesTvShowsDetailsScreen()
        resultScreenRoute()
        guessReleaseYearScreenScreenRoute()
        guessCharacterScreenRoute()
        guessGenreScreenRoute()
        guessByPosterGameScreenRoute()
    }
}
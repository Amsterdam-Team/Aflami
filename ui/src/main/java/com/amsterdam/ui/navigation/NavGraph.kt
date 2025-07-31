package com.amsterdam.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.amsterdam.ui.screens.cast.castScreenRoute
import com.amsterdam.ui.screens.categories.categoriesScreenRoute
import com.amsterdam.ui.screens.continueWatching.continueWatchingScreenRoute
import com.amsterdam.ui.screens.home.homeScreenRoute
import com.amsterdam.ui.screens.letsPlay.letsPlayScreenRoute
import com.amsterdam.ui.screens.lists.listsScreenRoute
import com.amsterdam.ui.screens.login.loginScreenRoute
import com.amsterdam.ui.screens.movieDetails.movieDetailsScreenRoute
import com.amsterdam.ui.screens.profile.profileScreenRoute
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
    }
}
package com.example.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.ui.screens.cast.castScreenRoute
import com.example.ui.screens.categories.categoriesScreenRoute
import com.example.ui.screens.home.homeScreenRoute
import com.example.ui.screens.letsPlay.letsPlayScreenRoute
import com.example.ui.screens.lists.listsScreenRoute
import com.example.ui.screens.login.loginScreenRoute
import com.example.ui.screens.movieDetails.movieDetailsScreenRoute
import com.example.ui.screens.profile.profileScreenRoute
import com.example.ui.screens.register.registerScreenRoute
import com.example.ui.screens.resetPassword.resetPasswordScreenRoute
import com.example.ui.screens.search.actorSearch.searchByActorScreenRoute
import com.example.ui.screens.search.countrySearch.searchByCountryScreenRoute
import com.example.ui.screens.search.keywordSearch.searchScreenRoute
import com.example.ui.screens.seriesDetails.seriesDetailsScreenRoute
import com.example.ui.screens.topRated.topRatedScreenRoute

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: Any = Route.Tab.Home
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
        resetPasswordScreenRoute()
    }
}
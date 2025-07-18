package com.example.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.ui.screens.cast.castScreenRoute
import com.example.ui.screens.categories.categoriesScreenRoute
import com.example.ui.screens.home.homeScreenRoute
import com.example.ui.screens.letsPlay.letsPlayScreenRoute
import com.example.ui.screens.lists.listsScreenRoute
import com.example.ui.screens.movieDetails.movieDetailsScreenRoute
import com.example.ui.screens.profile.profileScreenRoute
import com.example.ui.screens.search.keywordSearch.searchScreenRoute
import com.example.ui.screens.search.actorSearch.searchByActorScreenRoute
import com.example.ui.screens.search.countrySearch.searchByCountryScreenRoute

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: Any = Route.Tab.Home
){
    NavHost(navController = navController, startDestination = startDestination){
        homeScreenRoute()
        listsScreenRoute()
        letsPlayScreenRoute()
        categoriesScreenRoute()
        profileScreenRoute()
        searchScreenRoute()
        searchByActorScreenRoute()
        searchByCountryScreenRoute()
        movieDetailsScreenRoute()
        castScreenRoute()
    }
}
package com.amsterdam.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.amsterdam.ui.screens.categories.categoriesScreenRoute
import com.amsterdam.ui.screens.home.homeScreenRoute
import com.amsterdam.ui.screens.letsPlay.letsPlayScreenRoute
import com.amsterdam.ui.screens.lists.listsScreenRoute
import com.amsterdam.ui.screens.profile.profileScreenRoute
import com.amsterdam.ui.screens.search.searchScreenRoute
import com.amsterdam.ui.screens.searchByActor.searchByActorScreenRoute
import com.amsterdam.ui.screens.searchByCountry.searchByCountryScreenRoute

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
    }
}
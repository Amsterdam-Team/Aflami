package com.amsterdam.ui.screens.lists

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.amsterdam.ui.navigation.Route

fun NavGraphBuilder.listsScreenRoute(){
    composable<Route.Tab.Lists> {
        ListsScreen()
    }
}
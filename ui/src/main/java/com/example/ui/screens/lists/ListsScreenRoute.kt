package com.example.ui.screens.lists

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.ui.navigation.Route

fun NavGraphBuilder.listsScreenRoute() {
    composable<Route.Tab.Lists> {
        ListsScreen()
    }
}
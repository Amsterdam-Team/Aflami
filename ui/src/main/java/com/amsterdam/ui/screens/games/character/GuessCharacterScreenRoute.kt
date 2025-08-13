package com.amsterdam.ui.screens.games.character

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.amsterdam.ui.navigation.Route

fun NavGraphBuilder.guessCharacterScreenRoute(){
    composable<Route.GuessCharacter> {
        GuessCharacterScreen()
    }
}
package com.amsterdam.ui.screens.categoriesDetails.movies

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.amsterdam.ui.navigation.Route
import com.amsterdam.viewmodel.categoriesDetails.movies.CategoriesMovieDetailsArgs

class CategoriesMovieDetailsArgsImpl(savedStateHandle: SavedStateHandle): CategoriesMovieDetailsArgs {
    override val genreName = savedStateHandle.toRoute<Route.CategoriesDetails>().genreName
}
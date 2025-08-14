package com.amsterdam.ui.screens.categoriesDetails.tvShow

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.amsterdam.ui.navigation.Route
import com.amsterdam.viewmodel.categoriesDetails.tvShow.CategoriesTvShowsDetailsArgs

class CategoriesTvShowsDetailsArgsImpl(savedStateHandle: SavedStateHandle): CategoriesTvShowsDetailsArgs {
    override val genreName = savedStateHandle.toRoute<Route.CategoriesTvShowsDetails>().genreName
}
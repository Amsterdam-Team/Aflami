package com.amsterdam.ui.screens.seriesDetails

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.amsterdam.ui.navigation.Route
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsArgs

class SeriesDetailsArgsImpl(savedStateHandle: SavedStateHandle): SeriesDetailsArgs {
    override val tvShowId = savedStateHandle.toRoute<Route.SeriesDetails>().tvShowId
}
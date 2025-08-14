package com.amsterdam.ui.screens.cast

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.amsterdam.ui.navigation.Route
import com.amsterdam.viewmodel.cast.CastScreenArgs

class CastScreenArgsImpl(savedStateHandle: SavedStateHandle): CastScreenArgs {
    override val mediaId = savedStateHandle.toRoute<Route.Cast>().mediaId
    override val mediaType = savedStateHandle.toRoute<Route.Cast>().mediaType
}
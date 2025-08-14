package com.amsterdam.ui.screens.listDetails

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.amsterdam.ui.navigation.Route
import com.amsterdam.viewmodel.listDetails.ListDetailsArgs

class ListDetailsArgsImpl(savedStateHandle: SavedStateHandle): ListDetailsArgs {
    override val listId = savedStateHandle.toRoute<Route.ListDetails>().listId
    override val listName = savedStateHandle.toRoute<Route.ListDetails>().listName
}
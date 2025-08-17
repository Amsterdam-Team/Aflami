package com.amsterdam.viewmodel.cast

import com.amsterdam.entity.Actor
import com.amsterdam.viewmodel.cast.CastUiState.ActorUiState

private fun Actor.toActorUiState(): ActorUiState =
    ActorUiState(actorImage = imageUrl, actorName = name)

fun List<Actor>.toActorsUiState(): List<ActorUiState> = map { it.toActorUiState() }
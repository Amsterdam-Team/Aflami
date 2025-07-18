package com.example.viewmodel.cast.mapper

import com.example.entity.Actor
import com.example.viewmodel.cast.CastUiState.CastItemUiState

fun Actor.toUiState() = CastItemUiState(
    actorImage = imageUrl,
    actorName = name,
)
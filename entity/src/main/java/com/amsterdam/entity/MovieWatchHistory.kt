package com.amsterdam.entity

import kotlinx.datetime.Instant

data class MovieWatchHistory(
    val movie: Movie,
    val lastWatchedTime: Instant
)
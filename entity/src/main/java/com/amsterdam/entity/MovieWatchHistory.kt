package com.amsterdam.entity

import kotlinx.datetime.Instant

data class MovieWatchHistory(
    val movieId: Long,
    val lastWatchedTime: Instant
)
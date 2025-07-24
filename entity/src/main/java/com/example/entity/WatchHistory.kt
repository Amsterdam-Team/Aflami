package com.example.entity

import kotlinx.datetime.Instant

data class WatchHistory(
    val movieId: Long,
    val lastWatchedTime: Instant
)